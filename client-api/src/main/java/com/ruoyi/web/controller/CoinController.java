package com.ruoyi.web.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.entity.TopChain;
import com.ruoyi.web.entity.TopToken;
import com.ruoyi.web.entity.TopTransaction;
import com.ruoyi.web.entity.TopUserEntity;
import com.ruoyi.web.service.TopChainService;
import com.ruoyi.web.service.TopTokenService;
import com.ruoyi.web.service.TopUserService;
import com.ruoyi.web.utils.NumbersUtils;
import com.ruoyi.web.utils.UnsignMessageUtils;
import com.ruoyi.web.vo.RechargeBody;
import com.ruoyi.web.vo.TopTokenChainVO;
import com.ruoyi.web.vo.WalletRegisterBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.abi.TypeDecoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 充值
 * 
 * @author ruoyi
 */
@Slf4j
@RequestMapping("/token")
@Tag(description = "TokenController", name = "token信息")
@RestController
public class CoinController
{

    @Autowired
    private TopTokenService topTokenService;

    @Operation(summary = "根据链id查询所有支持的token")
    @GetMapping("queryTokensByChainId")
    public AjaxResult queryTokensByChainId(@Parameter String chainId){
        List<TopTokenChainVO> list= topTokenService.queryTokensByChainId(chainId);
        return AjaxResult.success("success",list);
    }


    /**
     * 充值
     *
     * @param rechargeBody 充值hash值
     * @return 结果
     */
    @Operation(summary = "充值到账")
    @PostMapping("/recharge")
    public AjaxResult recharge(@RequestBody RechargeBody rechargeBody)throws Exception{
        return topTokenService.recharge(rechargeBody);
    }


}
