package com.daizhihua.oauth.util;

import com.daizhihua.core.entity.SysUser;
import com.daizhihua.oauth.entity.OnlineUserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheUtil {


    private static List<OnlineUserDto> userList = new ArrayList<>();

    private static Map<Long,OnlineUserDto> map = new HashMap<>();

    public static void sertUser(OnlineUserDto onlineUserDto){
        if(!userList.contains(onlineUserDto)){
            userList.add(onlineUserDto);
        }
    }

    public static List<OnlineUserDto> getUser(){
        return userList;
    }

    public static void removeUser(OnlineUserDto onlineUserDto){
        userList.remove(onlineUserDto);
    }


    public static OnlineUserDto getForUserId(Long id){
        return map.get(id);
    }

    /**
     * 保存在线用户
     * @param id
     * @param onlineUserDto
     */
    public static void setOnlineUserDto(Long id,OnlineUserDto onlineUserDto){
        map.put(id,onlineUserDto);
    }

    public static void removeForUserId(Long id){
        map.remove(id);
    }

}
