package com.novedu.nov.order.config;

/**
 * @author ：juam
 * @date ：2022/2/10 14:17
 * @description：
 * @modified By：
 * @version:
 */
import java.io.FileWriter;
import java.io.IOException;

public class AlipayConfig {

    //http://localhost:8086/order/alipay/web
    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String APP_ID = "2021000119610730";

    // 商户私钥，您的PKCS8格式RSA2私钥，这些就是我们刚才设置的
    public static String RSA_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCavZNnaNl0zprvWc0mbp6aOEOPLtnQdidHPicP/bw8ySkSraiu+W/rZK2aWgBqUu6EBs070o+mfRCrNgioLMZQoZDBF0QofnqCDAiPFeU9EgW4uiQmJMVEqgLZ4NWMtjgXaAKntaCzxXRSrefWTmbWzz6tj5hJm7WRZMJMqEcXLC76GwEwwSOcZ+dUZFi7K/L2VrlWKdECoEGA4xZaEtKLhtl6NHfznpZ93JXV3PVb3WNPDTFI7pex4C/Rpx4S0UM6C9MNCUZq5lDnVScLhCIc8KIihD2IkA4dAVKDs8FRi274u24xleTL5Cf1H46FEeutJLLMNw7ET96pwHiSpDkbAgMBAAECggEBAJD/BvqlMu7TEjcp7KQQj6lvA09N1WgS7udBBBVl61cCH+ySsTeUIB1wr4OsxirbHwiz3ugpe9XD5zSnYyFDI2k2m49zn7haG610XSjGmZHKK3ZXZbwgRID81cMXfMNnk9PK8IH6HWWWplM7Bby4poCuncrAfTCsKUzeH3r5S/QXS63BEav1zmepj/BnOgmbaiuCoQVcWXWge+HPVh1Ofi0iScBCGYhvxEDubdO+VDMHs7fKAmhEJ8ClDGCZ4za7UBT+rjVvfJFrytSuVRonSwBznnc4swD8jNeEnQv2HAj4kxTsN8Dmk2Lst+Qpkl8Zg1sApTVWXHCdBFQ1d0B8PCECgYEAy3yt5+hkLjoDLoBQb/wUxpRWN+JPpFV6eNgZp2i1XD0L4+LyIwIf/yfP+JhIs56IvHLmu1E7UT5K6XW/U9fu9K93pG2NhhcuFIghSdD++dKuXSN2td79fwfANHUlXuUIMxJ7NbCwr99zQSVspajztFtnTR1DXa3XbmUdjnXkVYkCgYEAwqx4lO4wHDBYGc8rpvo/JvOJT9p2mxa1LCxhHbeXud/bkcozhoPrPcf6IIVFldVTfWIs1o4lextx78jsmfZSxmYIZoh7VZq+mKGyZuCKjiHtofi2KU0LvEehmHKTQOcrhBQTfv3YvbnOujPZme1Ev+rMQwzGR2HRXqVkj7F/1IMCgYBMa93QO9nr9oOdYjlTZWPUFH+w60Cn4iZl3hr8YXnDU3X0txXjCfhEVopnvaGIO+D0jV6UOPTDFVcDCOK7NVyb7o5LZY2CL5Bxy5Jc663m2zX66se5lC62F44a5WgMlI2YyLcXOe2Rj6T0cX61VJd6Q92u+2+Cbjh7UGRiTWpcqQKBgQCRLWH3XvIN85egF7msGq+B8tiR7o/I9NRDjw0VxlamTWVHgg2O4U5LkpjcK8dLB/3tYYuirfsRpdQASEz6PJJaVrkpDds8adx9owiSZB2wBWlcA0iF4GlCDgFa2QcrvbncE8S+TbyXMLwUbJp4ilNc9FNEJYCwtygWdy1ZY7u61QKBgQCTv0Hm9A45+emFxPloY2OicbePK+bNqDSDaC8dxV7YADkZB1GL4jH4dNm6V/v9ULm8whS385tCIn7h5eXfFjU164BOGagtX7hmOvtYHy8nUF8vkMSpfDeFTTpUL0vxZ6sEAiY9T9rZP0Y+blzCGUcl202j1YNvWZgNnYL3F2IIaA==";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm
    // 对应APPID下的支付宝公钥。，这些就是我们刚才设置的
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj2OkCeRA04Pjew6N7UiTfGVJaF1vvs38otVCAJ58yJkFhnitGNgAXLF19OTMnEzf9FUf3Avosd5WAXXlxcCUUggnxWFDkAZICgf02eA1IHt/GwDtX8zrRByEfl6nMZsLv8DHxhAHOKtuOMJEkBL/oXIGiNwgTE39aGUGWcvI1tls6bQbtRWxMSASnMVGYqvpaws0v4I10wxDQybsPbB6LMCLaVL5yiRwbBZIBy5A2s8uiRvA7wlTPdDWAhWDl0wRjLg0oGGZGd/WzEfEcJTRR9VNL6ATMPX1RtaRaOxU7SAwFY0f1nFyPp52vfGkQgKxUYUPBJ3cxzmg19vlPej83wIDAQAB";

    // 异步通知，再这里我们设计自己的后台代码
    public static String notify_url = "http://localhost:3000/order/alipay/notify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://localhost:3000/course";

    // 签名方式
    public static String SIGN_TYPE = "RSA2";

    // 字符编码格式
    public static String CHARSET = "utf-8";

    // 支付宝网关 https://openapi.alipaydev.com/gateway.do https://openapi.alipay.com/gateway.do
    public static String GATEWAYURL = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String LOG_PATH = "/home/zhaohy/alipayLog/";

    public static String FORMAT = "json";

    // ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     *
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(LOG_PATH + "alipay_log_" + System.currentTimeMillis() + ".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

