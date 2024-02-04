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
import io.swagger.annotations.ApiModelProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "top_token_price")
public class TopTokenPrice implements Serializable {
    
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("币种")
    @TableField(value = "token")
    private String token;
    
    @ApiModelProperty("价格")
    @TableField(value = "price")
    private BigDecimal price;
    
    @ApiModelProperty("创建日期")
    @TableField(value = "created_date")
    private LocalDateTime createdDate;
    
    @ApiModelProperty("创建人")
    @TableField(value = "created_by")
    private String createdBy;
    
    @ApiModelProperty("更新日期")
    @TableField(value = "updated_date")
    private LocalDateTime updatedDate;
    
    @ApiModelProperty("更新人")
    @TableField(value = "updated_by")
    private String updatedBy;
    

}

