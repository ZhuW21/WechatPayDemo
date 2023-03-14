package com.example.wechatpaydemo.v3.common;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author qingzhou
 * @date 2023-03-13 17:32
 **/
@Data
@Component
public class WechatMerchant {

    /**
     * 微信支付商户号
     */
    private String mchId = "微信支付商户号";

    /**
     * AppID（从微信公众平台获取）
     */
    private String appId = "AppID";

    /**
     * AppSecret（即 AppID 对应的密码，同样从微信公众平台获取）
     */
    private String appSecret = "/";

    /**
     * API 密钥（V2）
     */
    private String apiKey = "API 密钥（V2）";

    /**
     * 微信回调地址的服务器域名，如：http://xxx.com，SDK 在发送请求时会自动补全对应回调的 url
     */
    private String notifyDomain = "微信回调地址";

    /**
     * API 证书绝对路径（在调用微信支付安全级别较高的接口，如：退款、企业红包、企业付款时，会使用到 API 证书）
     */
    private String apiCertPath = "/";

    /**
     * 是否沙箱环境
     */
    private boolean isSandbox = false;

    /**
     * 是否启用V3版本接口
     */
    private Boolean isV3 = true;

    /**
     * V3接口API秘钥
     */
    private String v3ApiKey = "V3接口API秘钥";

    /**
     * 商户证书序列号
     */
    private String serialNo = "商户证书序列号";

    /**
     * 商户API私钥
     */
    private String privateCer = "商户API私钥";
}
