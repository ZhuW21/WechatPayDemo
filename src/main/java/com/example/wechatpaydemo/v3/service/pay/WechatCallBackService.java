package com.example.wechatpaydemo.v3.service.pay;

import com.example.wechatpaydemo.v3.req.WechatV3UnifiedRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信支付账单业务层
 *
 * @author qingzhou
 * @date 2023-03-13 16:59
 **/
@Service
public interface WechatCallBackService {

    /**
     * 支付结果回调
     *
     * @param request 请求
     * @return String 结果
     */
    String payCallBack(HttpServletRequest request);
}
