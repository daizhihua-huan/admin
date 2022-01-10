package com.daizhihua.oauth.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto implements Serializable {

    private String username;

    private String password;

    private String code;

    private String uuid;

    private String key;
}
