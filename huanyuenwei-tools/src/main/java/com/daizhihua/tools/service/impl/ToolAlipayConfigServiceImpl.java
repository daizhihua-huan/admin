package com.daizhihua.tools.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.daizhihua.core.exception.BadRequestException;
import com.daizhihua.tools.entity.ToolAlipayConfig;
import com.daizhihua.tools.entity.vo.TradeVo;
import com.daizhihua.tools.mapper.ToolAlipayConfigMapper;
import com.daizhihua.tools.service.ToolAlipayConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 支付宝配置类 服务实现类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-16
 */
@Service
@Slf4j
public class ToolAlipayConfigServiceImpl extends ServiceImpl<ToolAlipayConfigMapper, ToolAlipayConfig> implements ToolAlipayConfigService {




    @Override
    public String toPayAsPc(ToolAlipayConfig alipay, TradeVo trade) throws Exception {
        if(alipay==null){
            throw new BadRequestException("请先添加相应配置，再操作");
        }
        AlipayClient alipayClient = new DefaultAlipayClient(
                alipay.getGatewayUrl(), alipay.getAppId(), alipay.getPrivateKey(),
                alipay.getFormat(), alipay.getCharset(), alipay.getPublicKey(), alipay.getSignType());

        // 创建API对应的request(电脑网页版)
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        // 订单完成后返回的页面和异步通知地址
        request.setReturnUrl(alipay.getReturnUrl());
        request.setNotifyUrl(alipay.getNotifyUrl());
        // 填充订单参数
        request.setBizContent("{" +
                "    \"out_trade_no\":\""+trade.getOutTradeNo()+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":"+trade.getTotalAmount()+"," +
                "    \"subject\":\""+trade.getSubject()+"\"," +
                "    \"body\":\""+trade.getBody()+"\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\""+alipay.getSysServiceProviderId()+"\"" +
                "    }"+
                "  }");//填充业务参数
        // 调用SDK生成表单, 通过GET方式，口可以获取url
        return alipayClient.pageExecute(request, "GET").getBody();
    }

    @Override
    public String toPayAsWeb(ToolAlipayConfig alipay, TradeVo trade) throws Exception {
        if(alipay == null){
            throw new BadRequestException("请先添加相应配置，再操作");
        }
        AlipayClient alipayClient = new DefaultAlipayClient(alipay.getGatewayUrl(), alipay.getAppId(), alipay.getPrivateKey(), alipay.getFormat(), alipay.getCharset(), alipay.getPublicKey(), alipay.getSignType());

        double money = Double.parseDouble(trade.getTotalAmount());
        double maxMoney = 5000;
        if(money <= 0 || money >= maxMoney){
            throw new BadRequestException("测试金额过大");
        }
        // 创建API对应的request(手机网页版)
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setReturnUrl(alipay.getReturnUrl());
        request.setNotifyUrl(alipay.getNotifyUrl());
        request.setBizContent("{" +
                "    \"out_trade_no\":\""+trade.getOutTradeNo()+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":"+trade.getTotalAmount()+"," +
                "    \"subject\":\""+trade.getSubject()+"\"," +
                "    \"body\":\""+trade.getBody()+"\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\""+alipay.getSysServiceProviderId()+"\"" +
                "    }"+
                "  }");
        return alipayClient.pageExecute(request, "GET").getBody();
    }

    @Override
    public ToolAlipayConfig getAlipayConfig() {
        ToolAlipayConfig toolAlipayConfig;
        try{
            toolAlipayConfig = this.getOne(null);
        }catch (Exception e){
            throw new BadRequestException("请先配置用户");
        }


        return toolAlipayConfig;
    }
}
