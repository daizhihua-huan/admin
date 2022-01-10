package com.daizhihua.tools.service.impl;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import com.daizhihua.core.exception.BadRequestException;
import com.daizhihua.tools.entity.vo.EmailVo;
import com.daizhihua.tools.entity.ToolEmailConfig;
import com.daizhihua.tools.mapper.ToolEmailConfigMapper;
import com.daizhihua.tools.service.ToolEmailConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 邮箱配置 服务实现类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-15
 */
@Service
public class ToolEmailConfigServiceImpl extends ServiceImpl<ToolEmailConfigMapper, ToolEmailConfig> implements ToolEmailConfigService {

    @Override
    public void send(EmailVo emailVo) {
        ToolEmailConfig emailConfig = this.getOne(null);
        // 封装
        MailAccount account = new MailAccount();
        account.setUser(emailConfig.getFromUser());
        account.setHost(emailConfig.getHost());
        account.setPort(Integer.parseInt(emailConfig.getPort()));
        account.setAuth(true);
        account.setPass(emailConfig.getPass());
        account.setFrom(emailConfig.getUser()+"<"+emailConfig.getFromUser()+">");
        // ssl方式发送
        account.setSslEnable(true);
        // 使用STARTTLS安全连接
        account.setStarttlsEnable(true);
        String content = emailVo.getContent();
        // 发送
        try {
            int size = emailVo.getTos().size();
            Mail.create(account)
                    .setTos(emailVo.getTos().toArray(new String[size]))
                    .setTitle(emailVo.getSubject())
                    .setContent(content)
                    .setHtml(true)
                    //关闭session
                    .setUseGlobalSession(false)
                    .send();
        }catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }
    }
}
