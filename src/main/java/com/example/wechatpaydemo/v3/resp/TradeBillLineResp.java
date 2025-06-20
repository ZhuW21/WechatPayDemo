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
     * 列定义都是账单是ALL的情况
     * 交易时间
     */
    @SplitIndex(0)
    //private String tradeDate;
    private String column1;

    /**
     * 公众账号ID-ALL
     */
    @SplitIndex(1)
    //private String appId;
    private String column2;

    /**
     * 商户号
     */
    @SplitIndex(2)
    //private String mchId;
    private String column3;

    /**
     * 特约商户号
     */
    @SplitIndex(3)
    //private String specMchId;
    private String column4;

    /**
     * 设备号
     */
    @SplitIndex(4)
    //private String deviceId;
    private String column5;

    /**
     * 微信订单号
     */
    @SplitIndex(5)
    //private String wechatOrderNo;
    private String column6;

    /**
     * 商户订单号
     */
    @SplitIndex(6)
    //private String mchOrderNo;
    private String column7;

    /**
     * 用户标识
     */
    @SplitIndex(7)
    //private String userSign;
    private String column8;

    /**
     * 交易类型
     */
    @SplitIndex(8)
    //private String tradeType;
    private String column9;

    /**
     * 交易状态
     */
    @SplitIndex(9)
    //private String tradeStatus;
    private String column10;

    /**
     * 付款银行
     */
    @SplitIndex(10)
    //private String payBank;
    private String column11;

    /**
     * 货币种类
     */
    @SplitIndex(11)
    //private String payCurrency;
    private String column12;

    /**
     * 应结订单金额
     */
    @SplitIndex(12)
    //private String needPayAmount;
    private String column13;

    /**
     * 代金券金额
     */
    @SplitIndex(13)
    //private String voucherAmount;
    private String column14;

    /**
     * 微信退款单号
     */
    @SplitIndex(14)
    //private String refundWechatNo;
    private String column15;

    /**
     * 商户退款单号
     */
    @SplitIndex(15)
    //private String refundMchNo;
    private String column16;

    /**
     * 退款金额
     */
    @SplitIndex(16)
    //private String refundAmount;
    private String column17;

    /**
     * 充值券退款金额
     */
    @SplitIndex(17)
    //private String topUpRefundAmount;
    private String column18;

    /**
     * 退款类型
     */
    @SplitIndex(18)
    //private String refundType;
    private String column19;

    /**
     * 退款状态
     */
    @SplitIndex(19)
    //private String refundStatus;
    private String column20;

    /**
     * 商品名称
     */
    @SplitIndex(20)
    //private String productName;
    private String column21;

    /**
     * 商户数据包
     */
    @SplitIndex(21)
    //private String mchData;
    private String column22;

    /**
     * 手续费
     */
    @SplitIndex(22)
    //private String handlingFee;
    private String column23;

    /**
     * 费率
     */
    @SplitIndex(23)
    //private String feeRate;
    private String column24;

    /**
     * 订单金额
     */
    @SplitIndex(24)
    //private String orderAmount;
    private String column25;

    /**
     * 申请退款金额
     */
    @SplitIndex(25)
    //private String requestRefundAmount;
    private String column26;

    /**
     * 费率备注
     */
    @SplitIndex(26)
    //private String feeRateRemark;
    private String column27;


    @SplitIndex(27)
    //private String feeRateRemark;
    private String column28;


    @SplitIndex(28)
    //private String feeRateRemark;
    private String column29;


    @SplitIndex(29)
    private String column30;

    @SplitIndex(30)
    private String column31;

    @SplitIndex(31)
    private String column32;

    @SplitIndex(32)
    private String column33;

    @SplitIndex(33)
    private String column34;

    @SplitIndex(34)
    private String column35;
}
