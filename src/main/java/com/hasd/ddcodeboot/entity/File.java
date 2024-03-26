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
 * 文件表
 * </p>
 *
 * @author hasd
 * @since 2024-01-26
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "File对象", description = "文件表")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("文件名")
    private String name;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("文件大小")
    private Integer size;

    @ApiModelProperty("本地路径")
    private String path;

    @ApiModelProperty("URL")
    private String url;

    @ApiModelProperty("MD5")
    private String md5;

    @ApiModelProperty("格式")
    private String format;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty("修改时间")
    private LocalDateTime updatedAt;

    @ApiModelProperty("删除时间")
    private LocalDateTime deletedAt;
}
