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
public class BTCAddressBody extends SignBaseEntity {

    /**
     * BTC转账地址
     */
    @Schema(description = "BTC转账地址", example = "bc1q9kdcd08adkhg35r4g6nwu8ae4nkmsgp9vy00gf")
    private String btcTransferAddress;

}
