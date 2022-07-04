package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OneSubjectVo {
    @ApiModelProperty(value = "课程类别id")
    private String id;

    @ApiModelProperty(value = "类别名称")
    private String title;

    private List<TwoSubjectVo> children = new ArrayList<>();
}
