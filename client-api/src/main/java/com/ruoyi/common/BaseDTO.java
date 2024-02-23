package com.ruoyi.common;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseDTO {
    private Integer pageNum = 1;

    private Integer pageSize = 10;
}
