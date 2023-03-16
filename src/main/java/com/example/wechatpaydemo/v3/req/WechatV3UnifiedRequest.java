package com.example.wechatpaydemo.v3.req;

import com.ijpay.wxpay.model.v3.Amount;
import com.ijpay.wxpay.model.v3.GoodsDetail;
import com.ijpay.wxpay.model.v3.Payer;
import com.ijpay.wxpay.model.v3.SceneInfo;
import lombok.Builder;
import lombok.Data;

/**
 * JSAPI下单请求参数
 *
 * @author qingzhou
 * @date 2023-03-16 16:21
 **/
@Data
@Builder
public class WechatV3UnifiedRequest {

    /**
     * 商户订单号
     */
    private String out_trade_no;

    /**
     * 直连商户号
     */
    private String mchid;

    /**
     * 应用ID
     */
    private String appid;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 通知地址
     */
    private String notify_url;

    /**
     * 订单金额信息
     */
    private Amount amount;

    /**
     * 支付者信息
     */
    private Payer payer;

    /**
     * 支付场景描述
     */
    private SceneInfo sceneInfo;

    /**
     * 单品列表信息
     */
    private GoodsDetail goodsDetail;
}
