package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.CmsNotice;
import com.novedu.nov.edu.mapper.CmsNoticeMapper;
import com.novedu.nov.edu.service.CmsNoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    private String accessKey = "access_num";

    @Override
    public BaseResult saveOrUpdateNotice(CmsNotice cmsNotice) {
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
    public BaseResult receiveNotice() {
        if(redisTemplate.hasKey(accessKey)){
           Integer accessNum = (Integer) redisTemplate.opsForValue().get(accessKey);
           accessNum++;
           redisTemplate.opsForValue().set(accessKey,accessNum);
        }
        else
            redisTemplate.opsForValue().set(accessKey,1);
        return BaseResult.success(query().eq("type",1).orderByDesc("create_time").last("limit 1").one());
    }


}
