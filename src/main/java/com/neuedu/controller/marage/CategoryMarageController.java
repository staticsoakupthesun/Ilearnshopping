package com.neuedu.controller.marage;

import com.neuedu.common.ServerResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/marage/category/")
public class CategoryMarageController {

    /**
     * 获取品类子节点(平级)
     * 参数：
     * **/
    @RequestMapping(value = "get_category.do")
    public ServerResponse get_category(){


      return  null;
    }
}
