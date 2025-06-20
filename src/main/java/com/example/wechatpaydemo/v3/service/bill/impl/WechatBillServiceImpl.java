package com.example.wechatpaydemo.v3.service.bill.impl;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpResponse;
import com.example.wechatpaydemo.v3.anno.SplitIndex;
import com.example.wechatpaydemo.v3.common.WechatConfig;
import com.example.wechatpaydemo.v3.common.WechatMerchant;
import com.example.wechatpaydemo.v3.common.WechatPayV3;
import com.example.wechatpaydemo.v3.resp.TradeBillDataResp;
import com.example.wechatpaydemo.v3.resp.TradeBillLineResp;
import com.example.wechatpaydemo.v3.resp.TradeBillTotalResp;
import com.example.wechatpaydemo.v3.resp.WechatTradeBillDownloadResp;
import com.example.wechatpaydemo.v3.service.bill.WechatBillService;
import com.example.wechatpaydemo.v3.util.IOUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 微信支付账单业务层
 *
 * @author qingzhou
 * @date 2023-03-13 16:59
 **/
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WechatBillServiceImpl implements WechatBillService {

    private WechatConfig wechatConfig;

    private final WechatMerchant wechatMerchant;

    /**
     * 备份交易文件路径
     */
    private final String WECHAT_TEMP_PATH = "D:\\wechat_temp\\";

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
                .privateCer(wechatMerchant.getPrivateCer().replaceAll("\\s+", ""))
                .build();
    }


    @Override
    public TradeBillDataResp downloadTradeBill(String date) {

        WechatPayV3 wechatPayV3 = new WechatPayV3(wechatConfig);
        // 申请交易账单
        WechatTradeBillDownloadResp wechatTradeBillDownloadResp = wechatPayV3.tradeBill(date);

        // 解析交易账单下载地址，获取token
        String downloadUrl = wechatTradeBillDownloadResp.getDownloadUrl();
        UrlBuilder urlBuilder = UrlBuilder.ofHttp(downloadUrl, CharsetUtil.CHARSET_UTF_8);
        String token = urlBuilder.getQuery().get("token").toString();

        try (
                // 获取交易账单数据流
                HttpResponse httpResponse = wechatPayV3.billDownload(token);
        ) {

            if (httpResponse.getStatus() != 200) {
                return null;
            }

            // 异步备份交易账单文件(建议使用线程池，此处简略操作)
            new Thread(() -> {
                File folder = new File(WECHAT_TEMP_PATH);
                if (!folder.exists() && !folder.isDirectory()) {
                    boolean mkdirs = folder.mkdirs();
                    Assert.isTrue(mkdirs, "创建微信交易账单文件夹失败");
                }

                // 文件名
                String targetPath = WECHAT_TEMP_PATH.concat(wechatConfig.getMchId())
                        .concat("All")
                        .concat(date)
                        .concat(".csv");
                IOUtil.backUpFile(httpResponse.bodyStream(), targetPath);
            }).start();

            // 解析数据流
            return parseBody(httpResponse.body());
            //or 输出文件
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析交易账单文件响应数据
     *
     * @param body 响应数据
     * @return 交易账单文件数据
     */
    public TradeBillDataResp parseBody(String body) {
        TradeBillDataResp tradeBillDataResp = null;
        List<TradeBillLineResp> tradeBillLineRespList = new ArrayList<>();
        try {
            body = body.replace("`", "");
            String[] billDataStrArray = body.split("\r\n");

            if (billDataStrArray.length > 0) {

                /* 解析行数据 */
                for (int i = 1; i < billDataStrArray.length - 2; i++) {
                    // 填充空格，防止数组越界
                    String billData = billDataStrArray[i] + " ";
                    String[] data = billData.split(",");
                    TradeBillLineResp tradeBillLineResp = TradeBillLineResp.class.getDeclaredConstructor().newInstance();
                    for (Field field : TradeBillLineResp.class.getDeclaredFields()) {
                        SplitIndex splitIndex = field.getAnnotation(SplitIndex.class);
                        if (splitIndex != null) {
                            int index = splitIndex.value();
                            String value = null;
                            if(index <= data.length -1) {
                                value = data[index];
                            } 
                            setFieldValue(tradeBillLineResp, field, value);
                        }
                    }
                    tradeBillLineRespList.add(tradeBillLineResp);
                }

                /* 解析汇总数据*/
                // 填充空格，防止数组越界
                String totalData = billDataStrArray[billDataStrArray.length - 1] + " ";
                String[] totalDataArray = totalData.split(",");
                TradeBillTotalResp tradeBillTotalResp = TradeBillTotalResp.class.getDeclaredConstructor().newInstance();
                for (Field field : TradeBillTotalResp.class.getDeclaredFields()) {
                    SplitIndex splitIndex = field.getAnnotation(SplitIndex.class);
                    if (splitIndex != null) {
                        int index = splitIndex.value();
                        String value = null;
                        if(index <= totalDataArray.length -1) {
                            value = totalDataArray[index];
                        }
                        setFieldValue(tradeBillTotalResp, field, value);
                    }
                }

                tradeBillDataResp = TradeBillDataResp.builder()
                        .tradeBillTotalResp(tradeBillTotalResp)
                        .tradeBillLineRespList(tradeBillLineRespList)
                        .build();
            }
            return tradeBillDataResp;
        } catch (Exception e) {
            throw new RuntimeException("解析交易账单文件响应数据异常：" + e);
        }
    }

    /**
     * 使用反射设置对象的属性值。
     *
     * @param obj   要设置属性值的对象
     * @param field 属性
     * @param value 属性值
     * @throws IllegalAccessException 如果没有足够的权限访问属性
     */
    public static void setFieldValue(Object obj, Field field, String value)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException {

        Class<?> aClass = obj.getClass();
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), aClass);
        Method method = propertyDescriptor.getWriteMethod();
        String type = field.getGenericType()
                .toString();
        if (!StringUtils.hasText(value)) {
            return;
        }
        value = value.trim();
        switch (type) {
            case "class java.lang.String":
                method.invoke(obj, value);
                break;
            case "class java.lang.Integer":
                method.invoke(obj, Double.valueOf(value)
                        .intValue());
                break;
            case "class java.lang.Long":
                method.invoke(obj, Double.valueOf(value)
                        .longValue());
                break;
            case "class java.lang.Double":
                method.invoke(obj, Double.valueOf(value));
                break;
            case "class java.lang.Float":
                method.invoke(obj, Double.valueOf(value)
                        .floatValue());
                break;
            case "class java.lang.Character":
                method.invoke(obj, value.toCharArray()[0]);
                break;
            case "class java.math.BigDecimal":
                method.invoke(obj, new BigDecimal(value).setScale(4, RoundingMode.HALF_UP));
                break;
            default:
                method.invoke(obj, (Object) null);
                break;
        }
    }
    
}
