package com.example.wechatpaydemo.v3.controller.callback;

import com.example.wechatpaydemo.v3.service.pay.WechatCallBackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信支付回调控制层
 *
 * @author qingzhou
 * @date 2023-03-17 9:34
 **/
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WechatCallBackController {

    private final WechatCallBackService wechatCallBackService;

    /**
     * 微信支付回调
     * 支付完成后，微信会把相关支付结果及用户信息通过数据流的形式发送给商户，商户需要接收处理，并按文档规范返回应答。
     * 注意：
     * 1、同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。
     * 2、后台通知交互时，如果微信收到商户的应答不符合规范或超时，微信会判定本次通知失败，重新发送通知，直到成功为止（在通知一直不成功的情况下，
     * 微信总共会发起多次通知，通知频率为15s/15s/30s/3m/10m/20m/30m/30m/30m/60m/3h/3h/3h/6h/6h - 总计 24h4m），但微信不保证通知最终一定能成功。
     * 3、在订单状态不明或者没有收到微信支付结果通知的情况下，建议商户主动调用微信支付【查询订单API】确认订单状态。
     */
    @GetMapping("/pay/callback")
    public String payCallBack(HttpServletRequest request){
      return wechatCallBackService.payCallBack(request);
    }
}
