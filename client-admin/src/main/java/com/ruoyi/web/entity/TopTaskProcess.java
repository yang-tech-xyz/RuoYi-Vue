package com.ruoyi.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "top_task_process")
public class TopTaskProcess implements Serializable {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "TASK处理号")
    @TableField(value = "process_no")
    private String processNo;

    @Schema(description = "执行日期")
    @TableField(value = "process_date")
    private LocalDate processDate;

    @Schema(description = "开始日期")
    @TableField(value = "task_start_date")
    private LocalDateTime taskStartDate;

    @Schema(description = "结束日期")
    @TableField(value = "task_end_date")
    private LocalDateTime taskEndDate;

    @Schema(description = "状态：1=执行中，2=完成，3=执行失败")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "MEMO")
    @TableField(value = "memo")
    private String memo;

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

