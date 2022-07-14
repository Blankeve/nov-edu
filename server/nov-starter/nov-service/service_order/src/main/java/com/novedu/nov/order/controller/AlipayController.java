package com.novedu.nov.order.controller;

import com.novedu.nov.order.service.AlipayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ：juam
 * @date ：2022/2/10 14:43
 * @description：
 * @modified By：
 * @version:
 */
@RestController
@RequestMapping("/order/alipay")
@Api("支付宝支付的接口文档")
public class AlipayController {

    @Autowired
    AlipayService alipayService;

    @ApiOperation("电脑网页支付")
    @GetMapping("/web")
    public void doPay(HttpServletRequest httpRequest,HttpServletResponse httpResponse, Long id) throws Exception {
            alipayService.webPagePay(httpRequest,httpResponse,id);
    }

    @ApiOperation("支付完成通知")
    @PostMapping("/notify")
    public void notify(HttpServletRequest httpRequest,HttpServletResponse httpResponse) throws Exception {
        alipayService.getNotifyInfo(httpRequest,httpResponse);
    }
}
