package com.example.wechatpaydemo.v3.resp;

import com.ijpay.wxpay.model.v3.Payer;
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
public class WechatPayCallBackResouceResp {
    /**
     * 应用ID
     */
    private String appid;

    /**
     * 商户号
     */
    private String mchid;

    /**
     * 商户订单号
     */
    private String out_trade_no;

    /**
     * 微信支付订单号
     */
    private String transaction_id;

    /**
     * 交易类型
     */
    private String trade_type;

    /**
     * 交易状态
     */
    private String trade_state;

    /**
     * 交易状态描述
     */
    private String trade_state_desc;

    /**
     * 付款银行
     */
    private String bank_type;

    /**
     * 附加数据
     */
    private String attach;

    /**
     * 支付完成时间
     */
    private String success_time;

    /**
     * 支付者信息
     */
    private Payer payer;

    /**
     * 订单金额信息
     */
    private WechatCallBackNoticeAmountResp amount;

    /**
     * 支付场景信息描述
     */
    private WechatCallBackNoticeSceneResp scene_info;
}
