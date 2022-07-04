package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.CourseInfoForm;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.handler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-06-30
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Resource
    EduCourseDescriptionService eduCourseDescriptionService;

    @Resource
    EduVideoService eduVideoService;

    @Resource
    EduChapterService eduChapterService;

    @Override
    public String addCourseInfo(CourseInfoForm courseInfoForm) {

        //1.添加课程信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm,eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if (insert ==0){
            throw new GuliException(2001,"创建课程失败");
        }

        //2.获取课程id
        String courseId = eduCourse.getId();

        //3.添加课程描述信息
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseId);
        eduCourseDescription.setDescription(courseInfoForm.getDescription());
        eduCourseDescriptionService.save(eduCourseDescription);

        return courseId;
    }

    @Override
    public CourseInfoForm getCourseInfoById(String id) {
        //根据id查询课程信息
        EduCourse eduCourse = baseMapper.selectById(id);
        //封装课程信息
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(eduCourse,courseInfoForm);
        //根据id查看课程描述信息
        EduCourseDescription eduCourseDescription = eduCourseDescriptionService.getById(id);
        //封装课程描述
        courseInfoForm.setDescription(eduCourseDescription.getDescription());
        return courseInfoForm;
    }


    //修改课程信息
    @Override
    public void updateCourseInfo(CourseInfoForm courseInfoForm) {
        //1.复制课程数据
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm,eduCourse);
        //2.更新课程数据
        int update = baseMapper.updateById(eduCourse);
        //3.判断是否成功
        if (update==0){
            throw new GuliException(2001,"修改课程失败");
        }
        //4.更新课程描述
        EduCourseDescription eduCourseDescription =new EduCourseDescription();
        eduCourseDescription.setId(courseInfoForm.getId());
        eduCourseDescription.setDescription(courseInfoForm.getDescription());
        eduCourseDescriptionService.updateById(eduCourseDescription);
    }

    //根据课程id查询课程发布信息
    @Override
    public CoursePublishVo getCoursePublishById(String id) {
        CoursePublishVo coursePublishVo = baseMapper.getCoursePublishById(id);
        return coursePublishVo;
    }


    //
    @Override
    public void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();
        courseWrapper.orderByDesc("gmt_create");

        if (courseQuery == null){
            baseMapper.selectPage(pageParam,courseWrapper);
            return;
        }

        String title = courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();

        if (!StringUtils.isEmpty(title)){
            courseWrapper.like("title",title);
        }

        if (!StringUtils.isEmpty(teacherId)){
            courseWrapper.like("teacher_id",teacherId);
        }

        if (!StringUtils.isEmpty(subjectParentId)){
            courseWrapper.like("subject_parent_id",subjectParentId);
        }

        if (!StringUtils.isEmpty(subjectId)){
            courseWrapper.like("subject_id",subjectId);
        }

        baseMapper.selectPage(pageParam,courseWrapper);

    }

    @Override
    public void delCourseInfo(String id) {
        //1.TODO 删除视频

        //2.删除小节
        QueryWrapper<EduVideo> videoWrapper =new QueryWrapper<>();
        videoWrapper.eq("course_id",id);
        eduVideoService.remove(videoWrapper);
        //3.删除章节
        QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_id",id);
        eduChapterService.remove(chapterWrapper);
        //4.删除课程描述
        eduCourseDescriptionService.removeById(id);

        //5.删除课程
        int delete = baseMapper.deleteById(id);
        if (delete == 0){
            throw new GuliException(20001,"删除课程失败");
        }

    }
}
