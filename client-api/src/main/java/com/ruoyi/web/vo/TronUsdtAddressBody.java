package com.ruoyi.web.vo;

import com.ruoyi.common.SignBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户钱包注册信息
 *
 * @author ruoyi
 */
@Data
public class TronUsdtAddressBody extends SignBaseEntity {

    /**
     * BTC转账地址
     */
    @Schema(description = "TRON USDT 钱包地址", example = "41acb7530b6acfd70006b85ba25b45d9f0d624aceb")
    private String tronUsdtaddress;

}
