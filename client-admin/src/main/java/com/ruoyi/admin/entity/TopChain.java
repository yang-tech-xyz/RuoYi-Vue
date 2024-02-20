package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** 链信息配置表 */
@Data
public class TopChain extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Schema(description="主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 链类型 */
    @Schema(description = "链类型")
    private String chainType;

    /** rpc 节点url */
    @Schema(description = "rpc 节点url")
    private String rpcEndpoint;

    /** 在多少个区块确认之后 才确认充值成功 */
    @Schema(description = "在多少个区块确认之后 才确认充值成功")
    private Long blockConfirm;

    /** 链id */
    @Schema(description = "链id")
    private Long chainId;

    /** 项目方收款地址 */
    @Schema(description = "项目方收款地址")
    private String receiveAddress;

    @Schema(description = "链名称")
    private String chainName;

}
