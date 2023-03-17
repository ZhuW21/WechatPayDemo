package com.example.wechatpaydemo.v3.service.pay.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.wechatpaydemo.v3.common.WechatConfig;
import com.example.wechatpaydemo.v3.common.WechatMerchant;
import com.example.wechatpaydemo.v3.common.WechatPayV3;
import com.example.wechatpaydemo.v3.req.WechatV3UnifiedRequest;
import com.example.wechatpaydemo.v3.resp.WechatPayCallBackResouceResp;
import com.example.wechatpaydemo.v3.resp.WechatPayCallBackResp;
import com.example.wechatpaydemo.v3.service.pay.WechatCallBackService;
import com.example.wechatpaydemo.v3.service.pay.WechatPayService;
import com.ijpay.core.enums.RequestMethod;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.wxpay.enums.WxApiType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * 微信支付业务层
 *
 * @author qingzhou
 * @date 2023-03-13 16:59
 **/
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WechatPayCallBackServiceImpl implements WechatCallBackService {

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
    public String payCallBack(HttpServletRequest request) {

        String noticeStr = HttpKit.readData(request);

        WechatPayCallBackResp noticeParam = JSON.parseObject(noticeStr, WechatPayCallBackResp.class);

        // 秘钥解密订单信息
        String noticeResource = WechatPayV3.decryptNoticeByApiV3Key(
                noticeParam.getResource().getAssociated_data(),
                noticeParam.getResource().getNonce(),
                noticeParam.getResource().getCiphertext()
        );

        // 订单信息
        WechatPayCallBackResouceResp resource = JSON.parseObject(noticeResource, WechatPayCallBackResouceResp.class);

        // 业务处理。。。。

        return "SUCCESS";
    }
}
