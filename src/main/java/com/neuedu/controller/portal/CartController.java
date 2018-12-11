package com.neuedu.controller.portal;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/portal/cart/")
public class CartController {
    //从这获取CURRENTUSER的值
    /**
     * 购物车中添加商品
     * */
    @RequestMapping(value = "add.do")
    public ServerResponse add(HttpSession session,Integer productId, Integer count){
     UserInfo userInfo=(UserInfo) session.getAttribute(Const.CURRENTUSER);
     if (userInfo==null){
         return ServerResponse.createServerResponseByError("请登录");
     }
        return null;
    }
}
