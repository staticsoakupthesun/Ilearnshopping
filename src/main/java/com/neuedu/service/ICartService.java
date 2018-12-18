package com.neuedu.service;

import com.neuedu.common.ServerResponse;

public interface ICartService {
  /**
   * 购物车中添加商品
   * */
  public ServerResponse add(Integer userId,Integer productId, Integer count);
  /**
   * 购物车列表
   * */
  ServerResponse list(Integer userId);
  /**
   * 更新购物车中的某个商品的数量
   * */
  ServerResponse  update(Integer userId,Integer productId,Integer count);
  /**
   * 移除购物车的某个商品
   * */
 ServerResponse delete_product(Integer userId,String productIds);
  /**
   * 取消/选中某个商品
   * **/
  ServerResponse select(Integer userId,Integer productId,Integer check);
  /**
   * 购物车中产品的数量
   * **/
 ServerResponse get_cart_product_count(Integer userId);
}
