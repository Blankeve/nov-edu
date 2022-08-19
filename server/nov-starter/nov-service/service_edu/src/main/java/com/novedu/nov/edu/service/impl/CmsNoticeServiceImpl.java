package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.constants.RedisKeyConstants;
import com.novedu.nov.common.util.RequestUtils;
import com.novedu.nov.edu.entity.CmsNotice;
import com.novedu.nov.edu.mapper.CmsNoticeMapper;
import com.novedu.nov.edu.service.CmsNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 广告推荐 服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-02-28
 */
@Service
public class CmsNoticeServiceImpl extends ServiceImpl<CmsNoticeMapper, CmsNotice> implements CmsNoticeService {

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public BaseResult saveOrUpdateNotice(CmsNotice cmsNotice) {
        cmsNotice.setSendUser(RequestUtils.getUsername());
        return BaseResult.successOrError(saveOrUpdate(cmsNotice));
    }

    @Override
    public BaseResult removeNotice(String id) {
        return BaseResult.successOrError(removeById(id));
    }

    @Override
    public BaseResult queryNoticePage(Page page, CmsNotice cmsNotice) {
        return BaseResult.success(page(page));
    }

    @Override
    public BaseResult receiveNotice(String id) {
        //统计首页点击量
        if (redisTemplate.hasKey(RedisKeyConstants.ACCESS_NUM)) {
            Integer accessNum = (Integer) redisTemplate.opsForValue().get(RedisKeyConstants.ACCESS_NUM);
            accessNum++;
            redisTemplate.opsForValue().set(RedisKeyConstants.ACCESS_NUM, accessNum);
        } else
            redisTemplate.opsForValue().set(RedisKeyConstants.ACCESS_NUM, 1);
        //接收最新公告
        CmsNotice notice;
        if (redisTemplate.hasKey(RedisKeyConstants.FRONT_NOTICE)) {
            notice = (CmsNotice) redisTemplate.opsForValue().get(RedisKeyConstants.FRONT_NOTICE);
        } else {
            notice = lambdaQuery().eq(CmsNotice::getType, 1).orderByDesc(CmsNotice::getCreateTime).last("limit 1").one();
            redisTemplate.opsForValue().set(RedisKeyConstants.FRONT_NOTICE, notice);
        }
        if (!notice.getId().toString().equals(id))
            return BaseResult.success(notice);
        else
            return BaseResult.success().map("id", notice.getId().toString());
    }


}
