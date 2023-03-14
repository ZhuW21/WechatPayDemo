package com.example.wechatpaydemo.v3.common;

import lombok.Data;

import java.util.List;

/**
 * 平台证书信息
 *
 * @author qingzhou
 * @date 2023-03-13 16:59
 */
@Data
public class CertificateData {
    private List<CertificateParams> data;

    /**
     * 解析后的证书序列号
     */
    private String SerialNumber;

    /**
     * 解析后的平台证书
     */
    private String publicCer;

    @Data
    public static class CertificateParams {
        private String effective_time;
        private String expire_time;
        private String serial_no;

        private EncryptCertificate encrypt_certificate;
    }

    @Data
    public static class EncryptCertificate {
        private String algorithm;
        private String associated_data;
        private String nonce;
        private String ciphertext;
    }
}
