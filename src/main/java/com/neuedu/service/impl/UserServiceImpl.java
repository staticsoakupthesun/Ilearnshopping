package com.neuedu.service.impl;

import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import com.neuedu.utils.MD5Utils;
import com.neuedu.utils.TokenCache;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    //service层调用dao层

    @Autowired
    UserInfoMapper userInfoMapper;

    //注册
    @Override
    public ServerResponse register(UserInfo userInfo) {
        /*调用值通过ResponseCode封装好的进行调用枚举类型  这样每个错误信息都返回一个状态码*/
        //step1:参数的非空校验
        if (userInfo==null){
            return ServerResponse.createServerResponseByError(ResponseCode.PARAW_EMPTY.getStatus(),ResponseCode.PARAW_EMPTY.getMsg());
        }
        //step2:判断用户名是否已经存在  拿到用户名  int类型
       String username=userInfo.getUsername();
        //int count =userInfoMapper.checkUsername(username);
       /* if (count>0){// 用户名已存在 需要重新书写
            return ServerResponse.createServerResponseByError(ResponseCode.EXISTS_USERNAME.getStatus(),ResponseCode.EXISTS_USERNAME.getMsg());
        }*/

        ServerResponse serverResponse=check_valid(username,Const.USERNAME);
        if (!serverResponse.isSuccess()){
            return ServerResponse.createServerResponseByError(ResponseCode.EXISTS_USERNAME.getStatus(),ResponseCode.EXISTS_USERNAME.getMsg());
        }
        //step3:判断邮箱是否已经存在
       /*int result= userInfoMapper.checkEmail(userInfo.getEmail());
        if (result>0){// 说明邮箱已经存在
            return ServerResponse.createServerResponseByError(ResponseCode.EXISTS_EMAIL.getStatus(),ResponseCode.EXISTS_EMAIL.getMsg());
        }*/
        ServerResponse email_serverResponse=check_valid(userInfo.getEmail(),Const.EMAIL);
        if (!serverResponse.isSuccess()){
            return ServerResponse.createServerResponseByError(ResponseCode.EXISTS_EMAIL.getStatus(),ResponseCode.EXISTS_EMAIL.getMsg());
        }
        //step4:注册    封装角色
        userInfo.setRole(Const.USER_ROLE_CUSTOMER);
        //修改注册接口，进行数据库密码加密  把没有加密的密码重新赋值
        userInfo.setUpwd(MD5Utils.getMD5Code(userInfo.getUpwd()));
       int insert= userInfoMapper.insert(userInfo);
       if (insert>0){  //step5:返回结果
           return ServerResponse.createServerResponseBySucess("注册成功");
       }
       return ServerResponse.createServerResponseByError("注册失败");

    }




    @Override
    public ServerResponse login(String username, String upwd) {
        //step1:参数的非空校验   isBlank判断是否为空的语句  isEmpty 空格不为空
        if (StringUtils.isBlank(username)){
            return ServerResponse.createServerResponseByError("用户名不能为空");
        }
        if (StringUtils.isBlank(upwd)){
            return ServerResponse.createServerResponseByError("密码不能为空");
        }
        //step2:检查username是否存在
      /* int result = userInfoMapper.checkUsername(username);
        if (result<=0){//用户名不存在
            return ServerResponse.createServerResponseByError("用户名不存在");
        }*/
      ServerResponse serverResponse=check_valid(username,Const.USERNAME);
      if (serverResponse.isSuccess()){
          return ServerResponse.createServerResponseByError(ResponseCode.NOT_EXISTS_USERNAME.getStatus(),ResponseCode.NOT_EXISTS_USERNAME.getMsg());
      }



        //step3:根据用户名和密码查询
        //在登录之前进行密码加密，要不然提示密码错误
       UserInfo userInfo=  userInfoMapper.selectUserByUsernameupwd(username,MD5Utils.getMD5Code(upwd));
        if (userInfo==null){//密码错误
            return ServerResponse.createServerResponseByError("密码错误");
        }//step4:处理回结果并返
        userInfo.setUpwd("");
        return ServerResponse.createServerResponseBySucess(null,userInfo);
    }


    /**
     * 校验用户名和邮箱接口
     * **/
    @Override
    public ServerResponse check_valid(String str, String type) {
       // step1:参数的非空校验
        if (StringUtils.isBlank(str) || StringUtils.isBlank(type)){
           return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //step2:用户名和邮箱是否存在
        if (type.equals(Const.USERNAME)){
           int username_result=userInfoMapper.checkUsername(str);
           if (username_result>0){ //说明已经存在
               return ServerResponse.createServerResponseByError(ResponseCode.EXISTS_USERNAME.getStatus(),ResponseCode.EXISTS_USERNAME.getMsg());
           }
           return ServerResponse.createServerResponseBySucess("用户名不存在");
        }else  if (type.equals(Const.EMAIL)){
            int email_result=userInfoMapper.checkEmail(str);
            if (email_result>0){ //说明已经存在
                return ServerResponse.createServerResponseByError(ResponseCode.EXISTS_EMAIL.getStatus(),ResponseCode.EXISTS_EMAIL.getMsg());
            }
            return ServerResponse.createServerResponseBySucess("邮箱不存在");
        }
        //step3:返回结果
        return ServerResponse.createServerResponseByError("type参数传递有误");
    }

    /**
     * 找回密码接口
     * **/
    @Override
    public ServerResponse forget_get_question(String username) {
        //step1:参数校验
        if (username==null || username.equals("")){
            return ServerResponse.createServerResponseByError("用户名不能为空");
        }
        //step2:校验username去查询找回密码问题返回前端
        int result_forget= userInfoMapper.checkUsername(username);
         if (result_forget==0){
             //说明用户名不存在
             return ServerResponse.createServerResponseByError("用户名不存在");
         }
        //step3:查找密保问题
       String qusetion=userInfoMapper.selectQusetionByUsername(username);
         if (qusetion==null || qusetion.equals("")){
             return ServerResponse.createServerResponseByError("密保问题空");
         }

        //step3:校验答案-->成功 修改密码
        return ServerResponse.createServerResponseBySucess(qusetion);
    }

    /**
     * 提交问题答案
     * **/
    @Override
    public ServerResponse forget_check_answer(String username, String question, String answer) {
        //step1：参数校验
        if (username==null || username.equals("")){
            return ServerResponse.createServerResponseByError("用户名不能为空");
        }
        if (question==null || question.equals("")){
            return ServerResponse.createServerResponseByError("问题不能为空");
        }

        if (answer==null || answer.equals("")){
            return ServerResponse.createServerResponseByError("答案不能为空");
        }
        //step2：根据username，question，answer 查询
        int result= userInfoMapper.selectByUsernameQuestionAnswer(username,question,answer);
        if (result==0){
            return ServerResponse.createServerResponseByError("答案错误");
        }
        //step3：服务端生成一个token保存并将同类返回客户端
        //UUID.randomUUID().toString() 保证唯一 UUID生成的是唯一的 随机生成字符串
        String forgrtToken =UUID.randomUUID().toString();
        //用一个guava缓存保存到客户端  token保存到缓存中
        //调用TokenCache set的K值，forgrtToken放到缓存中
        TokenCache.set(username,forgrtToken);
        return ServerResponse.createServerResponseBySucess(forgrtToken);
    }

    @Override
    public ServerResponse forget_reset_password(String username, String newUpwd, String forgetToken) {
        //step1：参数校验
        if (username==null || username.equals("")){
            return ServerResponse.createServerResponseByError("用户名不能为空");
        }
        if (newUpwd==null || newUpwd.equals("")){
            return ServerResponse.createServerResponseByError("密码不能为空");
        }

        if (forgetToken==null || forgetToken.equals("")){
            return ServerResponse.createServerResponseByError("forgetToken不能为空");
        }
        //step2：token校验
       String token= TokenCache.get(username);
        if (token== null){
            return ServerResponse.createServerResponseByError("token过期");
        }
        if (!token.equals(forgetToken)){
            return ServerResponse.createServerResponseByError("token无效");
        }

        //step3：修改密码
         int result=  userInfoMapper.updateUserupwd(username,MD5Utils.getMD5Code(newUpwd));
        if (result>0){
            return ServerResponse.createServerResponseBySucess("密码修改成功");
        }
        return ServerResponse.createServerResponseByError("密码修改失败");
    }
     /**
      * 登录状态下重置密码
      * **/
    @Override
    public ServerResponse reset_password(UserInfo userInfo,String passwordOld, String passwordNew) {
       //step1：参数非空校验
        if (StringUtils.isBlank(passwordOld) || StringUtils.isBlank(passwordNew)){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //step2：校验旧密码是否正确
       UserInfo userInfoOld= userInfoMapper.selectUserByUsernameupwd(userInfo.getUsername(),MD5Utils.getMD5Code(passwordOld));
        if (userInfoOld == null){
            return ServerResponse.createServerResponseByError("旧密码错误");
        }

        //step3：修改密码
       int count= userInfoMapper.updateUserupwd(userInfo.getUsername(),MD5Utils.getMD5Code(passwordNew));
        //step4：返回结果
        if (count <=0){
            return ServerResponse.createServerResponseByError("密码修改失败");

        }
        return  ServerResponse.createServerResponseBySucess("密码修改成功");
    }
}

