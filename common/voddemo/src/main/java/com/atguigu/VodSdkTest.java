package com.atguigu;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
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

}
