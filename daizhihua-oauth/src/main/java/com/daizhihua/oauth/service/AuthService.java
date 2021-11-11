package com.daizhihua.oauth.service;



import java.util.Map;

public interface AuthService {

     Map<String,Object>getImage();

     Map<String,Object>auth(String username,String password,String code,String key, String uuid);
}
