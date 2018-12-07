package com.neuedu.common;
//定义响应状态码 定义为枚举类型  多种状态码可以定义更细一些
public enum  ResponseCode {

    PARAW_EMPTY(2,"参数为空"),
    EXISTS_USERNAME(3,"用户名已经存在"),
    EXISTS_EMAIL(4,"邮箱已经存在"),
    NOT_EXISTS_USERNAME(5,"用户名不存在"),
    USER_NOT_LOGIN(6,"用户未登录")
    ;
    private int status;
    private  String msg;

    ResponseCode(){

    }

    ResponseCode(int status,String msg){
        this.status=status;
        this.msg=msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
