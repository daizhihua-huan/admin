package com.daizhihua.manager.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class RoleVo {

    private Long id;

    private List<Long> menus;

}
