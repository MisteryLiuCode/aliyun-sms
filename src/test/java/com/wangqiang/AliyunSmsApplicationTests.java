package com.wangqiang;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
class AliyunSmsApplicationTests {

    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.accessSecret}")
    private String accessKeySecret;

    @Value("${aliyun.templateCode}")
    private String templateCode;

    @Test
    void contextLoads() {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //官方参数不要改动
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //自定义参数（手机号，验证码，签名，模版）
        request.putQueryParameter("PhoneNumbers", "18888888888");
        request.putQueryParameter("SignName", "锵锵");
        request.putQueryParameter("TemplateCode", templateCode);


        //构建一个短信验证码
        HashMap<String, Object> map = new HashMap<>();
        map.put("code",2233);
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map));

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    void randomNum(){
        String trandNo = String.valueOf((Math.random() * 9 + 1) * 1000000).toString().substring(0, 6);
        System.out.println(trandNo);
    }

}
