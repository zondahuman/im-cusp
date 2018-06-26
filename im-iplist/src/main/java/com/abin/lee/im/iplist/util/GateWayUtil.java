package com.abin.lee.im.iplist.util;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by abin on 2018/6/26.
 */
@Component
public class GateWayUtil {
    public static List<String> list = Lists.newArrayList();


    @PostConstruct
    public void createList() {
        for (int i = 0; i < 5; i++) {
            list.add("192.168.1.10" + i + ":1001" + i);
        }
    }



}
