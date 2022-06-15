package com.novedu.nov.order.service.impl;

/**
 * @author ：juam
 * @date ：2022/2/10 14:41
 * @description：
 * @modified By：
 * @version:
 */

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alipay.api.internal.util.AlipaySignature;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.order.client.EduClient;
import com.novedu.nov.order.config.AlipayConfig;
import com.novedu.nov.order.entity.EduCourseApply;
import com.novedu.nov.order.entity.TradeOrder;
import com.novedu.nov.order.service.AlipayService;
import com.novedu.nov.order.service.TradeOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    EduClient eduClient;

    @Autowired
    TradeOrderService orderService;

    @Override
    public void webPagePay(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Long orderId) throws Exception {
        TradeOrder order = orderService.getById(orderId);
        BaseResult baseResult = eduClient.queryCourseDetail(order.getCourseId());
        httpResponse.setContentType("text/html;charset=UTF-8");
        if (BaseResult.serviceInvokeFailure().getCode().equals(baseResult.getCode())) {
            httpResponse.getWriter().write(baseResult.getMsg());
            return;
        }
        Map courseInfo = (Map) baseResult.getData();
        String courseTitle = courseInfo.get("courseTitle").toString();
        String courseId = courseInfo.get("courseId").toString();
        BigDecimal totalFee = order.getTotalFee();
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.GATEWAYURL, AlipayConfig.APP_ID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGN_TYPE);  //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest(); //创建API对应的request
        alipayRequest.setReturnUrl(AlipayConfig.return_url + "/" + courseId);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url); //在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"" + orderId + "\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":" + totalFee + "," +
                "    \"subject\":\"" + courseTitle + "\"," +
                "    \"body\":\"" + courseTitle + "\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }" +
                "  }"); //填充业务参数
        String form = "";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody();  //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        order.setPaidTime(new Date());
        order.setPayType(2);
        order.setStatus(1);
        if (orderService.updateById(order)) {
            log.info("同步订单:" + orderId + "成功");
            baseResult = eduClient.statisticsCourseBuyCount();
            if (BaseResult.serviceInvokeFailure().getCode().equals(baseResult.getCode())) {
                httpResponse.getWriter().write(baseResult.getMsg());
                return;
            }
        }
        httpResponse.setContentType("text/html;charset=utf-8");
        httpResponse.getWriter().write(form); //直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @Override
    public String appPagePay(String outTradeNo, Integer totalAmount, String subject) throws Exception {
        return null;
    }

    @Override
    public void getNotifyInfo(HttpServletRequest request, HttpServletResponse httpResponse) {
        log.info("收到支付宝异步回调 start...");
        //接收参数进行校验
        Map<String, String> parameters = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        System.out.println(requestParams.toString());
        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            parameters.put(key, valueStr);
        }
        //appid
        String appId = request.getParameter("app_id");
        //我们调用appPagePay方法 传入的订单编号
        String orderId = request.getParameter("out_trade_no");
//        支付宝交易号
        String tradeNo = request.getParameter("trade_no");
        try {
            //交易状态
            String payState = request.getParameter("trade_status");
//                用户支付金额
            BigDecimal totalAmount = new BigDecimal(request.getParameter("total_amount"));
//                实收金额
            BigDecimal receiptAmount = new BigDecimal(request.getParameter("receipt_amount"));
            log.info("交易状态:{},支付金额为：{},实付金额为：{}", payState, totalAmount, receiptAmount);
            //验证签名
            boolean signVerified = AlipaySignature.rsaCheckV1(parameters, AlipayConfig.ALIPAY_PUBLIC_KEY, "UTF-8", AlipayConfig.SIGN_TYPE);
//                验签  验证商户id  验证支付宝返回状态
            if (signVerified && AlipayConfig.APP_ID.equals(appId) && "TRADE_SUCCESS".equals(payState)) {
                //你的业务参数    判断成功后给支付宝返回7个字符的success

            }
        } catch (AlipayApiException e) {
            log.error(e.getErrMsg());
            throw new RuntimeException("调用支付宝接口发生异常");
        }

    }
}

