package com.neuedu.controller.marage;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 后台用户控制器类
 * RestController返回json
 * **/
@RestController
@RequestMapping(value = "/marage/user/")
public class UserMarageController {


    @Autowired
    IUserService userService;
    /**
     * 管理员登录
     * */
    @RequestMapping(value = "login.do")
    public ServerResponse login(HttpSession session, String username, String upwd){
        //进行业务逻辑的处理service

        ServerResponse serverResponse= userService.login(username,upwd);
        if (serverResponse.isSuccess()){
            UserInfo userInfo=(UserInfo)serverResponse.getData();
            //k 是用户信息和vule   如果成功保存到session中
            //做一个权限判断如果权限等于0无权登录 1是管理员
            if (userInfo.getRole()==Const.USER_ROLE_CUSTOMER){
                return ServerResponse.createServerResponseByError("无权登录");
            }
            session.setAttribute(Const.CURRENTUSER,userInfo);
        }
        return serverResponse;
    }

}
