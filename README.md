<<<<<<< HEAD
# aliyun-sms
阿里云发送短信验证码SpringBoot
=======
### 项目介绍
简单验证码系统，主要实现了以下需求：验证码默认为6位随机纯数字;验证码默认有效期为2分钟;每个手机号码60秒内只能发送1次验证码;保存于服务端的验证码最多可以被使用3次，随后作废，无论正确与否，以防止暴力破解。

![image-20211231154821172](https://cdn.jsdelivr.net/gh/ID-Wangqiang/MyBlogImg/img/202112311548868.png)

### 技术工具
项目后端采用SpringBoot框架，Redis 用作短信验证码的缓存，前端采用Thymeleaf模板简单展示功能，涉及到的其他技术工具有阿里云短信服务，Fastjson。

### 开发环境
- IDEA  
- JDK8   
- Redis
- Maven   

### 如何运行
1. git clone git@github.com:ID-Wangqiang/aliyun-sms.git   

2. 用IDEA打开项目

   - 如需引用阿里云的短信服务，在配置文件application.properties中填写AccessKey、AccessSecret、阿里云短板模版Code （可选）

   ![image-20211231155618264](https://cdn.jsdelivr.net/gh/ID-Wangqiang/MyBlogImg/img/202112311556304.png)

   并在代码中取消注释

   ![image-20211231155749530](https://cdn.jsdelivr.net/gh/ID-Wangqiang/MyBlogImg/img/202112311557571.png)

   

3. 运行AliyunSmsApplication.java文件，浏览器访问localhost:8080即可展示前端页面

### 参考资料
[阿里云短信服务](https://help.aliyun.com/product/44282.html)  
[Java SDK](https://help.aliyun.com/document_detail/112148.html)  
[短信服务代码实例](https://api.aliyun.com/?spm=a2c4g.11186623.2.15.39d060e2bjChk2#/?product=Dysmsapi&version=2017-05-25&api=SendSms&tab=DEMO&lang=JAVA)   
[狂神说](https://www.bilibili.com/video/BV1c64y1M7qN)  
>>>>>>> c4d6028 (first commit)
