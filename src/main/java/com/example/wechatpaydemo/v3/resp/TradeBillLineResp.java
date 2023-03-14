package com.example.wechatpaydemo.v3.resp;

import com.example.wechatpaydemo.v3.anno.SplitIndex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 交易数据
 * @author qingzhou
 * @date 2023-03-13 18:02
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeBillLineResp {

    /**
     * 交易时间
     */
    @SplitIndex(0)
    private String tradeDate;

    /**
     * 公众账号ID
     */
    @SplitIndex(1)
    private String appId;

    /**
     * 商户号
     */
    @SplitIndex(2)
    private String mchId;

    /**
     * 特约商户号
     */
    @SplitIndex(3)
    private String specMchId;

    /**
     * 设备号
     */
    @SplitIndex(4)
    private String deviceId;

    /**
     * 微信订单号
     */
    @SplitIndex(5)
    private String wechatOrderNo;

    /**
     * 商户订单号
     */
    @SplitIndex(6)
    private String mchOrderNo;

    /**
     * 用户标识
     */
    @SplitIndex(7)
    private String userSign;

    /**
     * 交易类型
     */
    @SplitIndex(8)
    private String tradeType;

    /**
     * 交易状态
     */
    @SplitIndex(9)
    private String tradeStatus;

    /**
     * 付款银行
     */
    @SplitIndex(10)
    private String payBank;

    /**
     * 货币种类
     */
    @SplitIndex(11)
    private String payCurrency;

    /**
     * 应结订单金额
     */
    @SplitIndex(12)
    private String needPayAmount;

    /**
     * 代金券金额
     */
    @SplitIndex(13)
    private String voucherAmount;

    /**
     * 微信退款单号
     */
    @SplitIndex(14)
    private String refundWechatNo;

    /**
     * 商户退款单号
     */
    @SplitIndex(15)
    private String refundMchNo;

    /**
     * 退款金额
     */
    @SplitIndex(16)
    private String refundAmount;

    /**
     * 充值券退款金额
     */
    @SplitIndex(17)
    private String topUpRefundAmount;

    /**
     * 退款类型
     */
    @SplitIndex(18)
    private String refundType;

    /**
     * 退款状态
     */
    @SplitIndex(19)
    private String refundStatus;

    /**
     * 商品名称
     */
    @SplitIndex(20)
    private String productName;

    /**
     * 商户数据包
     */
    @SplitIndex(21)
    private String mchData;

    /**
     * 手续费
     */
    @SplitIndex(22)
    private String handlingFee;

    /**
     * 费率
     */
    @SplitIndex(23)
    private String feeRate;

    /**
     * 订单金额
     */
    @SplitIndex(24)
    private String orderAmount;

    /**
     * 申请退款金额
     */
    @SplitIndex(25)
    private String requestRefundAmount;

    /**
     * 费率备注
     */
    @SplitIndex(26)
    private String feeRateRemark;
}
