package com.neuedu.controller.portal;

import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/portal/user/")
public class UserController {

    @Autowired
    IUserService userService;
    //登录
    @RequestMapping(value = "login.do")
    public ServerResponse login(HttpSession session,String username, String upwd){
        //进行业务逻辑的处理service

      ServerResponse serverResponse= userService.login(username,upwd);
      if (serverResponse.isSuccess()){
          //k 是用户信息和vule   如果成功保存到session中
          session.setAttribute(Const.CURRENTUSER,serverResponse.getData());
      }
      return serverResponse;
    }

    //注册  多参数用数据绑定传入 属性名与传入的参数相同
    @RequestMapping(value = "register.do")
    public ServerResponse register(UserInfo userInfo){
      return userService.register(userInfo);
    }

    /**
     * 检查用户名或邮箱是否有效
     * */

    //type 类型：username  str 就是username的值
    @RequestMapping(value = "check_valid.do")
    public  ServerResponse check_valid(String str,String type){

        return userService.check_valid(str,type);
    }

    /**
     * 获取登录用户信息
     * 不需要传参
     *
     * o instanceof UserInfo o是否属于userInfo类型
     * 先登录再测试
     * */


    @RequestMapping(value = "get_user_info.do")
    public  ServerResponse get_user_info(HttpSession session){
     Object o= session.getAttribute(Const.CURRENTUSER);
     if (o!=null && o instanceof UserInfo){
         UserInfo userInfo=(UserInfo)o;
         UserInfo responseUserInfo=new UserInfo();
         responseUserInfo.setId(userInfo.getId());
         responseUserInfo.setUsername(userInfo.getUsername());
         responseUserInfo.setEmail(userInfo.getEmail());
         responseUserInfo.setCreateTime(userInfo.getCreateTime());
         responseUserInfo.setUpdateTime(userInfo.getUpdateTime());
         return ServerResponse.createServerResponseBySucess(null,responseUserInfo);
     }
     return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
    }


    /**
     *获取用户详细信息
     *
     * */
    @RequestMapping(value = "get_information.do")
    public  ServerResponse get_information(HttpSession session){
        Object o= session.getAttribute(Const.CURRENTUSER);
        if (o!=null && o instanceof UserInfo){
            UserInfo userInfo=(UserInfo)o;

            return ServerResponse.createServerResponseBySucess(null,userInfo);
        }
        return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
    }
       /**
        * 根据用户名找回密保问题
        * **/
    @RequestMapping(value = "forget_get_question.do")
    public  ServerResponse forget_get_question(String username){
        ServerResponse serverResponse=userService.forget_get_question(username);
        return serverResponse;
    }

    /**
     * 提交问题答案
     * **/
    @RequestMapping(value = "forget_check_answer.do")
    public  ServerResponse forget_check_answer(String username,String question,String answer){
        ServerResponse serverResponse=userService.forget_check_qanswer(username,question,answer);
        return serverResponse;
    }
    /**
     * 忘记密码和重置密码
     * **/
    @RequestMapping(value = "forget_reset_password.do")
    public  ServerResponse forget_reset_password(String username,String upwd,String forgetToken){
        ServerResponse serverResponse=userService.forget_reset_password(username,upwd,forgetToken);
        return serverResponse;
    }

}
