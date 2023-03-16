package com.example.wechatpaydemo.v3.controller.pay;

import com.example.wechatpaydemo.v3.req.WechatV3UnifiedRequest;
import com.example.wechatpaydemo.v3.service.pay.WechatPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信支付控制层
 *
 * @author qingzhou
 * @date 2023-03-16 16:13
 **/
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WechatPayController {

    private final WechatPayService wechatPayService;

    /**
     * JSAPI下单
     * @param request 请求参数
     * @return 预支付标识
     */
    @PostMapping("/jsApiUnifiedOrder")
    public String jsApiUnifiedOrder(@RequestBody WechatV3UnifiedRequest request){
        return wechatPayService.jsApiUnifiedOrder(request);
    }

}
