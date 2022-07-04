package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.vo.ExcelSubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


public class SubjectExcelListener extends AnalysisEventListener<ExcelSubjectData> {

    public EduSubjectService eduSubjectService;

    public SubjectExcelListener() {}
    //创建有参数构造，传递subjectService用于操作数据库
    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }


    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        //1读取数据验空
        if(excelSubjectData==null){
            throw  new GuliException(20001,"导入课程分类失败");
        }
        //2判断一级分类名称是否重复
        EduSubject existOneSubject = this.existOneSubject(eduSubjectService, excelSubjectData.getOneSubjectName());

        //3一级不重复插入数据库
        if(existOneSubject==null){
            existOneSubject = new EduSubject();
            existOneSubject.setTitle(excelSubjectData.getOneSubjectName());
            existOneSubject.setParentId("0");
            eduSubjectService.save(existOneSubject);
        }
        String pid = existOneSubject.getId();
        //4判断二级名称是否重复
        EduSubject existTwoSubject = this.existTwoSubject(eduSubjectService, excelSubjectData.getTwoSubjectName(), pid);
        //5二级不重复插入数据库
        if(existTwoSubject==null){
            existTwoSubject = new EduSubject();
            existTwoSubject.setTitle(excelSubjectData.getTwoSubjectName());
            existTwoSubject.setParentId(pid);
            eduSubjectService.save(existTwoSubject);
        }
    }

    //判断一级分类是否重复
    private EduSubject existOneSubject(EduSubjectService subjectService, String name) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id","0");
        wrapper.eq("title",name);
        EduSubject eduSubject = subjectService.getOne(wrapper);
        return eduSubject;
    }

    //判断二级分类是否重复
    private EduSubject existTwoSubject(EduSubjectService eduSubjectService, String name,String pid) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",pid);
        wrapper.eq("title",name);
        EduSubject eduSubject = eduSubjectService.getOne(wrapper);
        return eduSubject;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}