package com.novedu.nov.order.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.order.entity.TradeOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 订单 Mapper 接口
 * </p>
 *
 * @author juam
 * @since 2022-02-08
 */
@Repository
public interface TradeOrderMapper extends BaseMapper<TradeOrder> {

    IPage<TradeOrder> queryOrderPage(Page page, @Param("ew") Wrapper<TradeOrder> queryWrapper);
}
