package com.neuedu.service;

import com.neuedu.common.ServerResponse;

import javax.servlet.http.HttpSession;

public interface IOrderService {
    /**
     * 创建订单
     * **/
    ServerResponse createOrder (Integer userId, Integer shippingId);

    /**
     * 取消订单
     * ***/
    ServerResponse cancel(Integer userId,Long orderNo);

    /**
     * 获取订单的商品信息
     * ***/
   ServerResponse get_order_cart_product(Integer userId);

   /**
    * 订单列表
    * **/
  ServerResponse list(Integer userId,Integer pageNum,Integer pageSize);
    /**
     * 订单详情
     * **/
   ServerResponse detail(Long orderNo);
}
