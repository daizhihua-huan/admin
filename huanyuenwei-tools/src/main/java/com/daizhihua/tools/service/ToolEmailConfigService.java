package com.daizhihua.tools.service;

import com.daizhihua.tools.entity.vo.EmailVo;
import com.daizhihua.tools.entity.ToolEmailConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 邮箱配置 服务类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-15
 */
public interface ToolEmailConfigService extends IService<ToolEmailConfig> {

    void send(EmailVo emailVo);
}
