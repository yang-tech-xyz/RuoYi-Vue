package com.ruoyi.admin.dto;

import lombok.Data;

@Data
public class AccountTxPageDTO {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    private String wallet;

    private String symbol;

}
