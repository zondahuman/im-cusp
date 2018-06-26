package com.abin.lee.im.iplist.controller;

import com.abin.lee.im.iplist.service.IpListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by abin on 2018/6/26.
 */
@Controller
@RequestMapping("/iplist")
@Slf4j
public class IpListController {


    @Autowired
    IpListService ipListService;


    @RequestMapping(value = "/roundRobin")
    @ResponseBody
    public String roundRobin(String listName) {
        String result = "";
        try {
            result = this.ipListService.roundRobin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }






}
