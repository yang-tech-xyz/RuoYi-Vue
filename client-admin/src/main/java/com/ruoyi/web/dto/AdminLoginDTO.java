package com.ruoyi.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminLoginDTO {

    @NotBlank
    private String account;

    @NotBlank
    private String password;

    @NotBlank
    private String googleCode;

}
