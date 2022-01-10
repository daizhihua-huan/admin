package com.daizhihua.tools.service.impl;

import com.daizhihua.tools.entity.Test;
import com.daizhihua.tools.mapper.TestMapper;
import com.daizhihua.tools.service.TestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author daizhihua
 * @since 2021-12-09
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {

}
