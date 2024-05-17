package com.ruoyi.admin.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class PowerOrderIncomePageDTO {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private Long userId;

    private String wallet;

    private String orderNo;

}
