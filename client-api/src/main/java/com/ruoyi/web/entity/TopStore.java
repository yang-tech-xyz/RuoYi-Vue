package com.ruoyi.web.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "top_store")
public class TopStore implements Serializable {
    
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户ID")
    @TableField(value = "meb_id")
    private Long mebId;
    
    @ApiModelProperty("存单号")
    @TableField(value = "store_no")
    private String storeNo;
    
    @ApiModelProperty("存入币种")
    @TableField(value = "token")
    private String token;
    
    @ApiModelProperty("存入金额")
    @TableField(value = "amount")
    private BigDecimal amount;
    
    @ApiModelProperty("存入时间")
    @TableField(value = "store_date")
    private LocalDateTime storeDate;
    
    @ApiModelProperty("释放时间")
    @TableField(value = "release_date")
    private LocalDateTime releaseDate;
    
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

