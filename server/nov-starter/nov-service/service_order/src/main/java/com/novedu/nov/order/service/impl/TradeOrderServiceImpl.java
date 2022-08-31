package com.novedu.nov.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.base.RoleType;
import com.novedu.nov.common.constants.RedisKeyConstants;
import com.novedu.nov.common.util.RequestUtils;
import com.novedu.nov.order.client.OpenEduService;
import com.novedu.nov.order.client.OpenUcenterService;
import com.novedu.nov.order.entity.TradeOrder;
import com.novedu.nov.order.entity.dto.TradeOrderDTO;
import com.novedu.nov.order.mapper.TradeOrderMapper;
import com.novedu.nov.order.service.TradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-02-08
 */
@Service
public class TradeOrderServiceImpl extends ServiceImpl<TradeOrderMapper, TradeOrder> implements TradeOrderService {

    @Autowired
    OpenEduService openEduService;

    @Autowired
    OpenUcenterService openUcenterService;

    @Autowired
    TradeOrderMapper orderMapper;

    @Autowired
    RedisTemplate redisTemplate;


    @Override
    public BaseResult createOrder(TradeOrder tradeOrder) {
        Long courseId = tradeOrder.getCourseId();
        Long uid = tradeOrder.getUid();
        String userOrderKey = RedisKeyConstants.USER_ORDER + courseId + "_uid_" + uid;
        boolean hasKey = redisTemplate.hasKey(userOrderKey);
        if (hasKey) {
            TradeOrder order = (TradeOrder) redisTemplate.opsForValue().get(userOrderKey);
            if (order != null) {
                return BaseResult.success("该课程当前已有未支付订单").map("order", order.getId() + "");
            }
        }
        Map courseInfo = (Map) openEduService.queryCourseDetail(courseId).getData();
        Map memberInfo = (Map) openUcenterService.getMemberInfo(uid).getData();
        tradeOrder.setCourseCover(courseInfo.get("courseCover").toString());
        tradeOrder.setCourseTitle(courseInfo.get("courseTitle").toString());
        tradeOrder.setTeacherName(courseInfo.get("teacherName").toString());
        String price = courseInfo.get("coursePrice").toString();
        tradeOrder.setTotalFee(new BigDecimal(price));
        tradeOrder.setMobile(memberInfo.get("mobile").toString());
        tradeOrder.setNickname(memberInfo.get("nickname").toString());
        //当课程免费时，直接创建订单并完成支付状态
        if (Float.valueOf(price) == 0) {
            tradeOrder.setPaidTime(new Date());
            tradeOrder.setPayType(0);
            tradeOrder.setStatus(1);
        }
        boolean success = save(tradeOrder);
        if (!success) {
            return BaseResult.error("创建订单失败");
        }
        BaseResult baseResult = openEduService.statisticsCourseBuyCount();
        if (baseResult == null || BaseResult.error().getCode().equals(baseResult.getCode())) {
            log.error("学习人数同步失败");
        }
        redisTemplate.opsForValue().set(userOrderKey, tradeOrder, 30, TimeUnit.MINUTES);
        return BaseResult.success().map("order", tradeOrder.getId() + "");
    }

    @Override
    public BaseResult queryOrderById(Long id) {
        return BaseResult.success(getById(id));
    }

    @Override
    public IPage<TradeOrder> queryOrderPage(Page page, TradeOrderDTO order) {
        Long uid = RequestUtils.getUid();
        BaseResult baseResult = openUcenterService.queryUserRole(Long.valueOf(uid));
        if (baseResult == null) {
            return new Page<>();
        }
        Map role = (Map) baseResult.getData();
        Integer code = (Integer) role.get("code");
        if (code == RoleType.TEACHER.getCode()) {
            BaseResult baseResult1 = openEduService.queryTeacherIdByUid(uid.toString());
            if (baseResult1 == null)
                return new Page<>();
            order.setTeacherId(Long.valueOf(baseResult1.getData().toString()));
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like(StringUtils.hasText(order.getNickname()), "u.nickname", order.getNickname());
        if (order.getCourseId() != null)
            queryWrapper.eq("o.course_id", order.getCourseId());
        if (order.getUid() != null)
            queryWrapper.eq("o.uid", order.getUid());
        if (order.getTeacherId() != null)
            queryWrapper.eq("o.teacher_id", order.getTeacherId());
        if (order.getPayType() != null)
            queryWrapper.eq("o.pay_type", order.getPayType());
        if (order.getStatus() != null)
            queryWrapper.eq("o.status", order.getStatus());
        Date start = order.getStartTime();
        Date end = order.getEndTime();
        if (start != null && end != null && end.getTime() > start.getTime())
            queryWrapper.apply("o.create_time > date_format({0},'%Y-%m-%d %H:%i:%s') and o.create_time < date_format({1},'%Y-%m-%d %H:%i:%s')", start, end);
        return orderMapper.queryOrderPage(page, queryWrapper);
    }

    @Override
    public BaseResult queryOrderByUidAndCourseId(Long id, Long uid) {
        if (uid == null || uid == 1)
            uid = RequestUtils.getUid();
        if (uid == null)
            return BaseResult.success();
        LambdaQueryWrapper<TradeOrder> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(TradeOrder::getUid, uid);
        queryWrapper.eq(TradeOrder::getCourseId, id);
        queryWrapper.eq(TradeOrder::getStatus, 1);
        int count = count(queryWrapper);
        return BaseResult.success().map("paid", count > 0);
    }

    @Override
    public BaseResult queryUserOrderPage(Page page) {
        Long uid = RequestUtils.getUid();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("o.uid", uid);
        queryWrapper.eq("o.status", 1);
        return BaseResult.success(orderMapper.queryOrderPage(page, queryWrapper));
    }

}
