package com.hasd.ddcodeboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 *
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
@TableName("role_authority")
@ApiModel(value = "RoleAuthority对象", description = "")
public class RoleAuthority implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer roleId;

    private Integer authorityId;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


}
