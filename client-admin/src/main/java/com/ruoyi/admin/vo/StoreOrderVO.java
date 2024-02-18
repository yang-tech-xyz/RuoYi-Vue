package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StoreOrderVO {

    @Schema(description = "钱包地址")
    private String wallet;

    @Schema(description = "产品ID")
    private Long storeId;

    @Schema(description = "投资周期")
    private Integer period;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "存单号")
    private String orderNo;

    @Schema(description = "存入币种")
    private String symbol;

    @Schema(description = "币种价格")
    private BigDecimal price;

    @Schema(description = "存入数量")
    private BigDecimal amount;

    @Schema(description = "投资金额USD：币种数量*币种价格")
    private BigDecimal investAmount;

    @Schema(description = "订单时间")
    private LocalDateTime orderDate;

    @Schema(description = "到期释放时间：订单时间+周期")
    private LocalDate releaseDate;

    @Schema(description = "状态：1=收益中，2=已领取")
    private Integer status;

    @Schema(description = "累计利息")
    private BigDecimal totalIncome;

    @Schema(description = "创建日期")
    private LocalDateTime createdDate;

    @Schema(description = "更新日期")
    private LocalDateTime updatedDate;

}
