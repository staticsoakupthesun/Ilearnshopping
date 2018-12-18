package com.neuedu.common;
//定义响应状态码 定义为枚举类型  多种状态码可以定义更细一些
public enum  ResponseCode {

    PARAW_EMPTY(2,"参数为空"),
    EXISTS_USERNAME(3,"用户名已经存在"),
    EXISTS_EMAIL(4,"邮箱已经存在"),
    NOT_EXISTS_USERNAME(5,"用户名不存在"),
    USER_NOT_LOGIN(6,"用户未登录"),
    NO_PRIVILEGE(7,"无权限操作")

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

    public enum ProductStatusEnum{
        PRODUCT_ONLINE(1,"在售"),
        PRODUCT_OFFLINE(2,"下架"),
        PRODUCT_DELETE(3,"删除"),
        ;
        private int code;
        private  String desc;
        ProductStatusEnum(int code,String desc){
            this.code=code;
            this.desc=desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public enum CartCheckedEnum{
        PRODUCT_CHECKED(1,"已勾选"),
        PRODUCT_UNCHECKED(0,"未勾选"),

        ;
        private int code;
        private  String desc;
        CartCheckedEnum(int code,String desc){
            this.code=code;
            this.desc=desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    public enum OrderStatusEnum{
        ORDER_CANCELED(0,"已取消"),
        ORDER_UN_PAY(10,"未付款"),
        ORDER_PAYED(20,"已付款"),
        ORDER_SEND(40,"已发货"),
        ORDER_SUCCESS(50,"交易成功"),
        ORDER_CLOSED(60,"交易失败"),
        ;
        private int code;
        private  String desc;
        OrderStatusEnum(int code,String desc){
            this.code=code;
            this.desc=desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        //code代表状态
        public static OrderStatusEnum codeOf(Integer code){
           //遍历枚举de 方法values
            for (OrderStatusEnum orderStatusEnum:values()) {
                //getCode()枚举对象
                if (code==orderStatusEnum.getCode()){
                    return orderStatusEnum;
                }
            }
            return null;
        }
    }

    public enum PayEnum{
        ONLINE(1,"线上支付"),

        ;
        private int code;
        private  String desc;
        PayEnum(int code,String desc){
            this.code=code;
            this.desc=desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
        //code代表状态
        public static PayEnum codeOf(Integer code){
            //遍历枚举de 方法values
            for (PayEnum payEnum:values()) {
                //getCode()枚举对象
                if (code==payEnum.getCode()){
                    return payEnum;
                }
            }
            return null;
        }
    }
}
