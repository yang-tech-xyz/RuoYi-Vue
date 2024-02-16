package com.ruoyi.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminVO {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "谷歌密钥")
    private String googleSecret;

    @Schema(description = "状态：1=有效，2=禁止登录")
    private Integer status;

    @Schema(description = "创建日期")
    private LocalDateTime createdDate;

    @Schema(description = "创建人")
    private String createdBy;

    @Schema(description = "更新日期")
    private LocalDateTime updatedDate;

    @Schema(description = "更新人")
    private String updatedBy;


}
