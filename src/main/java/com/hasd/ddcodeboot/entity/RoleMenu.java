package com.hasd.ddcodeboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
    
/**
 * <p>
 * 
 * </p>
 *
 * @author hasd
 * @since 2024-01-20
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
    @TableName("role_menu")
@ApiModel(value = "RoleMenu对象", description = "")
    public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

                                        @TableId(value = "id", type = IdType.AUTO)
                                    private Integer id;

                        @ApiModelProperty("角色id")
                                private Integer roleId;

                        @ApiModelProperty("菜单id")
                                private Integer menuId;


}
