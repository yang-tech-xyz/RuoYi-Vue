package com.ruoyi.admin.dto;

import lombok.Data;

@Data
public class UserPageDTO {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    private Long userId;

    private String wallet;
    
}
