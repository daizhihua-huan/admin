package com.daizhihua.oauth.entity.dto;

import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto implements Serializable {

    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String code;
    @NotNull
    private String uuid;

    private String key;
}
