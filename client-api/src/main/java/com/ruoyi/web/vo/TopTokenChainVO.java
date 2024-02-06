package com.ruoyi.web.vo;

import com.ruoyi.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 币种配置对象 top_token
 * 
 * @author ruoyi
 * @date 2024-02-03
 */
@Data
public class TopTokenChainVO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** token 名称 */
    @Schema(name = "token 名称")
    private String symbol;

    /** 小数位 */
    @Schema(name = "小数位")
    private Long decimal;

    /** 是否上线: 0,上线,1下线 */
    @Schema(name = "是否上线: 0,上线,1下线")
    private Integer online;

    @Schema(name = "该币种在该链的ERC20地址")
    private String erc20Address;

    @Schema(name = "链id")
    private Integer chainId;
}
