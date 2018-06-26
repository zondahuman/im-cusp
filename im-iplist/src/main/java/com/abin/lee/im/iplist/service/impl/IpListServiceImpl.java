package com.abin.lee.im.iplist.service.impl;

import com.abin.lee.im.iplist.service.IpListService;
import com.abin.lee.im.iplist.util.GateWayUtil;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abin on 2018/6/26.
 */
@Service
public class IpListServiceImpl implements IpListService {
    private int index = 0;
    private static final Integer SET_MAX = Integer.MAX_VALUE;

    @Override
    public String roundRobin() {
        String result = getDynamicIp() ;
        return result;
    }

    public String getDynamicIp() {
        String result = "";
        result = GateWayUtil.list.get(index % GateWayUtil.list.size());
        this.setIndex(index);
        return result ;
    }

    public void setIndex(int index){
        index++;
        if(index >= SET_MAX)
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
