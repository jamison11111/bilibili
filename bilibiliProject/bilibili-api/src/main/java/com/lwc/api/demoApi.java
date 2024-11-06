package com.lwc.api;

import com.lwc.domain.JsonResponse;
import com.lwc.domain.Video;
import com.lwc.service.ElasticSearchService;
import com.lwc.service.demoService;
import com.lwc.service.util.FastDFSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.print.DocFlavor;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * ClassName: demoAPI
 * Description:
 *
 * @Author 林伟朝
 * @Create 2024/10/8 22:05
 */
@RestController
public class demoApi {
    @Autowired
    private demoService service;
    @Autowired
    private FastDFSUtil fastDFSUtil;
    //操作搜索引擎服务器的相关api
    @Autowired
    private ElasticSearchService elasticSearchService;

    @GetMapping("/queryTest")
    public String queryTest(Integer age){
        return service.query(age);
    }


    //文件分片的测试接口,临时文件会先存放在客户端本机，或存放在前端服务器
    @GetMapping("/slices")
    public void slices(MultipartFile file)throws Exception{
        fastDFSUtil.convertFileToSlices(file);
    }

    //以模糊查询的方法从es搜索引擎中查询搜索引擎服务器内存放的视频信息，不用token，游客也可以搜
    @GetMapping("/es-videos")
    public JsonResponse<Video> getEsVideos(@RequestParam String keyword){
        Video video=elasticSearchService.getVideo(keyword);
        return new JsonResponse<>(video);
    }

    @GetMapping("/contents")
    public JsonResponse<List<Map<String, Object>>> getContents(@RequestParam String keyword,
                                                               @RequestParam Integer pageNo,
                                                               @RequestParam Integer pageSize) throws IOException {
        List<Map<String,Object>>list=elasticSearchService.getContents(keyword,pageNo,pageSize);
        return new JsonResponse<>(list);
    }

    //删除es搜索引擎服务器内的所有数据
    @DeleteMapping("/es-datas")
    public JsonResponse<String> deleteAll(){
        elasticSearchService.deleteAllVideos();
        elasticSearchService.deleteAllUserInfos();
        return new JsonResponse<>("success");
    }





}
