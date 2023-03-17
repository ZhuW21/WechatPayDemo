package com.example.wechatpaydemo.v3.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信支付结果通知
 *
 * @author qingzhou
 * @date 2023-03-17 10:12
 **/
@Data
@Accessors(chain = true)
public class WechatPayCallBackResp {
    /**
     * 通知ID
     */
    private String id;

    /**
     * 通知创建时间
     */
    private String create_time;

    /**
     * 通知类型
     */
    private String event_type;

    /**
     * 通知数据类型
     */
    private String resource_type;

    /**
     * 通知数据
     */
    private Resource resource;

    /**
     * 回调摘要
     */
    private String summary;


    @Data
    @Accessors(chain = true)
    public static class Resource {

        /**
         * 加密算法类型
         */
        private String algorithm;

        /**
         * 附加数据
         */
        private String associated_data;

        /**
         * 原始类型
         */
        private String original_type;

        /**
         * 随机串
         */
        private String nonce;

        /**
         * 数据密文
         */
        private String ciphertext;
    }
}
