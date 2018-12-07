package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;

public interface IUserService {

    /**
     * 注册接口
     * **/
    public ServerResponse register(UserInfo userInfo);
/**
 * 登录
 * */

    public ServerResponse login(String username,String upwd);
    /**
     * 检查用户名或邮箱是否有效
     **/

    public  ServerResponse check_valid(String str,String type);


    /**
     * 找回密码接口
     * **/

    //接口1：根据用户名查询密保问题  返回字符串  防止横向越权  防止不修改人的信息
    //需要服务端给客户端返回一个token 服务端保存一个token
     ServerResponse forget_get_question(String username);

    //接口2：提交问题答案
     ServerResponse forget_check_answer(String username,String question,String answer);

    //接口3：成功-->重设密码
     ServerResponse forget_reset_password(String username,String newUpwd,String forgetToken);


     //登录状态下重置密码接口
     ServerResponse reset_password(UserInfo userInfo,String passwordOld,String passwordNew);
}

