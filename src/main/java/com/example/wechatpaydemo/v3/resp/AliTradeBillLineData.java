package com.example.wechatpaydemo.v3.resp;


import com.example.wechatpaydemo.v3.anno.SplitIndex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 支付宝-业务账单
 * @author: zhuwen
 * @date: 2025/06/16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AliTradeBillLineData {
    /** 支付宝交易号 */
    @SplitIndex(0)
    private String alipayTradeNo;

    /** 商户订单号 */
    @SplitIndex(1)
    private String merchantOrderNo;

    /** 业务类型 */
    @SplitIndex(2)
    private String busType;

    /** 商品名称 */
    @SplitIndex(3)
    private String productName;

    /** 创建时间 */
    @SplitIndex(4)
    private String createTime;

    /** 完成时间 */
    @SplitIndex(5)
    private String finishTime;

    /** 门店编号 */
    @SplitIndex(6)
    private String storeId;

    /** 门店名称 */
    @SplitIndex(7)
    private String storeName;

    /** 操作员 */
    @SplitIndex(8)
    private String operator;

    /** 终端号 */
    @SplitIndex(9)
    private String terminalId;

    /** 对方账户 */
    @SplitIndex(10)
    private String oppositeAccount;

    /** 订单金额（元） */
    @SplitIndex(11)
    private String orderAmount;

    /** 商家实收（元） */
    @SplitIndex(12)
    private String merchantReceived;

    /** 支付宝红包（元） */
    @SplitIndex(13)
    private String alipayRedPacket;

    /** 集分宝（元） */
    @SplitIndex(14)
    private String pointAmount;

    /** 支付宝优惠（元） */
    @SplitIndex(15)
    private String alipayDiscount;

    /** 商家优惠（元） */
    @SplitIndex(16)
    private String merchantDiscount;

    /** 券核销金额（元） */
    @SplitIndex(17)
    private String couponWriteOffAmount;

    /** 券名称 */
    @SplitIndex(18)
    private String couponName;

    /** 商家红包消费金额（元） */
    @SplitIndex(19)
    private String merchantRedPacketUsed;

    /** 卡消费金额（元） */
    @SplitIndex(20)
    private String cardConsumeAmount;

    /** 退款批次号 */
    @SplitIndex(21)
    private String refundBatchNo;

    /** 服务费（元） */
    @SplitIndex(22)
    private String serviceFee;

    /** 分润（元） */
    @SplitIndex(23)
    private String profitSharingAmount;

    /** 备注 */
    @SplitIndex(24)
    private String remark;

    /** 商户识别号 */
    @SplitIndex(25)
    private String merchantIdentifyNo;
}
