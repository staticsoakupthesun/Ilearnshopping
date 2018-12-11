package com.neuedu.controller.marage;

import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.ICategoryServive;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/marage/category/")
public class CategoryMarageController {
    @Autowired
    ICategoryServive categoryServive;

    /**
     * 1：获取品类子节点(平级)
     * 参数：
     * 从session中获取用户的登录信息
     **/
    @RequestMapping(value = "get_category.do")
    public ServerResponse get_category(HttpSession session, Integer categoryId) {

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null) {
            return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(), ResponseCode.USER_NOT_LOGIN.getMsg());
        }
        if (userInfo.getRole() != Const.USER_ROLE_ADMIN) {
            return ServerResponse.createServerResponseByError(ResponseCode.NO_PRIVILEGE.getStatus(), ResponseCode.NO_PRIVILEGE.getMsg());
        }
        return categoryServive.get_category(categoryId);
    }

    /**
     * 2：增加品类子节点
     * 传值：parentId  这个节点可传可不传，所以使用@RequestParam(required = false,defaultValue = "0")
     **/
    @RequestMapping(value = "add_category.do")
    public ServerResponse add_category(HttpSession session,
                                       @RequestParam(required = false, defaultValue = "0") Integer parentId
            , String categoryName) {

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null) {
            return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(), ResponseCode.USER_NOT_LOGIN.getMsg());
        }
        if (userInfo.getRole() != Const.USER_ROLE_ADMIN) {
            return ServerResponse.createServerResponseByError(ResponseCode.NO_PRIVILEGE.getStatus(), ResponseCode.NO_PRIVILEGE.getMsg());
        }

        return categoryServive.add_category(parentId, categoryName);
    }


    /**
     * 3：修改品类子节点
     **/
    @RequestMapping(value = "set_category_name.do")
    public ServerResponse set_category_name(HttpSession session,
                                            Integer categoryId,
                                            String categoryName) {

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null) {
            return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(), ResponseCode.USER_NOT_LOGIN.getMsg());
        }
        if (userInfo.getRole() != Const.USER_ROLE_ADMIN) {
            return ServerResponse.createServerResponseByError(ResponseCode.NO_PRIVILEGE.getStatus(), ResponseCode.NO_PRIVILEGE.getMsg());
        }

        return categoryServive.set_category_name(categoryId, categoryName);
    }

    /**
     * 获取当前分类id及递归子节点categoryId
     **/

    @RequestMapping(value = "get_deep_category.do")
    public ServerResponse get_deep_category(HttpSession session,
                                            Integer categoryId) {

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null) {
            return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(), ResponseCode.USER_NOT_LOGIN.getMsg());
        }
        if (userInfo.getRole() != Const.USER_ROLE_ADMIN) {
            return ServerResponse.createServerResponseByError(ResponseCode.NO_PRIVILEGE.getStatus(), ResponseCode.NO_PRIVILEGE.getMsg());
        }

        return categoryServive.get_deep_category(categoryId);
    }
}
