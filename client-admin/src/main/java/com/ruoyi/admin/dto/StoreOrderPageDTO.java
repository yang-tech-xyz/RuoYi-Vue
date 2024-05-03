package com.ruoyi.admin.dto;

import lombok.Data;

@Data
public class StoreOrderPageDTO {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    private Long userId;

    private String wallet;

    private String symbol;

    private Integer period;

}
