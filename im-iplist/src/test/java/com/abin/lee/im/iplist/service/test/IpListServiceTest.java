package com.abin.lee.im.iplist.service.test;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

/**
 * Created by abin on 2018/6/26.
 */
public class IpListServiceTest {
    private volatile Integer index = 0;
    private static final Integer SET_MAX = 100000000;

    public List<String> createList() {
        List<String> list = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            list.add("192.168.1.10" + i + ":1001" + i);
        }
        return list;
    }

    public String getDynamicIp() {
        String result = "";
        List<String> total = createList() ;
        result = total.get(index % total.size());
        this.setIndex(index);
        return result ;
    }

    public void setIndex(int index){
        index++;
        if(index > SET_MAX)
            index = 0;
    }

    @Test
    public void testDynamicIp1(){
        for (int i = 0; i <10 ; i++) {
            String result = getDynamicIp() ;
            System.out.println("result = " + result);
        }
    }



}
