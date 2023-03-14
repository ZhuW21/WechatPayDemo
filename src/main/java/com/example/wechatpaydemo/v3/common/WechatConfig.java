package com.example.wechatpaydemo.v3.common;

import lombok.Builder;
import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 微信支付配置
 *
 * @author qingzhou
 * @date 2023-03-13 16:59
 */
@Data
@Builder
public class WechatConfig {

    /**
     * 微信支付商户号
     */
    private String mchId;

    /**
     * AppID（从微信公众平台获取）
     */
    private String appId;

    /**
     * AppSecret（即 AppID 对应的密码，同样从微信公众平台获取）
     */
    private String appSecret;

    /**
     * API 密钥 (V2中使用)
     */
    private String apiKey;

    /**
     * 微信回调地址的服务器域名，如：http://xxx.com，SDK 在发送请求时会自动补全对应回调的 url
     */
    private String notifyDomain;

    /**
     * API 证书绝对路径（在调用微信支付安全级别较高的接口，如：退款、企业红包、企业付款时，会使用到 API 证书）
     */
    private String apiCertPath;

    /**
     * 是否启用V3版本接口
     */
    private Boolean isV3;

    /**
     * V3 api秘钥
     */
    private String v3ApiKey;

    /**
     * 商户API证书序列号
     */
    private String serialNo;

    /**
     * 商户API私钥
     */
    private String privateCer;

    public String getPrivateCerStr(){
        return privateCer;
    }

    public byte[] getPrivateCer() {
        return Base64.getDecoder().decode(privateCer.getBytes(StandardCharsets.UTF_8));
    }
}
