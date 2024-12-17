package com.ruoyi.admin.dto;

import com.ruoyi.common.BaseDTO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 【请填写功能名称】对象 top_chain
 *
 * @author ruoyi
 * @date 2024-02-03
 */
@Data
public class TransactionStatisticDTO extends BaseDTO {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDateTime;


    private String symbol;

}