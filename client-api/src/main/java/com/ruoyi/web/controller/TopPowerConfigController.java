package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.entity.TopPowerConfig;
import com.ruoyi.web.service.TopPowerConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class TopPowerConfigController
{


    private final TopPowerConfigService topPowerConfigService;

    @Operation(summary = "查询算力配置表")
    @GetMapping("")
    public AjaxResult<TopPowerConfig> getPowerConfig(){
        TopPowerConfig topPowerConfig = topPowerConfigService.list().getFirst();
        return AjaxResult.success(topPowerConfig);
    }

}
