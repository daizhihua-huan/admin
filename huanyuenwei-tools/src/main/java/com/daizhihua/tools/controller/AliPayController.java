package com.daizhihua.tools.controller;

import com.daizhihua.core.annotion.AnonymousAccess;
import com.daizhihua.core.exception.BadRequestException;
import com.daizhihua.core.res.Resut;
import com.daizhihua.log.annotation.Log;
import com.daizhihua.tools.entity.ToolAlipayConfig;
import com.daizhihua.tools.entity.vo.TradeVo;
import com.daizhihua.tools.service.ToolAlipayConfigService;
import com.daizhihua.tools.util.AliPayStatusEnum;
import com.daizhihua.tools.util.AlipayUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Api(tags = "支付宝")
@Slf4j
@RestController
@RequestMapping(value = "aliPay")
public class AliPayController {

    @Autowired
    private ToolAlipayConfigService toolAlipayConfigService;


    @Autowired
    private AlipayUtils alipay;

    @ApiOperation(value = "获取支付宝配置参数")
    @GetMapping
    public Resut get(){
        return Resut.ok(toolAlipayConfigService.getOne(null,false));
    }

    @ApiOperation(value = "修改支付宝参数")
    @PutMapping
    public Resut update(@RequestBody ToolAlipayConfig toolAlipayConfig){
        if(toolAlipayConfig.getAppId()==null){
            toolAlipayConfigService.save(toolAlipayConfig);
        }else{
            toolAlipayConfigService.updateById(toolAlipayConfig);
        }
        return Resut.ok();
    }

    @ApiIgnore
    @RequestMapping("/return")
    @ApiOperation("支付之后跳转的链接")
    @AnonymousAccess
    public Resut returnPage(HttpServletRequest request, HttpServletResponse response){
        try{
            ToolAlipayConfig toolAlipayConfig = toolAlipayConfigService.getOne(null);
            response.setContentType("text/html;charset=" + toolAlipayConfig.getCharset());
            //内容验签，防止黑客篡改参数
            if (alipay.rsaCheck(request, toolAlipayConfig)) {
                //商户订单号
                String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                //支付宝交易号
                String tradeNo = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                System.out.println("商户订单号" + outTradeNo + "  " + "第三方交易号" + tradeNo);

                // 根据业务需要返回数据，这里统一返回OK
                return Resut.ok(HttpStatus.OK);
            } else {
                // 根据业务需要返回数据
                return Resut.error(HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            throw  new BadRequestException("请先配置参数");
        }
    }

    @Log(value = "pc支付测试")
    @ApiOperation(value = "pc支付测试",notes = "pc支付测试")
    @PostMapping(value = "toPayAsPC")
    public Resut toPayAsPC(@RequestBody TradeVo trade) throws Exception {
        ToolAlipayConfig alipayConfig = toolAlipayConfigService.getAlipayConfig();
        trade.setOutTradeNo(alipay.getOrderCode());
        String payUrl = toolAlipayConfigService.toPayAsPc(alipayConfig, trade);
        return Resut.ok(payUrl);
    }

    @Log(value = "pc手机支付测试")
    @ApiOperation(value = "pc手机测试",notes = "pc测试")
    @PostMapping(value = "toPayAsWeb")
    public Resut toPayAsWeb(@Validated @RequestBody TradeVo trade) throws Exception {
        ToolAlipayConfig alipayConfig = toolAlipayConfigService.getAlipayConfig();
        trade.setOutTradeNo(alipay.getOrderCode());
        String payUrl = toolAlipayConfigService.toPayAsWeb(alipayConfig, trade);
        return Resut.ok(payUrl);
    }

    @ApiIgnore
    @RequestMapping("/notify")
    @AnonymousAccess
    @ApiOperation("支付异步通知(要公网访问)，接收异步通知，检查通知内容app_id、out_trade_no、total_amount是否与请求中的一致，根据trade_status进行后续业务处理")
    public Resut notifyPage(HttpServletRequest request){
        ToolAlipayConfig toolAlipayConfig = toolAlipayConfigService.getOne(null);
        Map<String, String[]> parameterMap = request.getParameterMap();
        //内容验签，防止黑客篡改参数
        if (alipay.rsaCheck(request, toolAlipayConfig)) {
            //交易状态
            String tradeStatus = new String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 商户订单号
            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //付款金额
            String totalAmount = new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //验证
            if (tradeStatus.equals(AliPayStatusEnum.SUCCESS.getValue()) || tradeStatus.equals(AliPayStatusEnum.FINISHED.getValue())) {
                // 验证通过后应该根据业务需要处理订单
            }
            return Resut.ok(HttpStatus.OK);
        }
        return Resut.error(HttpStatus.BAD_REQUEST);
    }


}
