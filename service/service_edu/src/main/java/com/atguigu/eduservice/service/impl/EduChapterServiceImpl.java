package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-07-02
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Resource
    private EduVideoService videoService;
    @Override
    public List<ChapterVo> getChapterVideoById(String courseId) {
        //1根据courseId查询章节集合信息
        QueryWrapper<EduChapter> chapterWrapper =new QueryWrapper<>();
        chapterWrapper.eq("course_id",courseId);
        List<EduChapter> chapterList = baseMapper.selectList(chapterWrapper);

        //2根据courseId查询小节集合信息
        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_id",courseId);
        List<EduVideo> videoList = videoService.list(videoWrapper);

        //3遍历章节信息进行封装
        ArrayList<ChapterVo> chapterVideoList = new ArrayList<>();
        for (int i = 0; i < chapterList.size(); i++) {
            EduChapter eduChapter = chapterList.get(i);
            ChapterVo chapterVo =new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            chapterVideoList.add(chapterVo);

            //4遍历和此章节关联小节信息进行封装
            List<VideoVo> videoVos = new ArrayList<>();
            for (int m = 0; m < videoList.size(); m++) {
                EduVideo eduVideo =videoList.get(m);
                if (eduChapter.getId().equals(eduVideo.getChapterId())){
                    VideoVo videoVo =new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoVos.add(videoVo);
                }
                chapterVo.setChildren(videoVos);
            }

        }
        return chapterVideoList;
    }

}
