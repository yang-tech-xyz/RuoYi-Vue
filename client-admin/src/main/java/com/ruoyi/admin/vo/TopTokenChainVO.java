package com.ruoyi.admin.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * @author ruoyi
 * @date 2024-02-03
 */
@Data
public class TopTokenChainVO extends BaseEntity
{
    @Schema(description = "id")
    private Long id;

    @Schema(description = "token id")
    private Long tokenId;


    @Schema(description = "token symbol")
    private String symbol;

    @Schema(description = "链id")
    private Long chainId;

    @Schema(description = "链名称")
    private String chainName;

    @Schema(description = "价格平台")
    private String erc20Address;

}
