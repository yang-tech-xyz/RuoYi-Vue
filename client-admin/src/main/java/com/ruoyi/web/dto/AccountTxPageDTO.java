package com.ruoyi.web.dto;

import lombok.Data;

@Data
public class AccountTxPageDTO {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    private String token;

}
