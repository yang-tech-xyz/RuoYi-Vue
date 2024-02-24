package com.ruoyi.admin.dto;

import com.ruoyi.admin.enums.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateIconDTO {

    @Schema(description = "图标,图片使用base64压缩后的字符串")
    private String icon;

    @Schema(description = "图标id")
    private Long tokenId;

}