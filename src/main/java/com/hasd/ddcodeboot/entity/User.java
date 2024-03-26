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
 * 用户表
 * </p>
 *
 * @author hasd
 * @since 2023-12-18
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "User对象", description = "用户表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty("登录名")
    private String userName;

    @ApiModelProperty("显示名称")
    private String nickName;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("性别 0-未知 1男 2女")
    private Integer sex;

    @ApiModelProperty("部门")
    private Integer deptId;

    @ApiModelProperty("角色")
    private Integer roleId;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedAt;

    @ApiModelProperty("删除时间")
    private LocalDateTime deletedAt;

    @ApiModelProperty("备注")
    private String comment;


}
