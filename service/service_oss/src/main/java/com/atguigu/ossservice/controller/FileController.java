package com.atguigu.ossservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.ossservice.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Api(description = "文件管理")
@RestController
@RequestMapping("/ossservice/fileoss")
@CrossOrigin
public class FileController {
    @Autowired
    private FileService fileService;

    @ApiOperation(value = "上传文件")
    @PostMapping("uploadFile")
    public R uploadFile(MultipartFile file){
        //1获取文件
        //2调用接口上传文件，获取Url
        String url = fileService.uploadFileOSS(file);
        return R.ok().data("url",url);
    }


}
