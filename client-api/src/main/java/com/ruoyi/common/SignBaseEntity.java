package com.ruoyi.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity基类
 * 
 * @author ruoyi
 */
@Data
public class SignBaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;


    /**
     * 用户钱包地址
     */
    @Schema(description="钱包地址",example = "0x127BC32E4eD80B47EB63BA57C29A6920AB1372F4")
    private String wallet;

    /**
     * 用户签名后信息
     */
    @Schema(description="簽名信息",example = "0x2b7f8afee45230099e1ea11e989d4f6e85aa9c73987026ff264f3ec9380a1a06186407801f6df9c568458de7ffd0fd487b76207c79c9ff191eb3539105d771631b")
    private String signMsg;


    /**
     * 用户签名后信息
     */
    @Schema(description="签名内容", example= "GatPool-sign")
    private String content;


}
