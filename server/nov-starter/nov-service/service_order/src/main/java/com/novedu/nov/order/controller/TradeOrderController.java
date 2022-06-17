package com.novedu.nov.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.annotation.UserMultiSubmitLimit;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.order.entity.TradeOrder;
import com.novedu.nov.order.service.TradeOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author juam
 * @since 2022-02-08
 */
@RestController
@RequestMapping("/order/trade")
@Api("订单管理的接口文档")
public class TradeOrderController {

    @Autowired
    TradeOrderService tradeOrderService;

    @UserMultiSubmitLimit
    @ApiOperation("创建订单")
    @PostMapping("/create")
    public BaseResult createOrder(@RequestBody TradeOrder tradeOrder) {
        return tradeOrderService.createOrder(tradeOrder);
    }

    @PostMapping("/export")
    public void exportCoursePage(HttpServletResponse response, Page page, TradeOrder order) {
        tradeOrderService.exportOrderPage(response, page, order);
    }

    @GetMapping("/export-all")
    public void exportAll(HttpServletResponse response, TradeOrder order) {
        tradeOrderService.exportAll(response, order);
    }

    @ApiOperation("查询订单")
    @PostMapping("/detail/{id}")
    public BaseResult queryOrderById(@PathVariable Long id) {
        return tradeOrderService.queryOrderById(id);
    }

    @ApiOperation("查询成交订单数量")
    @PostMapping("/count/{teacherId}")
    public BaseResult queryOrderCount(@PathVariable Long teacherId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (teacherId != null && teacherId != 0)
            queryWrapper.eq("id", teacherId);
        queryWrapper.eq("status", 1);
        List<TradeOrder> orders = tradeOrderService.list(queryWrapper);
        Map info = new HashMap();
        BigDecimal amount = BigDecimal.ZERO;
        for (TradeOrder o : orders) {
            amount = amount.add(o.getTotalFee());
        }
        info.put("count", orders.size());
        info.put("amount", amount);
        return BaseResult.success(info);
    }

    @ApiOperation("查询用户是否已经下单")
    @PostMapping("/hasbuy/{id}/{uid}")
    public BaseResult queryOrderByUidAndCourseId(@PathVariable Long id, @PathVariable Long uid) {
        return tradeOrderService.queryOrderByUidAndCourseId(id, uid);
    }

    @PostMapping("/page")
    public BaseResult queryOrderPage(Page page, TradeOrder order) {
        return tradeOrderService.queryOrderPage(page, order);
    }

    @GetMapping("/page")
    public BaseResult queryUserOrderPage(Page page) {
        return tradeOrderService.queryUserOrderPage(page);
    }
}

