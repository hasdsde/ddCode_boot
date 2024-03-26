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
 * 权限
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
@ApiModel(value = "Authority对象", description = "权限")
public class Authority implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("名称")
    private String name;


    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("类型  0-前端 1-后端")
    private Integer type;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;


}
