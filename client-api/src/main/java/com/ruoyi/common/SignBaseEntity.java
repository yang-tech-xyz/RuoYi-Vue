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
    @Schema(description="钱包地址",example = "413368e5dfc93fbfcf5f6ea1c7659814394a897aa8")
    private String wallet;

    /**
     * 用户签名后信息
     */
    @Schema(description="簽名信息",example = "bfe495b09a8b22bdf3ab169d9f152b209611815f1e7766b5ee458f1deeb625fd36dbab8cc23f3d3f1bae87293c0344bffe9e4b476fd5073b6db0f3b954dca47500")
    private String signMsg;


    /**
     * 用户签名后信息
     */
    @Schema(description="签名内容", example= "3f41ea1947027fd0b30f32f1fdddf3236f00fbb090a5223a1888c74995ea70e9")
    private String content;


}
