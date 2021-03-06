package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoForm;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-30
 */
@Api(description = "课程管理")
@RestController
@RequestMapping("/eduservice/edu-course")
@CrossOrigin
public class EduCourseController {
    @Resource
    private EduCourseService eduCourseService;

    @ApiOperation(value = "添加课程信息")
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoForm courseInfoForm) {
        String courseId = eduCourseService.addCourseInfo(courseInfoForm);
        return R.ok().data("courseId", courseId);
    }

    @ApiOperation(value = "根据id查看课程信息")
    @GetMapping("getCourseInfoById/{id}")
    public R getCourseInfoById(@PathVariable String id) {
        CourseInfoForm courseInfoForm = eduCourseService.getCourseInfoById(id);
        return R.ok().data("courseInfo", courseInfoForm);
    }

    @ApiOperation(value = "修改课程信息")
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoForm courseInfoForm) {
        eduCourseService.updateCourseInfo(courseInfoForm);
        return R.ok();
    }

    @ApiOperation(value = "根据id查询课程发布信息")
    @GetMapping("getCoursePublishById/{id}")
    public R getCoursePublishById(@PathVariable String id) {
        CoursePublishVo coursePublishVo = eduCourseService.getCoursePublishById(id);
        return R.ok().data("coursePublishVo", coursePublishVo);
    }

    @ApiOperation(value = "根据id发布课程")
    @PutMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id) {
        EduCourse eduCourse = eduCourseService.getById(id);
        eduCourse.setStatus("Normal");
        eduCourseService.updateById(eduCourse);
        return R.ok();
    }

    @ApiOperation(value = "查询所有课程信息")
    @GetMapping("getCourseInfo")
    //TODO 实现带条件、带分页查询
    public R getCourseInfo() {
        List<EduCourse> list = eduCourseService.list(null);
        return R.ok().data("list", list);
    }

    @ApiOperation(value = "根据id删除课程相关信息")
    @DeleteMapping("delCourseInfo/{id}")
    public R delCourseInfo(@PathVariable String id) {
        eduCourseService.delCourseInfo(id);
        return R.ok();
    }

    @ApiOperation(value = "分页课程查询")
    @GetMapping("{page}/{limit}")
    public R pageQuery(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
                    CourseQuery courseQuery) {

        Page<EduCourse> pageParam = new Page<>(page,limit);

        eduCourseService.pageQuery(pageParam,courseQuery);
        List<EduCourse> records = pageParam.getRecords();

        long total = pageParam.getTotal();
        return R.ok().data("total",total).data("rows",records);

    }


}

