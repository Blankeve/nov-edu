package com.novedu.nov.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.order.entity.TradeOrder;
import com.novedu.nov.order.entity.dto.TradeOrderDTO;

import javax.servlet.http.HttpServletResponse;

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

    BaseResult queryOrderPage(Page page, TradeOrderDTO order);

    BaseResult queryOrderByUidAndCourseId(Long id,Long uid);

    void exportOrderPage(HttpServletResponse response, Page page, TradeOrderDTO order);

    void exportAll(HttpServletResponse response,TradeOrderDTO order);

    BaseResult queryUserOrderPage(Page page);
}
