package com.hasd.ddcodeboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author hasd
 * @since 2024-01-22
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "Log对象", description = "")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("权限名")
    private String authorityName;

    @ApiModelProperty("详细信息")
    private String info;

    @ApiModelProperty("方法名")
    private String methodName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdAt;


}
