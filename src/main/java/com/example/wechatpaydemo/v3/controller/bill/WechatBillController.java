package com.example.wechatpaydemo.v3.controller.bill;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayDataDataserviceBillDownloadurlQueryModel;
import com.example.wechatpaydemo.v3.anno.SplitIndex;
import com.example.wechatpaydemo.v3.resp.AliTradeBillLineData;
import com.example.wechatpaydemo.v3.resp.TradeBillDataResp;
import com.example.wechatpaydemo.v3.resp.TradeBillLineResp;
import com.example.wechatpaydemo.v3.resp.TradeBillTotalResp;
import com.example.wechatpaydemo.v3.service.bill.WechatBillService;
import com.example.wechatpaydemo.v3.service.bill.impl.WechatBillServiceImpl;
import com.example.wechatpaydemo.v3.service.pay.impl.WechatPayServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.unionpay.UnionPayApi;
import com.ijpay.unionpay.enums.ServiceEnum;
import com.ijpay.unionpay.model.BillDownloadModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 微信支付账单控制层
 *
 * @author qingzhou
 * @date 2023-03-13 16:55
 **/
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WechatBillController {

    private final WechatBillServiceImpl wechatBillService;

    private final String WECHAT_TEMP_PATH = "D:\\alipay_temp\\";


    @GetMapping("/downloadTradeBill")
    TradeBillDataResp downloadTradeBill(@RequestParam String date) {
        return wechatBillService.downloadTradeBill(date);
    }


    @GetMapping("/ysfDownloadTradeBill")
    TradeBillDataResp ysfDownloadTradeBill(@RequestParam String date) {
        try {

            Map<String, String> params = BillDownloadModel.builder()
                    .service(ServiceEnum.BILL_MERCHANT.toString())
                    .bill_date(date)
                    .bill_type("ALL")
                    .mch_id("QRA29045311KKR1")
                    .nonce_str(WxPayKit.generateStr())
                    .build()
                    .createSign("979da4cfccbae7923641daa5dd7047c2", SignType.MD5);

            System.out.println(params);

            String result = UnionPayApi.execution("https://up.95516.com/payapi/gateway", params);
            TradeBillDataResp tradeBillDataResp = null;
            // 快速判断是否是 JSON 错误信息
            if (result.trim().startsWith("{") && result.contains("\"status\"")) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> errorMap = mapper.readValue(result, Map.class);
                if ("400".equals(String.valueOf(errorMap.get("status")))) {
                    throw new IllegalStateException((String) errorMap.get("message"));
                }
            } else {
                // 正常账单解析（CSV 数据）
                System.out.println("这是账单数据，准备解析...");
                tradeBillDataResp =  wechatBillService.parseBody(result);
            }
            /*TradeBillDataResp tradeBillDataResp =  wechatBillService.parseBody(result);
            return tradeBillDataResp;*/
            return tradeBillDataResp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/aliDownloadTradeBill")
    public List<AliTradeBillLineData> downloadBillData(String billType, String billDate) throws Exception {
        //log.info("支付宝账单下载, billDate:{}, billType:{}, smid:{}, aliConfig:{}", billDate, billType, aliConfig);
        /*if (aliConfig == null || StringUtils.isAnyEmpty(billDate, aliConfig.getAppId(),
                aliConfig.getAlipayPublicKey(), aliConfig.getPrivateKey())) {
            throw new BusinessException(DataGateWayCommonFailureCause.CONFIG_EXCEPTION, "支付宝账单下载配置存在异常");
        }*/
        try {
            com.alipay.api.AlipayConfig alipayConfig = new com.alipay.api.AlipayConfig();
            alipayConfig.setAppId("2016122204513266");
            String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCjX9bGpt0b+R2aRs91cuQWCuPXjN9Nuk2n6OPvxADBpDvcGDkIr43Lnv3KH+hNaYEB1oGvujLpNTpuBRGJclWkUvbr1UUMvRstKsXEAkkIxhrzLt+OAoUuFFgMLKqEumIUN5k+bxE+QgGBG+pgVdeJxtbkK9LfvpWlgljV/Ccg+pUFSpfRBWkbJp81+nrE2S1WXyuO+nO3Wx/ZQCH5K/t5gihskqnFv7E96H7V3peoAe9mIELzGBzie6krzlLrP3er0MiAfH9+8y14qN74d2/q9yjYJfWkuWgbvpmIFlOAQ/xSl83+1SmorQeZPRZZcdVYNemEeeHiTjwzLoKwzTbHAgMBAAECggEBAIHSDUvrmnW4/dHzBLVyZ1Ywrx5AXc1ZBo4vnLiQyn0VMU/Sm7eQMJ+mJkswEZs6A/5e4sytq/juiBCWzHmote8O0hFSdj0KJAc/HsmDe1+SdCXbkF87F3MnTVPXRmST6o3gJFCdV1lef+2FLjgA3nYn3+iN/lI3EpjjPA5GAR3NWqNLMoQSX3hAZ5w1jKc/7VHtsY+/XSBQBaoW2MjVf/bY4jGjrSUvrhjJPxaSSqRNkDWFW6BG8A5ZncOmyxxc+R7+5OqkeiUi6WniPYbSd5rSmwwNgbV+N/SZrfmVTycmzan9/d73/HWrsyQtG2ooR3hKzUFtEoxyc2cgS07SyFECgYEA4GrjdbUp8WVjSBKGLkHMfCiIwbzS6LJ1S7m+vThwoKwv3wWlH/hrFEhonrfC3g4JJmDmFfKs8ne50x4JOtgzJ+IJ2nfCEbz2qV7UD1fYIDfbWWezvjUfMtmQFkF/cZQo1xtG6X3ka21Y6zJqsnIMJWYi8PE2qDSIGw/IRrtY8DkCgYEAul2+I42AUjbSsW8LMeORg6AER79SiYf/F0wBciAaEsJicJkapEyEj3e97FvdFPpxfruDF1HhZNcBML02REbIErbnvkJ96VF36iJbY4nfPTrE1LDk+1/uYQja/tfFaq/2Ip8dYC4wRdNtbuJp4eT9b1RdOK+bAH2jcb6kSMEJXv8CgYAdoV9I8lOIYNeb834t61ghaISt981tFerwxaSOfT45qKJlcuxbbUROGEVf92lOQlIo9BBNA46cMPyjCEs1rsgKUwj0BG3mQ6nZsQCUygdqMZXPlyafTYpB/4HLvngiOequhBZ3y7x2Bqj/3NdDmlVKNtMUNvT9fnoeAJZz6FaU2QKBgQCgauir+bUnD4G0sXzVaKlsv2RvzcPiFmoBR69G6VMIKsJJYcCJBERmqg+VUE2xLRJcoFNq01RMVLX1mikIBFVbpo08PEfdef7aTtCTka3TKq8a1VmE90JdzB2Vl22bM/OOQaArlhZ/RErCU6oIg++S0O+dR6sIdR0nX45SAbdrSQKBgD+ktJxjEA/0gCzib/MG/ooJQvNetZ9J3SIW09BKNNxPk/7KKwKtqM979PDMyMAs1Bw/lExv30QhFA3S1s//Qw1yYjweaNdjA2hheXLnN19uNxCnF26WJZrxkzzl8Ovs1R5yfV61KRT05R0uRmNcAe94C9nNYZBz4JJw9YrQHX9G";
            alipayConfig.setPrivateKey(privateKey.replaceAll("\\s+", ""));
            alipayConfig.setFormat("json");
            String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk1ac2mKWr5wPLUvV1p2HXdrVOnqEmEgZzknAsw4FQL3ONN072hTh4+lJcxofhJoDgVl6q+hnkQiX+l2/EauHaLJqTLY15jgZKJKX/y3I3yVzVudVk7HuZ4JJ4oToSXymAfhInmyYRvGc5aa59EyivIOlCV325U3pj/c3v3tfV55SUcOj+nxEh/vmdrObBHzWJX0XiovYPAubm6kIPilNcniV5DE47AymFwtYciQPsk2WEyRUIUHbttl6RkXZSQkgZhuYY17LjVqUWrMO5aGGpgKnFipoBdWORNhPGDfxRfAiExDmHD0qoGCKdrLkex0W916jJmEdbcOVGJhnb7vK7QIDAQAB";
            alipayConfig.setAlipayPublicKey(publicKey.replaceAll("\\s+", ""));
            alipayConfig.setCharset("UTF-8");
            alipayConfig.setSignType("RSA2");
            AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);

            AlipayDataDataserviceBillDownloadurlQueryModel model = new AlipayDataDataserviceBillDownloadurlQueryModel();
            /*if(StringUtils.isBlank(billType)) {
                billType = "trade";
            }*/
            billType = "trade";
            model.setBillType(billType);
            model.setBillDate(billDate);
            //model.setSmid(smid);
            String downloadUrl = AliPayApi.billDownloadUrlQuery(alipayClient, false, model);
            if(downloadUrl == null || "".equals(downloadUrl)) {
                //throw new BusinessException(DataGateWayCommonFailureCause.ALP_BILL_DOWNLOAD_EXCEPTION, "支付宝账单下载地址获取为空");
                return null;
            }
            
            List<AliTradeBillLineData> resultList = aliPayParseData(downloadUrl, AliTradeBillLineData.class);
            return resultList;
        } catch (Exception e) {
            //log.error("支付宝账单数据获取异常", e);
            //throw new BusinessException(DataGateWayCommonFailureCause.ALP_BILL_DOWNLOAD_EXCEPTION, "支付宝账单数据获取异常");
            return null;
        }
    }

    /**
     * 支付宝账单数据解析
     * @param downloadUrl
     * @param classz
     * @return
     * @throws IOException
     */
    public static <T> List<T> aliPayParseData(String downloadUrl, Class<T> classz){
        List<T> aliPayBillLineRespList = new ArrayList<>();
        HttpURLConnection conn = null;
        ZipInputStream zis = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(downloadUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            conn.connect();

            // 不解压直接读取,加上GBK解决乱码问题
            zis = new ZipInputStream(conn.getInputStream(), Charset.forName("GBK"));
            reader = new BufferedReader(new InputStreamReader(zis, "GBK"));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.getName().endsWith(".csv")) continue;
                //暂时不用汇总的
                if(entry.getName().contains("汇总")) continue;
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
                                WechatPayServiceImpl.setFieldValue(aliPayBillLineResp, field, value);
                            }
                        }
                        aliPayBillLineRespList.add(aliPayBillLineResp);
                    }
                }
            }
        } catch (Exception e) {
            //log.error("支付宝下载账单数据处理异常", e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (zis != null) zis.close();
                if (zis != null) zis.close();
                if (conn != null) conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return aliPayBillLineRespList;
    }

    /**
     * 支付宝账单数据解析
     * @param downloadUrl
     * @param classz
     * @return
     * @throws IOException
     */
    public static <T> List<T> aliPayParseData1(String downloadUrl, Class<T> classz) throws Exception {
        List<T> aliPayBillLineRespList = new ArrayList<>();

        try (InputStream is = new URL(downloadUrl).openStream();
             ZipInputStream zis = new ZipInputStream(is, Charset.forName("GBK"))) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null ) {
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
                                WechatPayServiceImpl.setFieldValue(aliPayBillLineResp, field, value);
                            }
                        }
                        aliPayBillLineRespList.add(aliPayBillLineResp);
                    }
                }
            }
        } catch (Exception e) {
            //log.error("支付宝下载账单数据处理异常", e);
            throw new RuntimeException(e);
        }
        return aliPayBillLineRespList;
    }
}
