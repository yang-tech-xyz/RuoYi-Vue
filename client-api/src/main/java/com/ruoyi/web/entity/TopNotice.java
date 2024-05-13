package com.ruoyi.web.entity;

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
@TableName(value = "top_notice")
public class TopNotice implements Serializable {
    
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("标题")
    @TableField(value = "title")
    private String title;
    
    @ApiModelProperty("内容")
    @TableField(value = "contents")
    private String contents;
    
    @ApiModelProperty("排序：倒序")
    @TableField(value = "seq")
    private Integer seq;
    
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

