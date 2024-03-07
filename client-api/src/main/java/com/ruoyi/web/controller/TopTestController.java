package com.ruoyi.web.controller;

import cn.hutool.core.util.IdUtil;
import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.dto.InvitePageDTO;
import com.ruoyi.web.entity.TopUser;
import com.ruoyi.web.enums.TopNo;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.service.*;
import com.ruoyi.web.utils.NumbersUtils;
import com.ruoyi.web.utils.RequestUtil;
import com.ruoyi.web.utils.UnsignMessageUtils;
import com.ruoyi.web.vo.BTCAddressBody;
import com.ruoyi.web.vo.WalletRegisterBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.tron.trident.core.ApiWrapper;

import java.security.SignatureException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 测试
 *
 * @author ruoyi
 */
@Slf4j
@RequestMapping("/topTest")
@Tag(description = "TopTestController", name = "测试")
@RestController
public class TopTestController {

    @Autowired
    private TopTRONService topTRONService;


    @Operation(summary = "测试tron", description = "测试tron")
    @GetMapping("")
    public AjaxResult<?> test(){
        // main net, using TronGrid
        // ApiWrapper wrapper = ApiWrapper.ofMainnet("hex private key", "API key");
        // Nile test net, using a node from Nile official website
        ApiWrapper wrapper = ApiWrapper.ofNile("2b34557b528df6d1a0d824c47590e814bcb8269492776634d57902600eb72351");
        String hash = "24337b660790301a7c7919f11d910fa67db608ff9399a08e804fe067d9ecc91e";
        topTRONService.queryTransactionInfoByHash(wrapper,hash);
        return AjaxResult.success();
    }


}
