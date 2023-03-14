package com.example.wechatpaydemo.v3.resp;

import com.example.wechatpaydemo.v3.anno.SplitIndex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 账单解析汇总
 *
 * @author qingzhou
 * @date 2023-03-14 9:56
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeBillTotalResp {

    /**
     * 总交易单数
     */
    @SplitIndex(0)
    private String totalOrderCount;

    /**
     * 应结订单总金额
     */
    @SplitIndex(1)
    private String totalNeedPayOrderAmount;

    /**
     * 退款总金额
     */
    @SplitIndex(2)
    private String totalFundAmount;

    /**
     * 充值券退款总金额
     */
    @SplitIndex(3)
    private String totalTopUpFundAmount;

    /**
     * 手续费总金额
     */
    @SplitIndex(4)
    private String totalHandlingFee;

    /**
     * 订单总金额
     */
    @SplitIndex(5)
    private String totalOrderAmount;

    /**
     * 申请退款总金额
     */
    @SplitIndex(6)
    private String totalRequestFundAmount;

}
