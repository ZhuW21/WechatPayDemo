package com.example.wechatpaydemo.v3.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付回调通知金额参数
 *
 * @author qingzhou
 * @date 2023-03-17 10:12
 */

@Data
@Accessors(chain = true)
public class WechatCallBackNoticeAmountResp {

    /**
     * 总金额
     */
    private Integer total;

    /**
     * 用户支付金额
     */
    private Integer payer_total;

    /**
     * 货币类型
     */
    private String currency;

    /**
     * 用户支付币种
     */
    private String payer_currency;
}
