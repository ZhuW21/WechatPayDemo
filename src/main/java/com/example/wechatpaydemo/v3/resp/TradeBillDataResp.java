package com.example.wechatpaydemo.v3.resp;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author qingzhou
 * @date 2023-03-14 9:57
 **/
@Data
@Builder
public class TradeBillDataResp {

    /**
     * 账单数据（分条）
     */
    private List<TradeBillLineResp> tradeBillLineRespList;

    /**
     * 账单汇总数据
     */
    private TradeBillTotalResp tradeBillTotalResp;
}
