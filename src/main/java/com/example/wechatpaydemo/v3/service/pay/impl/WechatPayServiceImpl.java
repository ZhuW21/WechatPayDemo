package com.example.wechatpaydemo.v3.service.pay.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;
import com.example.wechatpaydemo.v3.anno.SplitIndex;
import com.example.wechatpaydemo.v3.common.WechatConfig;
import com.example.wechatpaydemo.v3.common.WechatMerchant;
import com.example.wechatpaydemo.v3.common.WechatPayV3;
import com.example.wechatpaydemo.v3.req.WechatV3UnifiedRequest;
import com.example.wechatpaydemo.v3.resp.AliTradeBillLineData;
import com.example.wechatpaydemo.v3.resp.TradeBillLineResp;
import com.example.wechatpaydemo.v3.service.pay.WechatPayService;
import com.ijpay.core.enums.RequestMethod;
import com.ijpay.wxpay.enums.WxApiType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 微信支付业务层
 *
 * @author qingzhou
 * @date 2023-03-13 16:59
 **/
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WechatPayServiceImpl implements WechatPayService {

    private WechatConfig wechatConfig;

    private final WechatMerchant wechatMerchant;

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
                .privateCer(wechatMerchant.getPrivateCer())
                .build();
    }

    @Override
    public String jsApiUnifiedOrder(WechatV3UnifiedRequest request) {
        // 此处省略一些业务逻辑（幂等、参数校验...）
        WechatPayV3 wechatPayV3 = new WechatPayV3(wechatConfig);
        return wechatPayV3.wechatPay(
                JSON.toJSONString(request),
                RequestMethod.POST,
                WxApiType.JS_API_PAY.getType()
        );
    }

    /*public static void main(String[] args) throws IOException {
        String zipFilePath = "D:\\Saas\\BossSoftBillWallet\\SMID账务账单.zip";
        List<Map<String, String>> resultList = new ArrayList<>();
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath), StandardCharsets.UTF_8)) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".csv") && !entry.getName().contains("/")) {
                    if (!entry.getName().endsWith(".csv")) continue;
                    //暂时不用汇总的
                    if(entry.getName().contains("汇总")) continue;

                    BufferedReader reader = new BufferedReader(new InputStreamReader(zis, "GBK"));
                    String line;
                    List<String> headers = new ArrayList<>();
                    boolean headerCaptured = false;

                    while ((line = reader.readLine()) != null) {
                        line = line.trim();

                        if (line.isEmpty() || line.startsWith("#") || line.startsWith("注") || line.startsWith("本文件")) {
                            continue;
                        }
                        
                        if (line.contains("支付宝交易号") || line.contains("支付宝订单号") || line.contains("商户订单号")) {
                            headers = Arrays.asList(line.split(","));
                            headerCaptured = true;
                            continue;
                        }

                        if (line.contains("总交易") || line.contains("总金额") || line.contains("汇总")) {
                            continue;
                        }
                        
                        if (headerCaptured) {
                            line = line.replace("\t", "").replace("\"", "");
                            String[] values = line.split(",", -1);
                            Map<String, String> row = new LinkedHashMap<>();
                            for (int i = 0; i < values.length; i++) {
                                row.put("column" + (i + 1), values[i].trim());
                            }
                            resultList.add(row);
                        }
                    }
                }
                zis.closeEntry();
            }
        }
        List<TradeBillLineResp> finalList = resultList.stream().filter(Objects::nonNull)
                .map(e-> TypeUtils.castToJavaBean(JSON.toJSON(e), TradeBillLineResp.class)).collect(Collectors.toList());
        System.out.println(finalList);
    }*/

    public static void main(String[] args) throws IOException {
        //String url = "D:\\Saas\\BossSoftBillWallet\\业务账单.zip";
        String url = "D:\\alipay_temp\\支付宝交易账单201612220451326620250610.zip";
        List<AliTradeBillLineData> list = aliPayParseData(url, AliTradeBillLineData.class);
        System.out.println(list);
    }

    /**
     * 支付宝账单数据解析
     * @param downloadUrl
     * @param classz
     * @return
     * @throws IOException
     */
    private static <T> List<T> aliPayParseData(String downloadUrl, Class<T> classz) throws IOException {
        List<T> aliPayBillLineRespList = new ArrayList<>();
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(downloadUrl),  Charset.forName("GBK"))) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null && !entry.getName().contains("/")) {
                if (!entry.getName().endsWith(".csv")) continue;
                //暂时不用汇总的
                if(entry.getName().contains("汇总")) continue;

                BufferedReader reader = new BufferedReader(new InputStreamReader(zis, "GBK"));
                String line;
                List<String> headers = new ArrayList<>();
                boolean headerCaptured = false;

                while ((line = reader.readLine()) != null) {
                    line = line.trim();

                    if (line.isEmpty() || line.startsWith("#") || line.startsWith("注") || line.startsWith("本文件")) {
                        continue;
                    }

                    if (line.contains("支付宝交易号") || line.contains("支付宝订单号") || line.contains("商户订单号")) {
                        headers = Arrays.asList(line.split(","));
                        headerCaptured = true;
                        continue;
                    }

                    if (line.contains("总交易") || line.contains("总金额") || line.contains("汇总")) {
                        continue;
                    }

                    if (headerCaptured) {
                        line = line.replace("\t", "").replace("\"", "");
                        String[] values = line.split(",", -1);
                        T aliPayBillLineResp = classz.getDeclaredConstructor().newInstance();
                        for (Field field : classz.getDeclaredFields()) {
                            SplitIndex splitIndex = field.getAnnotation(SplitIndex.class);
                            if (splitIndex != null) {
                                int index = splitIndex.value();
                                String value = null;
                                if(index <= values.length -1) {
                                    value = values[index];
                                }
                                setFieldValue(aliPayBillLineResp, field, value);
                            }
                        }
                        aliPayBillLineRespList.add(aliPayBillLineResp);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return aliPayBillLineRespList;
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
        if (StringUtils.isEmpty(value)) {
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
