package com.novedu.nov.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.order.entity.TradeOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author juam
 * @since 2022-02-08
 */
public interface TradeOrderService extends IService<TradeOrder> {

    BaseResult createOrder(TradeOrder tradeOrder);

    BaseResult queryOrderById(Long id);

    BaseResult queryOrderPage(Page page, TradeOrder order);

    BaseResult queryOrderByUidAndCourseId(HttpServletRequest request, Long id);
}
