package com.neuedu.controller.marage;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

//图片上传的控制器
@Controller
@RequestMapping(value = "/marage/product/")
public class UploadController {
    @Autowired
    IProductService productService;
    /**
     * 2中发送传送方式
     * **/
    //浏览器通过get请求发送
    @RequestMapping(value = "upload",method = RequestMethod.GET)
    public String upload(){
        return "upload";
        //逻辑视图 通过前缀+逻辑视图+后缀 -->/WEB-INF/jsp/upload.jsp 加载页面
    }
    //jsp页面的请求方式是post，json格式家注解@ResponseBody  图片接受方式MultipartFile file 流都封装到实体类MultipartFile中
    @RequestMapping(value = "upload",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload2(@RequestParam(value ="upload_file",required =false)MultipartFile file){

        String path="E:\\file";
        return productService.upload(file,path);
        //逻辑视图 通过前缀+逻辑视图+后缀 -->/WEB-INF/jsp/upload.jsp 加载页面
    }

}
