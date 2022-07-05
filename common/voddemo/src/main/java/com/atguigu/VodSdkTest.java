package com.atguigu;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import org.junit.Test;

import java.util.List;

public class VodSdkTest {

    String accessKeyId = "LTAI5tKjWVzbQamKWWp3wzis";
    String accessKeySecret = "PFcFLdyuT2ycaMU1HJo6wecZPZG788";


    @Test
    public void testGetPlayInfo() throws ClientException {
        //*初始化客户端对象
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(accessKeyId, accessKeySecret);
//*创建请求对象（不同操作，类不同）
        GetPlayInfoRequest request = new GetPlayInfoRequest();
//*创建响应对象（不同操作，类不同）
        GetPlayInfoResponse response = new GetPlayInfoResponse();
//*向请求中设置参数
        request.setVideoId("40bdbc56395a4b609b901fe5a54ffe6e");
// *调用客户端对象方法发送请求，拿到响应
        response = client.getAcsResponse(request);
//*从响应中拿到数据
        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
        }
    }

    @Test
    public void testGetVideoPlayAuth()throws ClientException{
        //*初始化客户端对象
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(accessKeyId, accessKeySecret);
        //*创建请求对象（不同操作，类不同）
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        //*创建响应对象（不同操作，类不同）
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        //*向请求中设置参数
        request.setVideoId("40bdbc56395a4b609b901fe5a54ffe6e");
        // *调用客户端对象方法发送请求，拿到响应
        response = client.getAcsResponse(request);
        //*从响应中拿到数据
        String playAuth = response.getPlayAuth();
        System.out.println("playAuth = "+playAuth);
    }

    @Test
    public void testUploadVideo(){

        //1.音视频上传-本地文件上传
        //视频标题(必选)
        String title = "6 - What If I Want to Move Faster";
        //本地文件上传和文件流上传时，文件名称为上传文件绝对路径，如:/User/sample/文件名称.mp4 (必选)
        //文件名必须包含扩展名
        String fileName = "F:\\java\\pictures\\video\\6 - What If I Want to Move Faster.mp4";
        //本地文件上传
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为1M字节 */
        request.setPartSize(1 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);
/* 是否开启断点续传, 默认断点续传功能关闭。当网络不稳定或者程序崩溃时，再次发起相同上传请求，可以继续未完成的上传任务，适用于超时3000秒仍不能上传完成的大文件。
    注意: 断点续传开启后，会在上传过程中将上传位置写入本地磁盘文件，影响文件上传速度，请您根据实际情况选择是否开启*/
        request.setEnableCheckpoint(false);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }

    }




}
