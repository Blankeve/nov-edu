package com.novedu.nov.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.api.RoleType;
import com.novedu.nov.common.util.ExcelUtils;
import com.novedu.nov.common.util.JwtUtils;
import com.novedu.nov.order.client.EduClient;
import com.novedu.nov.order.client.UcenterClient;
import com.novedu.nov.order.entity.EduCourseApply;
import com.novedu.nov.order.entity.TradeOrder;
import com.novedu.nov.order.mapper.TradeOrderMapper;
import com.novedu.nov.order.service.TradeOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    EduClient eduClient;

    @Autowired
    UcenterClient ucenterClient;

    @Autowired
    TradeOrderMapper orderMapper;

    @Autowired
    RedisTemplate redisTemplate;



    @Override
    public BaseResult createOrder(TradeOrder tradeOrder) {
        Long courseId = tradeOrder.getCourseId();
        Long uid = tradeOrder.getUid();
        String userOrderKey = "order_course_" + courseId + "_uid_" + uid;
        boolean hasKey = redisTemplate.hasKey(userOrderKey);
        if (hasKey) {
            TradeOrder order = (TradeOrder) redisTemplate.opsForValue().get(userOrderKey);
            if (order != null) {
                return BaseResult.success("该课程当前已有未支付订单").mapSet("order", order.getId() + "");
            }
        }
        Map courseInfo = (Map) eduClient.queryCourseDetail(courseId).getData();
        Map memberInfo = (Map) ucenterClient.getMemberInfo(uid).getData();
        tradeOrder.setCourseCover(courseInfo.get("courseCover").toString());
        tradeOrder.setCourseTitle(courseInfo.get("courseTitle").toString());
        tradeOrder.setTeacherName(courseInfo.get("teacherName").toString());
        String price = courseInfo.get("coursePrice").toString();
        tradeOrder.setTotalFee(new BigDecimal(price));
        tradeOrder.setMobile(memberInfo.get("mobile").toString());
        tradeOrder.setNickname(memberInfo.get("nickname").toString());
        boolean success = save(tradeOrder);
        if (!success) {
            return BaseResult.error("创建订单失败");
        }
        redisTemplate.opsForValue().set(userOrderKey, tradeOrder, 30, TimeUnit.MINUTES);
        return BaseResult.success().mapSet("order", tradeOrder.getId() + "");
    }

    @Override
    public BaseResult queryOrderById(Long id) {
        return BaseResult.success(getById(id));
    }

    @Override
    public BaseResult queryOrderPage(Page page, TradeOrder order) {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String token = request.getHeader("X-Token");
        String uid = JwtUtils.getAudience(token).get("uid");
        BaseResult baseResult = ucenterClient.queryUserRole(Long.valueOf(uid));
        if (baseResult == null) {
            return BaseResult.success();
        }
        Map role = (Map) baseResult.getData();
        Integer code = (Integer) role.get("code");
        if (code == RoleType.TEACHER.getCode()) {
            BaseResult baseResult1 = eduClient.queryTeacherIdByUid(uid);
            if (baseResult1 == null)
                return BaseResult.success();
            order.setTeacherId(Long.valueOf(baseResult1.getData().toString()));
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.hasText(order.getNickname()))
            queryWrapper.like("u.nickname", order.getNickname());
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
        return BaseResult.success(orderMapper.queryOrderPage(page, queryWrapper));
    }

    @Override
    public BaseResult queryOrderByUidAndCourseId(HttpServletRequest request, Long id) {
        String token = request.getHeader("X-Token");
        if (!StringUtils.hasText(token))
            return BaseResult.success("未登录");
        Long uid = Long.valueOf(JwtUtils.getAudience(token).get("uid"));
        EduCourseApply courseApply = new EduCourseApply();
        courseApply.setUid(uid);
        courseApply.setCourseId(id);
        BaseResult baseResult = eduClient.queryCourseApplyByCourseIdAndUid(courseApply);
        if (BaseResult.success().getCode().equals(baseResult.getCode()))
            return BaseResult.success("已报名该课程").mapSet("hasBuy", "1");
        else if (BaseResult.serviceInvokeFailure().getCode().equals(baseResult.getCode()))
            return BaseResult.serviceInvokeFailure();
        return BaseResult.success("暂未购买该课程");
    }

    @Override
    public void exportOrderPage(HttpServletResponse response, Page page, TradeOrder order) {
        BaseResult baseResult = queryOrderPage(page, order);
        if (baseResult != null && BaseResult.success().getCode().equals(baseResult.getCode())) {
            Page page1 = (Page) baseResult.getData();
            ExcelUtils.exportExcel(page1.getRecords(), "订单信息", "订单信息", TradeOrder.class, "订单信息", response);
        }
    }

    @Override
    public void exportAll(HttpServletResponse response, TradeOrder order) {
        BaseResult baseResult = queryOrderPage(new Page(1, count()), order);
        if (baseResult != null && BaseResult.success().getCode().equals(baseResult.getCode())) {
            Page page1 = (Page) baseResult.getData();
            ExcelUtils.exportExcel(page1.getRecords(), "订单信息", "订单信息", TradeOrder.class, "订单信息", response);
        }
    }

}
