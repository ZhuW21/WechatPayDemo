package com.example.wechatpaydemo.v3.service.pay;

import com.example.wechatpaydemo.v3.req.WechatV3UnifiedRequest;
import org.springframework.stereotype.Service;

/**
 * 微信支付账单业务层
 *
 * @author qingzhou
 * @date 2023-03-13 16:59
 **/
@Service
public interface WechatPayService {

    /**
     * JSAPI下单
     *
     * @param request 下单参数
     * @return TradeBillDataResp 交易账单数据
     */
    String jsApiUnifiedOrder(WechatV3UnifiedRequest request);
}
