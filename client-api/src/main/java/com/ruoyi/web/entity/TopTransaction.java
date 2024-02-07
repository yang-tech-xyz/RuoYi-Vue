package com.ruoyi.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 【请填写功能名称】对象 top_chain
 * 
 * @author ruoyi
 * @date 2024-02-03
 */
@Data
public class TopTransaction extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 链信息配置表 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 链类型 */
    @Schema(name = "链hash")
    private String hash;

    /** rpc 节点url */
    @Schema(name = "节点id")
    private Long chainId;

    /** 在多少个区块确认之后 才确认充值成功 */
    @Schema(name = "币种id")
    private Integer tokenId;

    /** 链id */
    @Schema(name = "链rpc节点")
    private String rpcEndpoint;

    @Schema(name = "事务状态")
    private String status;

    @Schema(name = "token标记")
    private String symbol;

    @Schema(name = "充值数量")
    private BigDecimal tokenAmount;

    @Schema(name = "充值是否成功标价,0:充值成功,1:充值失败")
    private Integer isConfirm;

    @Schema(name = "事务区块高度")
    private BigInteger height;

    @Schema(name = "用户id")
    private Long userId;

    @Schema(name = "用户id")
    private Long blockConfirm;


}
