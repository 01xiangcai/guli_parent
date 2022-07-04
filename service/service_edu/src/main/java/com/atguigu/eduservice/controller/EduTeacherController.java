package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.servicebase.handler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-22
 */
@Api(description = "讲师管理")
@RestController
@CrossOrigin
@RequestMapping("/eduservice/edu-teacher")
public class EduTeacherController {

    @Resource
    private EduTeacherService eduTeacherService;

    //查询所有教师
    @ApiOperation(value = "查询所有讲师列表")
    @GetMapping("getTeacher")
    public R list() {
//        try {
//            int a = 10/0;
//        }catch(Exception e) {
//            throw new GuliException(20001,"出现自定义异常");
//        }
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("list", list);
    }

    //讲师逻辑删除功能
    @ApiOperation(value = "根据id删除讲师")
    @DeleteMapping("{id}")
    public R removeById(@PathVariable String id) {
        eduTeacherService.removeById(id);
        return R.ok();
    }


    @ApiOperation(value = "讲师分页列表查询")
    @GetMapping("{page}/{limit}")
    public R pageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit) {

        Page<EduTeacher> pageParam = new Page<>(page, limit);

        eduTeacherService.page(pageParam, null);
        List<EduTeacher> records = pageParam.getRecords();
        long total = pageParam.getTotal();

        return R.ok().data("total", total).data("list", records);

    }

    @ApiOperation(value = "讲师分页列表查询带条件")
    @PostMapping("getTeacherPageVo/{current}/{limit}")
    public R getTeacherpageVo(
            @ApiParam(name = "current", value = "当前页码", required = true)
            @PathVariable Long current,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "teacherQuery", value = "条件填写", required = true)
            @RequestBody TeacherQuery teacherQuery) {//@RequestBody把json串转化成实体类

        //1.取出查询条件
        Integer level = teacherQuery.getLevel();
        String name = teacherQuery.getName();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        //2.判断条件是否为空
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)){
            wrapper.like("name",name);
        }
        if (!StringUtils.isEmpty(level)){
            wrapper.like("level",level);
        }
        if (!StringUtils.isEmpty(begin)){
            wrapper.like("begin",begin);
        }
        if (!StringUtils.isEmpty(end)){
            wrapper.like("end",end);
        }

        Page<EduTeacher> pageParam = new Page<>(current, limit);

        eduTeacherService.page(pageParam, wrapper);
        List<EduTeacher> records = pageParam.getRecords();
        long total = pageParam.getTotal();

        return R.ok().data("total", total).data("list", records);

    }

    @ApiOperation(value = "新增教师")
    @PostMapping("addTeacher")
    public R save(
            @ApiParam(name = "teacher",value = "讲师对象",required = true)
             @RequestBody EduTeacher eduTeacher){
        boolean save = eduTeacherService.save(eduTeacher);
        if (save) {
            return R.ok();
        }else{
            return R.error();
        }

    }

  @ApiOperation(value = "根据id查询教师")
    @GetMapping("getTeacherById/{id}")
    public R getById(
            @ApiParam(name = "id",value = "讲师id",required = true)
            @PathVariable String id){
      EduTeacher eduTeacher = eduTeacherService.getById(id);
      return R.ok().data("eduTeacher",eduTeacher);
  }


  @ApiOperation(value = "根据id修改教师")
  @PutMapping("{id}")
  public R updeleteById(
          @ApiParam(name = "id",value = "讲师id",required = true)
          @PathVariable String id,

          @ApiParam(name = "teacher",value = "讲师对象",required = true)
          @RequestBody EduTeacher eduTeacher){

        eduTeacher.setId(id);
        eduTeacherService.updateById(eduTeacher);
        return R.ok();

  }

    @ApiOperation(value = "修改讲师")
    @PostMapping("updateTeacher")
    public R updateTeacher( @RequestBody EduTeacher eduTeacher){
        boolean update = eduTeacherService.updateById(eduTeacher);
        if(update){
            return R.ok();
        }else{
            return R.error();
        }
    }

}

