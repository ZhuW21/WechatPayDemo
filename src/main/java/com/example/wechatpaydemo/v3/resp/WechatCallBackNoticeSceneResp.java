package com.example.wechatpaydemo.v3.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付回调 - 支付场景信息
 *
 * @author yewei
 * @date 2023-03-17 10:25
 **/
@Data
@Accessors(chain = true)
public class WechatCallBackNoticeSceneResp {
    /**
     * 商户端设备号
     */
    private String device_id;
}
