package com.daizhihua.manager.service;

import com.daizhihua.core.entity.SysRole;
import com.daizhihua.core.entity.SysUser;

import java.util.List;

public interface DataScopeService {

    List<Long> getDataScope(SysUser sysUser, SysRole sysRole);
}
