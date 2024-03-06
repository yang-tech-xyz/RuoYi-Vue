package com.ruoyi.admin.controller;

import com.ruoyi.admin.dto.TopTokenChainDTO;
import com.ruoyi.admin.entity.TopChain;
import com.ruoyi.admin.entity.TopToken;
import com.ruoyi.admin.entity.TopTokenChain;
import com.ruoyi.admin.exception.ServiceException;
import com.ruoyi.admin.service.TopChainService;
import com.ruoyi.admin.service.TopTokenChainService;
import com.ruoyi.admin.service.TopTokenService;
import com.ruoyi.admin.vo.TopTokenChainVO;
import com.ruoyi.common.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 充值
 *
 * @author ruoyi
 */
@Slf4j
@RequestMapping("/tokenChain")
@Tag(description = "TokenChainController", name = "币种-链信息")
@RestController
public class TopTokenChainController {

    @Autowired
    private TopTokenChainService topTokenChainService;


    @Autowired
    private TopTokenService topTokenService;

    @Autowired
    private TopChainService topChainService;


    @Operation(summary = "查询当前所有币种-链信息")
    @GetMapping("/getList")
    public AjaxResult<List<TopTokenChainVO>> getList() {
        return AjaxResult.success(topTokenChainService.getList());
    }

    @Operation(summary = "新增tokenChain")
    @PostMapping("/")
    public AjaxResult<String> add(@RequestBody TopTokenChainDTO topTokenChainDTO){
        Long chainId = topTokenChainDTO.getChainId();
        Optional<TopChain> optionalTopChain = topChainService.getOptByChainId(chainId);
        if(optionalTopChain.isEmpty()){
            log.warn("chain is not exist,chain id is:{}",chainId);
            throw new ServiceException("chain is not exist");
        }


        Long tokenId = topTokenChainDTO.getTokenId();
        Optional<TopToken> topTokenOptional = topTokenService.getOptById(tokenId);
        if(topTokenOptional.isEmpty()){
            log.warn("token is not exist,token id is:{}",tokenId);
            throw new ServiceException("token is not exist");
        }
        TopTokenChain topTokenChain = new TopTokenChain();
        BeanUtils.copyProperties(topTokenChainDTO,topTokenChain);
        topTokenChain.setCreateTime(LocalDateTime.now());
        topTokenChain.setUpdateTime(LocalDateTime.now());
        topTokenChain.setCreateBy("sys");
        topTokenChain.setUpdateBy("sys");
        topTokenChainService.save(topTokenChain);
        return AjaxResult.success();
    }

    @Operation(summary = "修改token-chain")
    @PutMapping("/")
    public AjaxResult<String> edit(@RequestBody TopTokenChainDTO topTokenChainDTO){
        topTokenChainService.edit(topTokenChainDTO);
        return AjaxResult.success();
    }

    @Operation(summary = "删除token-chain")
    @DeleteMapping("/{id}")
    public AjaxResult<String> remove(@PathVariable Long id){
        topTokenChainService.removeById(id);
        return AjaxResult.success();
    }

}
