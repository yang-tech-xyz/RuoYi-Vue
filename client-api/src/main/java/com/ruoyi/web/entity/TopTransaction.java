package com.ruoyi.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 【请填写功能名称】对象 top_chain
 * 
 * @author ruoyi
 * @date 2024-02-03
 */
@Data
@ApiModel
public class TopTransaction extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 链信息配置表 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 链类型 */
    @ApiModelProperty(name = "链hash")
    private String hash;

    /** rpc 节点url */
    @ApiModelProperty(name = "节点id")
    private Integer chainId;

    /** 在多少个区块确认之后 才确认充值成功 */
    @ApiModelProperty(name = "币种id")
    private Integer tokenId;

    /** 链id */
    @ApiModelProperty(name = "链rpc节点")
    private String rpcEndpoint;

    @ApiModelProperty(name = "事务状态")
    private String status;

    @ApiModelProperty(name = "token标记")
    private String symbol;

    @ApiModelProperty(name = "充值数量")
    private BigDecimal tokenAmount;

    @ApiModelProperty(name = "充值是否成功标价,0:充值成功,1:充值失败")
    private BigInteger confirm;

    @ApiModelProperty(name = "事务区块高度")
    private BigInteger height;


}
