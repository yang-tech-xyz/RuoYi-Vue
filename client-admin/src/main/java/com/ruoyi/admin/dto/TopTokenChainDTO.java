package com.ruoyi.admin.dto;

import com.ruoyi.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author ruoyi
 * @date 2024-02-03
 */
@Data
public class TopTokenChainDTO
{
    @Schema(description = "token id")
    private Long tokenId;

    @Schema(description = "链id")
    private Long chainId;

    @Schema(description = "价格平台")
    private String erc20Address;

}
