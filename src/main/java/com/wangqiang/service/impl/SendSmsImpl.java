package com.wangqiang.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.wangqiang.service.SendSms;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class SendSmsImpl implements SendSms {

    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.accessSecret}")
    private String accessKeySecret;

    @Value("${aliyun.templateCode}")
    private String templateCode;

    @Override
    public boolean send(String phoneNum, Map<String, Object> code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //官方参数不要改动
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //自定义参数（手机号，验证码，签名，模版）
        request.putQueryParameter("PhoneNumbers", phoneNum);
        request.putQueryParameter("SignName", "misteryliu");
        request.putQueryParameter("TemplateCode", templateCode);



        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(code));

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess(); // 判断获取验证码是否成功
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
