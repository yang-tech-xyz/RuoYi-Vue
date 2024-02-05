package com.ruoyi.common;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value="钱包地址",example = "0x5ebacac108d665819398e5c37e12b0162d781398")
    private String wallet;

    /**
     * 用户签名后信息
     */
    @ApiModelProperty(value="簽名信息",example = "0x4a2164fff41e784af1482cd91f8b7b60f8e1216c9afc89689f83bcaa27115e4d124af62bb2f24013a55604fe37e8401bf2f607ade2b84f2d353807f3f961ac0d1c")
    private String signMsg;


    /**
     * 用户签名后信息
     */
    @ApiModelProperty(value="签名内容", example= "青年人的责任重大！努力吧...")
    private String content;


}
