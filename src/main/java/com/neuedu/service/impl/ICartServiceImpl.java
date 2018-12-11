package com.neuedu.service.impl;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.ICartService;
import org.springframework.stereotype.Service;

@Service
public class ICartServiceImpl implements ICartService {
    @Override
    public ServerResponse add(Integer userId,Integer productId, Integer count) {
        //step1:参数非空校验


        //step2：根据productId userId 查询购物车信息


        //step3：
        return null;
    }
}
