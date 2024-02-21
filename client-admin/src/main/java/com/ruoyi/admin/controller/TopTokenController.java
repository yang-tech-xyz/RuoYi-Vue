package com.ruoyi.admin.controller;

import com.ruoyi.admin.common.CommonStatus;
import com.ruoyi.admin.entity.TopPowerConfig;
import com.ruoyi.admin.entity.TopToken;
import com.ruoyi.admin.entity.TopTransaction;
import com.ruoyi.admin.exception.ServiceException;
import com.ruoyi.admin.service.TopPowerConfigService;
import com.ruoyi.admin.service.TopTokenService;
import com.ruoyi.admin.service.TopTransactionService;
import com.ruoyi.admin.utils.UnsignMessageUtils;
import com.ruoyi.admin.vo.TokenVO;
import com.ruoyi.admin.vo.WithdrawAuditBody;
import com.ruoyi.common.AjaxResult;
import com.ruoyi.common.CommonSymbols;
import com.ruoyi.web.dto.TopTokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 充值
 *
 * @author ruoyi
 */
@Slf4j
@RequestMapping("/token")
@Tag(description = "TokenController", name = "token信息")
@RestController
public class TopTokenController {

    @Autowired
    private TopTokenService topTokenService;

    @Autowired
    private TopTransactionService topTransactionService;

    @Autowired
    private TopPowerConfigService topPowerConfigService;

    @Operation(summary = "查询当前所有币种")
    @GetMapping("/getList")
    public AjaxResult<List<TokenVO>> getList() {
        return AjaxResult.success(topTokenService.getList());
    }

    @Operation(summary = "新增token")
    @PostMapping("/")
    public AjaxResult<String> add(@RequestBody TopTokenDTO topTokenDTO){
        TopToken topToken = new TopToken();
        BeanUtils.copyProperties(topTokenDTO,topToken);
        topToken.setCreateTime(LocalDateTime.now());
        topToken.setUpdateTime(LocalDateTime.now());
        topToken.setCreateBy("sys");
        topToken.setUpdateBy("sys");
        topTokenService.save(topToken);
        return AjaxResult.success();
    }

    @Operation(summary = "修改token")
    @PutMapping("/")
    public AjaxResult<String> edit(@RequestBody TopToken topToken){
        topToken.setUpdateTime(LocalDateTime.now());
        topTokenService.updateById(topToken);
        return AjaxResult.success();
    }

    @Operation(summary = "删除token")
    @DeleteMapping("/{id}")
    public AjaxResult<String> remove(@PathVariable Long id){
        topTokenService.removeById(id);
        return AjaxResult.success();
    }
}
