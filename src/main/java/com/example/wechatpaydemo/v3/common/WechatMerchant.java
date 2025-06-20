package com.example.wechatpaydemo.v3.common;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author qingzhou
 * @date 2023-03-13 17:32
 **/
@Data
@Component
public class WechatMerchant {

    /**
     * 微信支付商户号
     */
    //private String mchId = "1629542722";
    private String mchId = "1498823642";

    /**
     * AppID（从微信公众平台获取）
     */
    //private String appId = "wx1d2fdf44838a2507";
    private String appId = "";

    /**
     * AppSecret（即 AppID 对应的密码，同样从微信公众平台获取）
     */
    //private String appSecret = "c93ff5a6f4d5c973cb3c203683a6f315";
    private String appSecret = "";
    /**
     * API 密钥（V2）
     */
    private String apiKey = "API 密钥（V2）";

    /**
     * 微信回调地址的服务器域名，如：http://xxx.com，SDK 在发送请求时会自动补全对应回调的 url
     */
    private String notifyDomain = "微信回调地址";

    /**
     * API 证书绝对路径（在调用微信支付安全级别较高的接口，如：退款、企业红包、企业付款时，会使用到 API 证书）
     */
    private String apiCertPath = "/";

    /**
     * 是否沙箱环境
     */
    private boolean isSandbox = false;

    /**
     * 是否启用V3版本接口
     */
    private Boolean isV3 = true;

    /**
     * V3接口API秘钥
     */
    //private String v3ApiKey = "fujian_bosssoft_300525_2022_0815";
    private String v3ApiKey ="bosssoftbszx2023bosssoftbszx2023";

    /**
     * 商户证书序列号
     */
    //private String serialNo = "52E9A7EB49CA54926BA77E3DAEF4955FB7B4E92A";
    private String serialNo = "5C6B8314A13A71FD8A837DFDCFCEC66C52464F67";

    /**
     * 商户API私钥
     */
//    private String privateCer = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDm3zhl7gKy8Zkt\n" +
//            "Cz13b4vPmJXyj3EK6K+iIM5up6SV5m3GowAYJEAkgTrwF8Joi/aUAlQS5myy3S0m\n" +
//            "F2uzIov9as7NbKVjcVCPfsT4qRtLFaH37j+PHd6oTPePrnNXYh8gwL3hfg6wwUHX\n" +
//            "B1X+/jPLgg70qkxVcCQPamNSfZrIQrSZs1PQIsyVqYceAreaij0pHI13yhaL0mC7\n" +
//            "Mw52NXesh0xMhlkR13fy4QtPxO6cizH3fmdambhCtd+9GC7sifNHTZLkVXbXrYGI\n" +
//            "lhu86XABqU8xLDQe8l2pT993da+vPa7SbFvr5Yjp2pvNLvLnEr/13MP/Vx9lcqeh\n" +
//            "NJVtw8AXAgMBAAECggEBANrl5pludAyqWauFrr/heVRZvkJ/kpTfTjeQl2n/dWPT\n" +
//            "WV4vClXZeR+jj/g47tElc6OeGYzaAVINt1MYfbsZFFYwwOexDZcdx15muedynzgv\n" +
//            "yKREturBTW2RnGAU7bN6iz0IwoBcfx1D/2Mbx4PADef05q1AEqQncWlUW9vIz8ry\n" +
//            "9tZjKnK6ADmCMwxRFYsGoXmfauhSIZhI0qiNKXyozxQHpyyRbr8B0C9tohPvawSg\n" +
//            "Y1s/ZTo3oz79Iy8AZLWC5wFUZCld5iinm6dJCaTCnzBHNI39rf0jDtw8lgUhgjS9\n" +
//            "K/OwQBNQRvsYOAmP8qqseqGzytndilvQz2+tKqIUvXECgYEA/VKWZJ5Ux1NikYQ1\n" +
//            "3zo2UbkpLYy9TRcgnUt2WXl3PJyql23pCmAgtVU//3uNw46+bRPbhoSs0W14zUbU\n" +
//            "8xOitZM7FxvrusXou2wwVDzVSNyr9Ru9j6WImqNuU8k6JgEVe+2/9xdcNFqQQJPb\n" +
//            "n78HIGRInDbsAufaahnNYjdziNMCgYEA6U/jaM4aLShci4/HtmMQDAhZVLJdVr2d\n" +
//            "Phywj7x41jqfkBQL7bPctzkrziG2UdaOErcD2YD+t9sP/et1Pep0m5xPstNBmdTB\n" +
//            "I43QhWbuE81Zrsy1GbbFIdLrre8uFceBRo7UxP1WjlCGBhVxCU0cd9o6BM6Bhb0/\n" +
//            "/viOiQP4oS0CgYEAyb1byAXPvm3Jsdl4ja3uVGcBRCUxHJOuhZXzSo5RRxP9R1tg\n" +
//            "m8KySbb6oJvk6jhjrrqBuT8v4hkse05NiMe6bKA0DtNNiBDUp24hjvRjZwNWbn0J\n" +
//            "GbzabUx6MH+wr1NtqKROnJkd9ROqjcMyxKV9J2615LJYgnfimVyzE+TVPcECgYAt\n" +
//            "ULfxrj5FURaQgM+gbCM8ww5etfoNE+0LmFTuxgOOUHqXaF1cwNd4uuD4O3IG+S5F\n" +
//            "EkjsIPLYUPMNNYNjEDuv0a9lI0UVlM4ikrPShUhuji8CFX6DTQ2C4U2nTdJcppBE\n" +
//            "XQ+UZ0Q9hgRSAC4hSVnjKX6lWlqr2DMq2aoNkLjkzQKBgQDS6bRW9ewjU4Tz3DDQ\n" +
//            "yryvgMsUlUMrBFXqT1xoRiv1JaORBucHr+gCR4Wr9agK85C6lmLHC4HYnO/3qpnO\n" +
//            "vNkdEjNF0u641cSWWkFfPL1l7LBMHuSnyk+gS8MG5akqSZuKE4wt6QiukERDDGOb\n" +
//            "eOupSchEw/3fRU1ws4D3gXkc4w==";
    private String privateCer = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC2nvWkaJ2hUZJm\n" +
            "Bf0lEaMuYHw76atEOrcjqiYg7TOjDAm86YeByhZK+HkuZtChKj8AkVU5HwyV9Qc3\n" +
            "iJ0KwjabKRTnN+PWviNokBl/xWUD8wjVBcPxH26gg0dTZEplTDByOOkdzfsoxXqN\n" +
            "PTSGIzZDKDYOZLQtHNacQ1PPk3MfnYQzHylTQCvHpPAIDSGwQCHj/sqHoBpGZVfx\n" +
            "Lk6xFk/xjg80zfBVe/wOl1akQ0TP+TsLul8Xomg5rxcBFb4nHCNHAsGJ87/A52fq\n" +
            "ctBwyOsxYwPbf2aqZEV5bjfNlZJXNzuOd25mxM4pxW2Pn8NWRFNHz/vNNQnguDAB\n" +
            "qEushz/lAgMBAAECggEARi0Pd/47lKIj/9CNVpFQg6HQG7XPeoiVl77qp/x+u0kE\n" +
            "9PNIfzeJGYYiCxK3WTdCvpUairjDvVxLSavVrSy0jytaPP/dLcgjEytMoSzZhIr/\n" +
            "+dwdT6skd8n6vrDrZcHBXCNCcZGV0jUQwvWiGWmukGADZcSM+Ej8r0+wBTREKQFX\n" +
            "xgz/fZdbAs63q7YSDZmmyl8uQ3ge48yPzibPU/1UKGYju80lo3lhlUvjQyXIRDCZ\n" +
            "tqY53YbKfyu7ijQUwHMX1sX4nSUXBPCmll01zQGH8M9sRjMntx8ldFf9nu4qGaw0\n" +
            "/gneKkrEtl52ch7LKpkZ+PfBw96sKJGvIIHiS6e4gQKBgQDtUXKYrvipE0cnjDYb\n" +
            "S7EMBS2/qv/Dj6RHvtnbD3q66elfVxsBGGmT0Ns9uKqAkqrP1NNhdqxQYgZhIvQ0\n" +
            "ikh9h/PyhjYMDoOhuoQ+vse+VNbLUES0gO+hjzTNsp39Rb/c0B8TbWRvKJV1fKHv\n" +
            "d7g0G8Zx3HJwTNpRlJ60o5dD5wKBgQDE/zoCDxDwD1j1wU+6kFqB0wurM2mqrdrj\n" +
            "CR0tv2fHZBUc0SqgBm8P8IFL1lx4/z/NYIodyz3mVp6uKcOKYTg9OrBRv3ROwZmn\n" +
            "if9cPp3AB5LBmD8bxn+8HP6uLrCUA0CSr2TBO/ggicZyNo6bf9iEekA9ivDz7vf5\n" +
            "4+tPVZpkUwKBgCcnwg2ZCOP0j0/iHWdBhNOj4xl1oD9m6sb/oTg4hJpkT6HwYD89\n" +
            "BOZjlljHqDIQgOhLiBjTmLimg8DxIHOuwSkdDk3cgPxaRLOhb4JevdwUaWAgPUW+\n" +
            "5Eq2sxKhPtnz8SUAna/545FHk+lPh/RXv2exkXtl0OfgdkDxkYHiX+nPAoGAV63F\n" +
            "Mr9j6YTbMl58zuuK7Pdy3Rr/79B7eMtIqmDjntJDt1S9ZQYYDn8b/RYGiHALBKzw\n" +
            "LDyWa1uoOpoqBwAoWNAzQ98ztA3lBrWb64GPErYMMpBBnmol1iMNk5cCTo54AhGA\n" +
            "haTcVhccNR9lL36ibqw+xWV+wwNcaWNxCxltozMCgYEAmesrBFObsKQsjTqzZugo\n" +
            "Tb/3dVXxVIGkGrzq92QFnrSdg7lCjia2aE5bM3c8j+rJ2yM4BbVIM5yvoy8OnA36\n" +
            "zJtQyWiTcX9PU+KXmktbd59x+Vr/E+Vb6h7jdbeY/g5J9aq2dPfK8OHlVgFHzHts\n" +
            "g/gto3elLwxOFcgblH4ZpYE=";
}
