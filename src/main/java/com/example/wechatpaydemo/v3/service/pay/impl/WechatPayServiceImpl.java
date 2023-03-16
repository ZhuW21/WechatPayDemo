package com.example.wechatpaydemo.v3.service.pay.impl;

import com.alibaba.fastjson.JSON;
import com.example.wechatpaydemo.v3.common.WechatConfig;
import com.example.wechatpaydemo.v3.common.WechatMerchant;
import com.example.wechatpaydemo.v3.common.WechatPayV3;
import com.example.wechatpaydemo.v3.req.WechatV3UnifiedRequest;
import com.example.wechatpaydemo.v3.service.pay.WechatPayService;
import com.ijpay.core.enums.RequestMethod;
import com.ijpay.wxpay.enums.WxApiType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 微信支付业务层
 *
 * @author qingzhou
 * @date 2023-03-13 16:59
 **/
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WechatPayServiceImpl implements WechatPayService {

    private WechatConfig wechatConfig;

    private final WechatMerchant wechatMerchant;

    @PostConstruct
    public void init() {
        wechatConfig = WechatConfig.builder()
                .mchId(wechatMerchant.getMchId())
                .appId(wechatMerchant.getAppId())
                .appSecret(wechatMerchant.getAppSecret())
                .apiKey(wechatMerchant.getApiKey())
                .apiCertPath(wechatMerchant.getApiCertPath())
                .notifyDomain(wechatMerchant.getNotifyDomain())
                .isV3(wechatMerchant.getIsV3())
                .v3ApiKey(wechatMerchant.getV3ApiKey())
                .serialNo(wechatMerchant.getSerialNo())
                .privateCer(wechatMerchant.getPrivateCer())
                .build();
    }

    @Override
    public String jsApiUnifiedOrder(WechatV3UnifiedRequest request) {
        // 此处省略一些业务逻辑（幂等、参数校验...）
        WechatPayV3 wechatPayV3 = new WechatPayV3(wechatConfig);
        return wechatPayV3.wechatPay(
                JSON.toJSONString(request),
                RequestMethod.POST,
                WxApiType.JS_API_PAY.getType()
        );
    }
}
