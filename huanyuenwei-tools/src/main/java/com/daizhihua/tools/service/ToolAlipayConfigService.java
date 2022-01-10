package com.daizhihua.tools.service;

import com.daizhihua.tools.entity.ToolAlipayConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.daizhihua.tools.entity.vo.TradeVo;

/**
 * <p>
 * 支付宝配置类 服务类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-16
 */
public interface ToolAlipayConfigService extends IService<ToolAlipayConfig> {

    /**
     * 处理来自PC的交易请求
     * @param alipay 支付宝配置
     * @param trade 交易详情
     * @return String
     * @throws Exception 异常
     */
    String toPayAsPc(ToolAlipayConfig alipay, TradeVo trade) throws Exception;

    /**
     * 处理来自手机网页的交易请求
     * @param alipay 支付宝配置
     * @param trade 交易详情
     * @return String
     * @throws Exception 异常
     */
    String toPayAsWeb(ToolAlipayConfig alipay, TradeVo trade) throws Exception;

    ToolAlipayConfig getAlipayConfig();

}
