package com.neuedu.controller.portal;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Address;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "portal/shipping/")
public class AddressController {
    @Autowired
    IAddressService addressService;
    /**
     * 添加地址
     * */
    @RequestMapping(value = "add.do")
    public ServerResponse add(HttpSession session, Address address){
      //传入对象信息通过springMVC对象绑定
      UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
      if (userInfo==null){
          return ServerResponse.createServerResponseByError("请登录");
      }
      return addressService.add(userInfo.getId(),address);
    }
    /**
     * 删除地址 根据id  防止横向越权
     * **/
    @RequestMapping(value = "del.do")
    public ServerResponse del(HttpSession session, Integer shippingId){
        //传入对象信息通过springMVC对象绑定
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError("请登录");
        }
        return addressService.del(userInfo.getId(),shippingId);
    }

    /**
     * 登录状态更新地址
     * **/

    @RequestMapping(value = "update.do")
    public ServerResponse update(HttpSession session, Address address){
        //传入对象信息通过springMVC对象绑定
        UserInfo userInfo=(UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError("请登录");
        }
        address.setUserId(userInfo.getId());
        return addressService.update(address);
    }

    /**
     * 查看具体地址
     * ***/
    @RequestMapping(value = "select.do")
    public ServerResponse select(HttpSession session, Integer shippingId){
        //传入对象信息通过springMVC对象绑定
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError("请登录");
        }
        return addressService.select (shippingId);
    }
    /**
     * 地址列表 分页
     * **/
    @RequestMapping(value = "list.do")
    public ServerResponse list(HttpSession session,
                              @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam (required = false,defaultValue = "10") Integer pageSize){
        //传入对象信息通过springMVC对象绑定
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError("请登录");
        }
        return addressService.list (pageNum,pageSize);
    }
}
