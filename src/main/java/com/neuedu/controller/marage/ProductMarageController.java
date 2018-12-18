package com.neuedu.controller.marage;

import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/marage/product/")
public class ProductMarageController {

    @Autowired
    IProductService iProductService;
    /**
     * 新增OR更新产品
     * **/
    @RequestMapping(value ="save.do" )
    public ServerResponse saveOrUpdate(HttpSession session, Product product){
        //判断用户是否登录，并且有管理员权限
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null) {
            return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(), ResponseCode.USER_NOT_LOGIN.getMsg());
        }
        if (userInfo.getRole() != Const.USER_ROLE_ADMIN) {
            return ServerResponse.createServerResponseByError(ResponseCode.NO_PRIVILEGE.getStatus(), ResponseCode.NO_PRIVILEGE.getMsg());
        }
        //调用service层增加或者更新
        return iProductService.saveOrUpdate(product);
    }

    /**
     * 产品的上下架
     * **/
    @RequestMapping(value ="set_sale_status.do" )
    public ServerResponse set_sale_status(HttpSession session,Integer productId,
                                          Integer pstatus){
        //判断用户是否登录，并且有管理员权限
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null) {
            return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(), ResponseCode.USER_NOT_LOGIN.getMsg());
        }
        if (userInfo.getRole() != Const.USER_ROLE_ADMIN) {
            return ServerResponse.createServerResponseByError(ResponseCode.NO_PRIVILEGE.getStatus(), ResponseCode.NO_PRIVILEGE.getMsg());
        }
        //调用service层增加或者更新
        return iProductService.set_sale_status(productId,pstatus);
    }
      /**
        * 查看商品详情
        * **/
    @RequestMapping(value ="detail.do" )
    public ServerResponse detail(HttpSession session,Integer productId){
        //判断用户是否登录，并且有管理员权限
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null) {
            return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(), ResponseCode.USER_NOT_LOGIN.getMsg());
        }
        if (userInfo.getRole() != Const.USER_ROLE_ADMIN) {
            return ServerResponse.createServerResponseByError(ResponseCode.NO_PRIVILEGE.getStatus(), ResponseCode.NO_PRIVILEGE.getMsg());
        }
        //调用service层增加或者更新
        return iProductService.detail(productId);
    }

    /**
     * 查看商品列表
     * **/
    @RequestMapping(value ="list.do" )
    public ServerResponse list(HttpSession session,
                               @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize){
        //判断用户是否登录，并且有管理员权限
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null) {
            return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(), ResponseCode.USER_NOT_LOGIN.getMsg());
        }
        if (userInfo.getRole() != Const.USER_ROLE_ADMIN) {
            return ServerResponse.createServerResponseByError(ResponseCode.NO_PRIVILEGE.getStatus(), ResponseCode.NO_PRIVILEGE.getMsg());
        }
        //调用service层增加或者更新
        return iProductService.list(pageNum,pageSize);
    }
    /**
     * 产品搜索
     * **/
    @RequestMapping(value ="search.do" )
    public ServerResponse search(HttpSession session,
                               @RequestParam(value = "productId",required = false) Integer categoryId,
                               @RequestParam(value = "productName",required = false) String productName,
                               @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize){
        //判断用户是否登录，并且有管理员权限
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null) {
            return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(), ResponseCode.USER_NOT_LOGIN.getMsg());
        }
        if (userInfo.getRole() != Const.USER_ROLE_ADMIN) {
            return ServerResponse.createServerResponseByError(ResponseCode.NO_PRIVILEGE.getStatus(), ResponseCode.NO_PRIVILEGE.getMsg());
        }
        //调用service层增加或者更新
        return iProductService.search(categoryId,productName,pageNum,pageSize);
    }


}
