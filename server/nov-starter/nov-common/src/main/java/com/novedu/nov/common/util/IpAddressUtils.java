
package com.novedu.nov.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * 获取IP方法
 */
@Slf4j
public class IpAddressUtils {

    // IP地址查询
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    public static String getIpAddress(HttpServletRequest request) {
        String ip = null;
        // X-Real-IP：nginx服务代理
        String ipAddresses = request.getHeader("X-Real-IP");
        System.out.println("获取nginx的ip：" + ipAddresses);
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Forwarded-For：Squid 服务代理
            ipAddresses = request.getHeader("X-FORWARDED-FOR");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }
        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;

    }

    public static String getRealAddressByIP(String ip)
    {
        // 内网不查询
        if ("127.0.0.1".equals(ip))
        {
            return "内网IP";
        }

            try
            {
                String rspStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", "GBK");
                if (StringUtils.isEmpty(rspStr))
                {
                    log.error("获取地理位置异常 {}", ip);
                    return UNKNOWN;
                }
                ObjectMapper objectMapper = new ObjectMapper();
                Map jsonObject = objectMapper.readValue(rspStr, Map.class);
                String pro = (String) jsonObject.get("pro");
                String city = (String) jsonObject.get("city");
                String region = (String) jsonObject.get("region");
                return String.format("%s %s %s", pro, city, region);
            }
            catch (Exception e)
            {
                log.error("获取地理位置异常 {}", ip);
            }

        return UNKNOWN;
    }

}