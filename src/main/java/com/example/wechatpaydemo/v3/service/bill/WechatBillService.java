package com.example.wechatpaydemo.v3.service.bill;

import com.example.wechatpaydemo.v3.resp.TradeBillDataResp;
import org.springframework.stereotype.Service;

/**
 * 微信支付账单业务层
 *
 * @author qingzhou
 * @date 2023-03-13 16:59
 **/
@Service
public interface WechatBillService {


    /**
     * 下载交易账单
     *
     * @param date 账单时间
     * @return TradeBillDataResp 交易账单数据
     */
    TradeBillDataResp downloadTradeBill(String date);

}
