package com.abin.lee.im.cclient.test.customer.message;

import com.abin.lee.im.custom.common.util.JsonUtil;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

/**
 * Created by abin on 2018/6/14.
 */
public class MessageCarrier {


    public static String userChat(String userId, String toUserId){
        Map<String, String> request = Maps.newConcurrentMap();
        request.put("userId", userId);
        request.put("msgType", "CHAT");
        request.put("toUserId", toUserId);
        request.put("chatMsg", "we go fishing .");
        return JsonUtil.toJson(request).toString();
    }


    public static String userLogin(String userId){
        Map<String, String> request = Maps.newConcurrentMap();
        request.put("userId", userId);
        request.put("msgType", "LOGIN");
        return JsonUtil.toJson(request).toString();
    }




}
