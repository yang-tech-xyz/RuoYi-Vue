package com.ruoyi.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminUpdateDTO {

    @Schema(description = "账号")
    private String account;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "谷歌密钥")
    private String googleSecret;

    @Schema(description = "状态：1=有效，2=禁止登录")
    private Integer status;

}
