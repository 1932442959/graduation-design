package com.scu.lcw.backsystem.controller;

import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/home")
public class HomeController {

    @RequestMapping("/test")
    public String test() {
        return "success!";
    }
}
