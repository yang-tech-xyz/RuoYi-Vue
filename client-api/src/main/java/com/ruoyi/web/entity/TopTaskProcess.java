package com.ruoyi.web.entity;

import java.time.LocalDate;
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
@TableName(value = "top_task_process")
public class TopTaskProcess implements Serializable {
    
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("TASK处理号")
    @TableField(value = "process_no")
    private String processNo;
    
    @ApiModelProperty("执行日期")
    @TableField(value = "process_date")
    private LocalDate processDate;
    
    @ApiModelProperty("开始日期")
    @TableField(value = "task_start_date")
    private LocalDateTime taskStartDate;
    
    @ApiModelProperty("结束日期")
    @TableField(value = "task_end_date")
    private LocalDateTime taskEndDate;
    
    @ApiModelProperty("状态：1=执行中，2=完成，3=执行失败")
    @TableField(value = "status")
    private Integer status;
    
    @ApiModelProperty("MEMO")
    @TableField(value = "memo")
    private String memo;
    
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

