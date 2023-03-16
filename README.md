# WechatPayDemo
微信支付接口Demo

`众所周知，腾讯的文档是给内部人员看的，官方Demo甚至不够完全，以致新手入门困难，这里将依赖第三方封装，对部分流程进行实现`

[第三方封装官网]：https://javen205.gitee.io/ijpay/

[微信官方文档]：https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_8.shtml

## 一、Maven引入
```xml 
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-All</artifactId>
    <version>2.7.4</version>
</dependency>

<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>2.0.25</version>
</dependency>

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.24</version>
</dependency>
```
## 目前Demo功能：
    1.下载交易账单

