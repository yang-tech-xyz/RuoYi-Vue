package com.ruoyi.admin.vo;

import com.ruoyi.admin.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class TopTransactionVO {

    /**
     * 链信息配置表
     */
    private Long id;

    /**
     * 链hash
     */
    @Schema(description = "链hash")
    private String hash;

    /**
     * 链id
     */
    @Schema(description = "节点id")
    private Long chainId;

    @Schema(description = "币种id")
    private Integer tokenId;

    /**
     * 链id
     */
    @Schema(description = "链rpc节点")
    private String rpcEndpoint;

    @Schema(description = "事务状态")
    private String status;

    @Schema(description = "token标记")
    private String symbol;

    @Schema(description = "充值数量")
    private BigDecimal tokenAmount;

    @Schema(description = "充值是否成功标价,0:充值成功,1:充值失败")
    private Integer isConfirm;

    @Schema(description = "事务区块高度")
    private BigInteger height;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户id")
    private Long blockConfirm;

    @Schema(description = "事务类型,recharge充值,withdraw提现")
    private TransactionType type;

    @Schema(description = "事务号")
    private String transNo;

    @Schema(description = "erc20地址")
    private String erc20Address;


    @Schema(description = "提现接收地址")
    private String withdrawReceiveAddress;


    @Schema(description = "提现数量")
    private String withdrawAmount;

    @Schema(description = "已经处理次数,仍然未能确认该笔交易")
    private int retryCounts;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
