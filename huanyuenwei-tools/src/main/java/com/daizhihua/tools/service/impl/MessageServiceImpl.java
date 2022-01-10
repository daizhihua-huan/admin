package com.daizhihua.tools.service.impl;

import com.daizhihua.tools.entity.Message;
import com.daizhihua.tools.mapper.MessageMapper;
import com.daizhihua.tools.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 代志华
 * @since 2022-01-05
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

}
