package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.common.SignBaseEntity;
import com.ruoyi.web.dto.ClaimDTO;
import com.ruoyi.web.service.TopPowerDailyIncomeService;
import com.ruoyi.web.utils.RequestUtil;
import com.ruoyi.web.utils.UnsignMessageUtils;
import com.ruoyi.web.vo.DailyIncomeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.util.List;

@Tag(description = "挖矿收益", name = "挖矿收益")
@RestController
@RequestMapping("topPowerDailyIncome")
public class TopPowerDailyIncomeController {

    @Autowired
    private TopPowerDailyIncomeService service;

    @Operation(summary = "查询未领取收益")
    @GetMapping("/getUnclaimedList")
    public AjaxResult<List<DailyIncomeVO>> getUnclaimedList() {
        return AjaxResult.success(service.getUnclaimedList(RequestUtil.getWallet()));
    }

    @Operation(summary = "领取收益")
    @PostMapping("/claim")
    public AjaxResult<Boolean> claim(@RequestBody ClaimDTO dto) {
        try {
            boolean validateResult = UnsignMessageUtils.validate(dto.getSignMsg(), dto.getContent(), dto.getWallet());
            if (!validateResult) {
                return AjaxResult.error("validate sign error!");
            }
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success(service.claim(RequestUtil.getWallet(), dto.getId()));
    }

    @Operation(summary = "领取全部收益")
    @PostMapping("/claimAll")
    public AjaxResult<Boolean> claimAll(@RequestBody SignBaseEntity signBase) {
        try {
            boolean validateResult = UnsignMessageUtils.validate(signBase.getSignMsg(), signBase.getContent(), signBase.getWallet());
            if (!validateResult) {
                return AjaxResult.error("validate sign error!");
            }
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success(service.claimAll(RequestUtil.getWallet()));
    }
}