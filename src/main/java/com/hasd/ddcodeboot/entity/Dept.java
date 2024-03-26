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
 * 部门
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
@ApiModel(value = "Dept对象", description = "部门")
public class Dept implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("父级id")
    private Integer parentId;

    @ApiModelProperty("名称")
    private String name;
    
    @ApiModelProperty("描述")
    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    private LocalDateTime updatedAt;


}
