package com.example.wechatpaydemo.v3.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.wechatpaydemo.v3.resp.WechatTradeBillDownloadResp;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.RequestMethod;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.AesUtil;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.enums.WxApiType;
import com.ijpay.wxpay.enums.WxDomain;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 微信支付调用核心层
 *
 * @author qingzhou
 * @date 2023-03-13 17:06
 **/
@Slf4j
public class WechatPayV3 {

    private static final Integer SUCCESS_CODE = 200;

    private static final int TAG_LENGTH_BIT = 128;

    private static final String SIGN_TYPE = "RSA";

    private static final String V3_AES_Key = "填入APIv3密钥";

    public static final String WECHATPAY_SIGNATURE = "Wechatpay-Signature";

    public static final String WECHATPAY_NONCE = "Wechatpay-Nonce";

    public static final String WECHATPAY_TIMESTAMP = "Wechatpay-Timestamp";

    public static final String WECHATPAY_SERIAL = "Wechatpay-Serial";

    private final WechatConfig wechatConfig;

    public WechatPayV3(WechatConfig wechatConfig) {
        this.wechatConfig = wechatConfig;
    }

    /**
     * 申请交易账单
     *
     * @param billDate 账单日期（仅支持三个月内的账单下载申请）
     * @return 交易账单返参
     */
    public WechatTradeBillDownloadResp tradeBill(String billDate) {
        try {
            if (CharSequenceUtil.isEmpty(billDate)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DATE, -1);
                billDate = DateUtil.format(calendar.getTime(), "YYYY-MM-dd");
            }
            Map<String, String> params = new HashMap<>(12);
            params.put("bill_date", billDate);
            params.put("bill_type", "ALL");
            // 返回格式为.gzip的压缩包账单，不设置默认为数据流
            //params.put("tar_type", "GZIP");

            IJPayHttpResponse result = WxPayApi.v3(
                    RequestMethod.GET,
                    WxDomain.CHINA.getType(),
                    WxApiType.TRADE_BILL.toString(),
                    wechatConfig.getMchId(),
                    wechatConfig.getSerialNo(),
                    null,
                    getPrivateKey(),
                    params
            );
            // 根据证书序列号查询对应的证书来验证签名结果
            checkedSignature(result);

            if (result.getStatus() != 200) {
                throw new RuntimeException("申请交易账单失败，" + result.getBody());
            }

            return JSON.parseObject(result.getBody(), WechatTradeBillDownloadResp.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 下载交易账单
     *
     * @param token token
     * @return 交易账单数据
     */
    public HttpResponse billDownload(String token) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String authType = "WECHATPAY2-SHA256-RSA2048";
        String nonceStr = WxPayKit.generateStr();

        String urlSuffix = WxApiType.BILL_DOWNLOAD.toString().concat("?").concat("token=" + token);

        // 拼接url
        String url = WxDomain.CHINA.getType() + urlSuffix;

        // 构建签名
        String authorization = WxPayKit.buildAuthorization(RequestMethod.GET, urlSuffix,
                wechatConfig.getMchId(), wechatConfig.getSerialNo(),
                getPrivateKey(), "", nonceStr, timestamp, authType);

        Map<String, String> headers = WxPayApi.getHeaders(authorization, wechatConfig.getSerialNo());

        return HttpRequest.get(url).addHeaders(headers).execute();
    }

    /**
     * 获取商户API私钥
     * 用于加签
     */
    private PrivateKey getPrivateKey() {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(wechatConfig.getPrivateCer());
            KeyFactory keyFactory = KeyFactory.getInstance(SIGN_TYPE);
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 验签
     *
     * @param response 返回对象
     */
    public void checkedSignature(IJPayHttpResponse response) {
        if (Objects.isNull(response)) {
            throw new RuntimeException("参数对象错误，不应该为空");
        }

        if (!verifySignature(
                response.getHeader(WECHATPAY_SIGNATURE),
                response.getBody(),
                response.getHeader(WECHATPAY_NONCE),
                response.getHeader(WECHATPAY_TIMESTAMP),
                response.getHeader(WECHATPAY_SERIAL)
        )) {
            throw new RuntimeException("验签失败");
        }
    }

    /**
     * 解密获取的平台公钥
     * 用于验签
     *
     * @param publicCer 平台证书 base64
     */
    private PublicKey getPublicKey(String publicCer) {
        try {
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                    Base64.getDecoder().decode(publicCer.getBytes(StandardCharsets.UTF_8))
            );

            KeyFactory publicKeyFactory = KeyFactory.getInstance(SIGN_TYPE);
            return publicKeyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 平台公钥验证签名
     *
     * @param signature    返回体或回调中的签名串
     * @param body         返回体body
     * @param nonce        返回体中的随机串
     * @param timeTamp     返回体中的时间戳
     * @param serialNumber 返回体中的证书序列号
     */
    private boolean verifySignature(
            String signature,
            String body,
            String nonce,
            String timeTamp,
            String serialNumber
    ) {
        CertificateData certificateData = getCertificate(serialNumber);
        if (!Objects.equals(serialNumber, certificateData.getSerialNumber())) {
            throw new RuntimeException("证书序列号不一致，拒绝验签");
        }

        try {
            return WxPayKit.verifySignature(
                    signature,
                    body,
                    nonce,
                    timeTamp,
                    getPublicKey(certificateData.getPublicCer())
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取新的平台证书
     *
     * @param serialNumber 返回体或回调中的平台证书序列号（Wechatpay-Serial）
     *                     兼容使用新旧平台证书。
     *                     旧证书过期前5天至过期当天，新证书开始逐步放量用于应答和回调的签名
     */
    private CertificateData getCertificate(String serialNumber) {
        IJPayHttpResponse response;
        try {
            response = WxPayApi.v3(
                    RequestMethod.GET,
                    WxDomain.CHINA.getType(),
                    WxApiType.GET_CERTIFICATES.getType(),
                    wechatConfig.getMchId(),
                    wechatConfig.getSerialNo(),
                    null,
                    getPrivateKey(),
                    ""
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        if (SUCCESS_CODE != response.getStatus()) {
            throw new RuntimeException(response.getBody());
        }

        // 序列化JSON
        CertificateData certificateParam = JSON.parseObject(response.getBody(), CertificateData.class);

        // 获取有效期限大于 4 天并与返回体或回调中的平台证书序列号相同的证书
        certificateParam.setData(
                certificateParam.getData().stream().filter(
                        certificate -> validateCerExpireTime(certificate.getExpire_time())
                                && certificate.getSerial_no().equals(serialNumber)
                ).collect(Collectors.toList())
        );

        certificateParam.getData().forEach(
                certificate -> {
                    try {
                        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                        SecretKeySpec key = new SecretKeySpec(wechatConfig.getV3ApiKey().getBytes(), "AES");
                        GCMParameterSpec spec = new GCMParameterSpec(
                                TAG_LENGTH_BIT,
                                certificate.getEncrypt_certificate().getNonce().getBytes()
                        );

                        cipher.init(Cipher.DECRYPT_MODE, key, spec);
                        cipher.updateAAD(certificate.getEncrypt_certificate().getAssociated_data().getBytes());
                        String publicKey = new String(
                                cipher.doFinal(Base64.getDecoder().decode(certificate.getEncrypt_certificate().getCiphertext())),
                                StandardCharsets.UTF_8
                        );

                        CertificateFactory ft = CertificateFactory.getInstance("X.509");
                        X509Certificate xc = (X509Certificate) ft.generateCertificate(
                                new ByteArrayInputStream(publicKey.getBytes(StandardCharsets.UTF_8))
                        );

                        certificateParam.setSerialNumber(certificate.getSerial_no());
                        certificateParam.setPublicCer(Base64.getEncoder().encodeToString(xc.getPublicKey().getEncoded()));
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
        );
        return certificateParam;
    }

    /**
     * 计算平台证书过期时间
     *
     * @param expireTime 有效期截止时间
     */
    private boolean validateCerExpireTime(String expireTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        LocalDateTime endTime = LocalDateTime.parse(expireTime, formatter);
        Duration duration = Duration.between(LocalDateTime.now(), endTime);
        return duration.toDays() > 4;
    }

    /**
     * 构建拉起支付数据串
     *
     * @param prepayId  预支付id
     * @param tradeType 微信交易类型
     */
    public Map<String, String> buildPaymentMessage(String prepayId, TradeType tradeType) {
        long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = WxPayKit.generateStr();
        String prepayMessage = buildPrepayId(prepayId, tradeType);

        ArrayList<String> paymentMessage = new ArrayList<>(5);

        paymentMessage.add(wechatConfig.getAppId());
        paymentMessage.add(String.valueOf(timestamp));
        paymentMessage.add(nonceStr);
        paymentMessage.add(prepayMessage);

        String signMessage = PayKit.buildSignMessage(paymentMessage);
        String paySign = null;

        try {
            paySign = PayKit.createSign(signMessage, getPrivateKey());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        Map<String, Object> metadata = buildPayment(timestamp, nonceStr, prepayMessage, paySign);
        HashMap<String, String> map = new HashMap<>(2);

        return Collections.singletonMap("payload", JSONObject.toJSONString(metadata));
    }

    /**
     * 根据微信支付方式返回预支付信息用于签名
     * JSAPI需要 [prepay_id=******] 格式
     * APP 只需要 prepay_id 的值
     *
     * @param prepayId  下单接口返回的预支付信息
     * @param tradeType 微信交易类型
     */
    private String buildPrepayId(String prepayId, TradeType tradeType) {
        JSONObject prepayJson = JSON.parseObject(prepayId);
        switch (tradeType) {
            case JSAPI:
                return "prepay_id=".concat(prepayJson.getString("prepay_id"));
            case APP:
                return prepayJson.getString("prepay_id");
            default:
                return "";
        }
    }

    /**
     * 构建并统一返回参数的格式 payload:{metadata:{.....}}
     *
     * @param timestamp     时间戳
     * @param nonceStr      随机串
     * @param prepayMessage 预支付信息
     * @param paySign       签名
     */
    private Map<String, Object> buildPayment(long timestamp, String nonceStr, String prepayMessage, String paySign) {
        Map<String, String> paymentMap = new HashMap<>(16);

        paymentMap.put("appId", wechatConfig.getAppId());
        paymentMap.put("timeStamp", String.valueOf(timestamp));
        paymentMap.put("nonceStr", nonceStr);
        paymentMap.put("package", prepayMessage);
        paymentMap.put("signType", SIGN_TYPE);
        paymentMap.put("paySign", paySign);

        return Collections.singletonMap("metadata", paymentMap);
    }

    /**
     * 解密支付回调
     *
     * @param associatedData     附加数据
     * @param nonce              随机串
     * @param noticeResourceText 数据密文
     */
    public static String decryptNoticeByApiV3Key(String associatedData, String nonce, String noticeResourceText) {
        AesUtil aesUtil = new AesUtil(V3_AES_Key.getBytes(StandardCharsets.UTF_8));
        try {
            return aesUtil.decryptToString(
                    associatedData.getBytes(StandardCharsets.UTF_8),
                    nonce.getBytes(StandardCharsets.UTF_8),
                    noticeResourceText
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
