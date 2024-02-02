package com.ruoyi.web.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 商户信息表
 *
 * @author un
 * @date 2023-11-18 15:25:00
 */
@Data
@Schema(description = "用户表")
public class TopUserEntity {

	@TableId()
	private int id;
	/**
	* 主键
	*/
	@NotNull
    @Schema(description="钱包地址")
    private String wallet;

    @Schema(description="邀请码")
    private String invitedCode;


	@NotNull
	@Schema(description="邀请人的邀请码")
	private String invitedUserCode;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

}