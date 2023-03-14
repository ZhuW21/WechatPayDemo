package com.example.wechatpaydemo.v3.controller.bill;

import com.example.wechatpaydemo.v3.resp.TradeBillDataResp;
import com.example.wechatpaydemo.v3.service.bill.WechatBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信支付账单控制层
 *
 * @author qingzhou
 * @date 2023-03-13 16:55
 **/
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WechatBillController {

    private final WechatBillService wechatBillService;


    @GetMapping("/downloadTradeBill")
    TradeBillDataResp downloadTradeBill(@RequestParam String date) {
        return wechatBillService.downloadTradeBill(date);
    }

}
