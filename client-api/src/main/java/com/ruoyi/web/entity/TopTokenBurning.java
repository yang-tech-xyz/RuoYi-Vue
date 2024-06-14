package com.ruoyi.web.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.*;
import java.math.BigDecimal;
import java.io.Serializable;
import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "top_token_burning")
public class TopTokenBurning implements Serializable {
    
    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "币种")
    @TableField(value = "symbol")
    private String symbol;
    
    @Schema(description = "销毁数量")
    @TableField(value = "burning_amount")
    private BigDecimal burningAmount;
    
    @Schema(description = "创建日期")
    @TableField(value = "created_date")
    private LocalDateTime createdDate;
    
    @Schema(description = "创建人")
    @TableField(value = "created_by")
    private String createdBy;
    
    @Schema(description = "更新日期")
    @TableField(value = "updated_date")
    private LocalDateTime updatedDate;
    
    @Schema(description = "更新人")
    @TableField(value = "updated_by")
    private String updatedBy;
    

}

