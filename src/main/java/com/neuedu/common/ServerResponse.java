package com.neuedu.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//封装返回前的高复用对象
//注解含义：ServerResponse对象转化为字符串的时候非空字段不会进行转化 过滤掉
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> {

    //状态码
    private int status;
    //返回接口数据
    private T   data;
    //接口的提示信息
    private String msg;

    private ServerResponse(){}
    private ServerResponse(int status){
        this.status=status;
    }
    private ServerResponse(int status,String msg){
        this.status=status;
        this.msg=msg;
    }
    private ServerResponse(int status,String msg,T data){
        this.status=status;
        this.msg=msg;
        this.data=data;
    }

    /**
     * 判断接口是否调用成功 status是否为空
     * 注解@JsonIgnore：ServerResponse转json的时候isSuccess字段忽略掉
     * */
    @JsonIgnore
    public  boolean isSuccess(){
      return  this.status==Const.SUCCESS_CODE;
    }


    /**
    *成功  接口调用成功 状态为0
    * */
public static ServerResponse createServerResponseBySucess(){
    return new ServerResponse(Const.SUCCESS_CODE);
}
public static ServerResponse createServerResponseBySucess(String msg){
        return new ServerResponse(Const.SUCCESS_CODE,msg);
    }

    public static <T> ServerResponse createServerResponseBySucess(String msg,T data){
        return new ServerResponse(Const.SUCCESS_CODE,msg,data);
    }

/**
 * 失败  2情况
 * */
public static ServerResponse createServerResponseByError(){

    return new ServerResponse(Const.SUCCESS_ERROR);
}

    public static ServerResponse createServerResponseByError(String msg){

        return new ServerResponse(Const.SUCCESS_ERROR,msg);
    }
    //状态码自定义
    public static ServerResponse createServerResponseByError(int status){

        return new ServerResponse(status);
    }

    public static ServerResponse createServerResponseByError(int status,String msg){

        return new ServerResponse(status,msg);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
