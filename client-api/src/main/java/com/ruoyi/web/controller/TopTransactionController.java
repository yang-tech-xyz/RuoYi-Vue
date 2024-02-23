package com.ruoyi.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.dto.TopTransactionDTO;
import com.ruoyi.web.entity.TopTransaction;
import com.ruoyi.web.service.TopPowerConfigService;
import com.ruoyi.web.service.TopTokenService;
import com.ruoyi.web.service.TopTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 充值
 *
 * @author ruoyi
 */
@Slf4j
@RequestMapping("/transaction")
@Tag(description = "TopTransactionController", name = "事务信息")
@RestController
public class TopTransactionController {


    @Autowired
    private TopTransactionService topTransactionService;

    @Autowired
    private TopPowerConfigService topPowerConfigService;

    @Autowired
    private TopTokenService topTokenService;

    @Operation(summary = "查询当前所有币种")
    @GetMapping("/")
    public AjaxResult<IPage<TopTransaction>> getTransaction(@ParameterObject TopTransactionDTO topTransaction) {
        return AjaxResult.success(topTransactionService.getTransaction(topTransaction));
    }
}
