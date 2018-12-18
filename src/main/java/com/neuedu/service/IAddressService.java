package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Address;

public interface IAddressService {
    /**
     * 添加收货地址
     * ***/
    public ServerResponse add(Integer userId, Address address);
    /**
     * 删除收货地址
     * ***/
    ServerResponse del(Integer userId,Integer shippingId);
    /**
     * 登录状态下更新地址
     * ***/
    ServerResponse update(Address address);
    /**
     * 查看具体地址
     * ***/
    ServerResponse select (Integer shippingId);
    /**
     * 分页查询
     * **/
    ServerResponse list (Integer pageNum,Integer pageSize);
}
