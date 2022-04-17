package com.wangqiang.controller;

import com.alibaba.fastjson.JSONObject;
import com.wangqiang.service.SendSms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @version : V1.0
 * @ClassName: SmsApiController
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/5/17 23:35
 */
@RestController
@CrossOrigin //处理跨域问题
public class SmsApiController {

    private static final Logger logger = LoggerFactory.getLogger(SmsApiController.class);

    @Value("${my.sms.randomCodeNumber}")
    private String randomCodeNumber;

    @Value("${my.sms.expiryTime}")
    private String expiryTime;

    @Value("${my.sms.sendOneExistTime}")
    private String sendOneExistTime;

    @Value("${my.sms.maxUseCount}")
    private String maxUseCount;

    @Autowired
    private SendSms sendSms;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @RequestMapping("/send")
    public String code(String phone){

        // 判断该手机是否60秒内发送过验证码
        String smsPhone = "EXIST" + phone;
        String existPhone = redisTemplate.opsForValue().get(smsPhone);
        JSONObject jsonObject = new JSONObject();


        if (!StringUtils.isEmpty(existPhone)) {
            String str = phone + ":" + "每个手机号60秒内只能发送1次验证:";
            jsonObject.put("success",false);
            jsonObject.put("message",str);
            return jsonObject.toJSONString();
        }

        //生成纯6位数字验证码并存储redis
        String code = String.valueOf((Math.random() * 9 + 1) * 1000000).toString().substring(0, Integer.valueOf(randomCodeNumber));

        // 阿里云短信发送服务
        HashMap<String, Object> map = new HashMap<>();
        map.put("code",code);
        logger.warn("手机号："+phone+" 验证码：" + code);
        // 引用阿里云短信服务
        boolean isSend = sendSms.send(phone, map);
        // 不引用阿里云短信服务
//        boolean isSend = true;
        if (isSend){
            // 验证码有效期分钟
            redisTemplate.opsForValue().set(phone,code, Long.valueOf(expiryTime),TimeUnit.MINUTES);
            // 每个手机号60秒只能发送1次验证码
            redisTemplate.opsForValue().set(smsPhone,smsPhone,Long.valueOf(sendOneExistTime),TimeUnit.SECONDS);
            // 每个验证码只能使用3次
            String phoneCount = "COUNT" + phone;
            redisTemplate.opsForValue().set(phoneCount,maxUseCount,2,TimeUnit.MINUTES);
            jsonObject.put("success",true);
            jsonObject.put("message","获取验证码成功");
            jsonObject.put("sms",code);
        }else {
            jsonObject.put("success",false);
            jsonObject.put("message","验证码存入缓存失败");
        }
        return jsonObject.toJSONString();

    }

    @RequestMapping("/validate")
    public String vaile(String phone,String code){
        String phoneCode = redisTemplate.opsForValue().get(phone);
        //判断redis缓存中是否有手机号验证码的记录
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success",false);
        String message = null;
        if (StringUtils.isEmpty(phoneCode)){
            message = "手机号：" + phone + ",验证码已过期，请重新发送验证码";
            logger.warn(message);
            jsonObject.put("message",message);
            return jsonObject.toJSONString();
        }
        // 判断验证码是否正确
        if (!code.equals(phoneCode)) {
            message ="手机号：" +phone+ ",输入验证码有误，请重新输入";
            logger.warn(message);
            jsonObject.put("message",message);
        }else {
            // 验证码可验证次数减少
            String phoneCount = "COUNT" + phone;
            String s = redisTemplate.opsForValue().get(phoneCount);
            int count = Integer.valueOf(s);
            if (count > 0) {
                count--;
                redisTemplate.opsForValue().set(phoneCount,String.valueOf(count),2,TimeUnit.MINUTES);
                message = "手机号：" +phone+ ",验证次数还有" +count+ "次";
                logger.warn(message);
                jsonObject.put("success",true);
                jsonObject.put("message",message);
            }else {
                message = "手机号：" +phone+ ",只能验证三次";
                jsonObject.put("message",message);
                logger.warn(message);
            }
        }
        return jsonObject.toJSONString();
    }
}
