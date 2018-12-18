package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.AddressMapper;
import com.neuedu.pojo.Address;
import com.neuedu.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class IAddressServiceImpl implements IAddressService {

    @Autowired
    AddressMapper addressMapper;
    /**
     * 添加收货地址
     * ***/
    @Override
    public ServerResponse add(Integer userId, Address address) {
        //step参数校验
        if (address==null){
            return ServerResponse.createServerResponseByError("参数错误");
        }
        //step2:添加
        address.setUserId(userId);
        addressMapper.insert(address);
        //step3:返回结果 gavan
        Map<String ,Integer> map=Maps.newHashMap();
        map.put("shippingId",address.getId());
        return ServerResponse.createServerResponseBySucess(null,map);
    }
    /**
     * 删除收货地址
     * ***/
    @Override
    public ServerResponse del(Integer userId, Integer shippingId) {
        //非空校验
        if (shippingId == null) {
            return ServerResponse.createServerResponseByError("参数错误");
        }
        //删除
        int result = addressMapper.deleteByUserIdAndShipping(userId, shippingId);
        //返回结果
        if (result > 0) {
            return ServerResponse.createServerResponseBySucess("删除成功");
        }
        return ServerResponse.createServerResponseByError("删除失败");
    }
    /**
     * 登录状态下更新地址
     * ***/
    @Override
    public ServerResponse update(Address address) {
        //非空判断
        if (address == null) {
            return ServerResponse.createServerResponseByError("参数错误");
        }
        //更新
       int result=  addressMapper.updateBySelectiveKey(address);
        //返回结果
        if (result > 0) {
            return ServerResponse.createServerResponseBySucess("更新成功");
        }
        return ServerResponse.createServerResponseByError("更新失败");
    }

    /**
     * 查看具体地址
     * ***/
    @Override
    public ServerResponse select(Integer shippingId) {
        //校验
        if (shippingId == null) {
            return ServerResponse.createServerResponseByError("参数错误");
        }
        //查询
        Address address=addressMapper.selectByPrimaryKey(shippingId);

        return ServerResponse.createServerResponseBySucess(null,address);
    }
    /**
     * 分页查询
     * **/
    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
        //查询
        PageHelper.startPage(pageNum,pageSize);
        List<Address> addressList=addressMapper.selectAll();
        PageInfo pageInfo =new PageInfo(addressList);
        return ServerResponse.createServerResponseBySucess(null,pageInfo);
    }
}

