package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.dto.AccountTxPageDTO;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.service.TopAccountTxService;
import com.ruoyi.web.utils.RequestUtil;
import com.ruoyi.web.vo.AccountTxVO;
import com.ruoyi.web.vo.PageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@Tag(description = "资产流水", name = "资产流水")
@RestController
@RequestMapping("topAccountTx")
public class TopAccountTxController {

    @Autowired
    private TopAccountTxService service;

    @Operation(summary="查询流水记录")
    @GetMapping("/getPage")
    public AjaxResult<PageVO<AccountTxVO>> getPage(@ModelAttribute AccountTxPageDTO dto) {
        return AjaxResult.success(service.getPage(RequestUtil.getWallet(), dto));
    }

    @Operation(summary="查询指定日期的交易记录")
    @GetMapping("/testSumExchangeAmount")
    public AjaxResult<String> testSumExchangeAmount() {
        // 检查当日的兑换量，是否达到了100w
        LocalDate now = LocalDate.of(2024,4,16);
        BigDecimal btcfSumExchangeAmount = service.sumExchangeAmount("BTCF",now ,now.plusDays(1));
        if(btcfSumExchangeAmount.compareTo(new BigDecimal("33")) > 0){
            throw new ServiceException("Over restrict amount");
        }
        return AjaxResult.success();
    }



}

