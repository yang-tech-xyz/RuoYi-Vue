package com.ruoyi.admin.controller;

import com.ruoyi.admin.dto.TopChainDTO;
import com.ruoyi.admin.entity.TopChain;
import com.ruoyi.admin.service.TopChainService;
import com.ruoyi.common.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 充值
 *
 * @author ruoyi
 */
@Slf4j
@RequestMapping("/chain")
@Tag(description = "TopChainController", name = "链信息")
@RestController
public class TopChainController {

    @Autowired
    private TopChainService topChainService;


    @Operation(summary = "查询当前所有币种")
    @GetMapping("/getList")
    public AjaxResult<List<TopChain>> getList() {
        return AjaxResult.success(topChainService.list());
    }

    @Operation(summary = "新增chain")
    @PostMapping("/")
    public AjaxResult<String> add(@RequestBody TopChainDTO topChainDTO){
        TopChain topChain = new TopChain();
        BeanUtils.copyProperties(topChainDTO,topChain);
        topChain.setCreateTime(LocalDateTime.now());
        topChain.setUpdateTime(LocalDateTime.now());
        topChain.setCreateBy("sys");
        topChain.setUpdateBy("sys");
        topChainService.save(topChain);
        return AjaxResult.success();
    }

    @Operation(summary = "修改链信息")
    @PutMapping("/")
    public AjaxResult<String> edit(@RequestBody TopChain topChain){
        topChain.setUpdateTime(LocalDateTime.now());
        topChainService.updateById(topChain);
        return AjaxResult.success();
    }

    @Operation(summary = "删除链信息")
    @DeleteMapping("/{id}")
    public AjaxResult<String> remove(@PathVariable Long id){
        topChainService.removeById(id);
        return AjaxResult.success();
    }

}
