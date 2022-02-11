package com.novedu.nov.order.service;

import com.alipay.api.AlipayApiException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ：juam
 * @date ：2022/2/10 14:22
 * @description：
 * @modified By：
 * @version:
 */

public interface AlipayService {
    /**
     * web端订单支付
     *
     * @param outTradeNo  订单编号（唯一）
     * @param totalAmount 订单价格
     * @param subject     商品名称
     */
    void webPagePay(HttpServletRequest httpRequest, HttpServletResponse httpResponse,Long orderId) throws Exception;

    /**
     * app端订单支付
     *
     * @param outTradeNo  订单编号
     * @param totalAmount 订单价格
     * @param subject     商品名称
     */
    String appPagePay(String outTradeNo, Integer totalAmount, String subject) throws Exception;


    void getNotifyInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse);
}

