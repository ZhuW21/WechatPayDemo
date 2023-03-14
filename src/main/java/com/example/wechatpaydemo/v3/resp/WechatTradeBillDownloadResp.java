package com.example.wechatpaydemo.v3.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 账单返参
 *
 * @author qingzhou
 * @date 2023-03-13 16:59
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WechatTradeBillDownloadResp {

    /**
     * 账单下载地址
     */
    @JSONField(name = "download_url")
    private String downloadUrl;

    /**
     * 哈希类型
     */
    @JSONField(name = "hash_type")
    private String hashType;

    /**
     * 哈希值
     */
    @JSONField(name = "hash_value")
    private String hashValue;
}
