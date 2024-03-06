package com.ruoyi.web.dto;

import com.ruoyi.common.SignBaseEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StoreCpOrderDTO extends SignBaseEntity {

    @NotBlank
    private String orderNo;
}
