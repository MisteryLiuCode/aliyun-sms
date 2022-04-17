package com.wangqiang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    //显示register页面
    @RequestMapping("/index")
    public String register(){
        return "index.html";
    }

}

