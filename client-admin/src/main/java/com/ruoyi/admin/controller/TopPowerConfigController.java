package com.ruoyi.admin.controller;

import com.ruoyi.admin.entity.TopChain;
import com.ruoyi.admin.entity.TopPowerConfig;
import com.ruoyi.admin.service.TopPowerConfigService;
import com.ruoyi.admin.vo.TopPowerConfigVO;
import com.ruoyi.common.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 算力配置文件表
 *
 * @author ruoyi
 */
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/powerConfig")
@Tag(description = "TopPowerConfigController", name = "算力配置")
@RestController
public class TopPowerConfigController {


    private final TopPowerConfigService topPowerConfigService;

    @Operation(summary = "查询配置")
    @GetMapping("/")
    public AjaxResult<TopPowerConfigVO> getConfig(){
        TopPowerConfig topPowerConfig = topPowerConfigService.list().getFirst();
        TopPowerConfigVO topPowerConfigVO = new TopPowerConfigVO();
        BeanUtils.copyProperties(topPowerConfig,topPowerConfigVO);
        return AjaxResult.success(topPowerConfigVO);
    }

    @Operation(summary = "更新配置")
    @PostMapping("/")
    public AjaxResult<TopPowerConfigVO> update(@RequestBody TopPowerConfig topPowerConfig){
        topPowerConfigService.updateById(topPowerConfig);
        return AjaxResult.success();
    }

}
