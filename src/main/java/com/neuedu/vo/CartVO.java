package com.neuedu.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 封装购物车高复用对象
 * **/
public class CartVO  implements Serializable {
  /**
   * 由三部分构成
   * cartProductVOList 购物车每一条商品信息
   *isallchecked是否全选
   * carttotalprice总价格
   * **/
 private List<CartProductVO> cartProductVOList;
 private boolean isallchecked;
 private BigDecimal carttotalprice;

    public List<CartProductVO> getCartProductVOList() {
        return cartProductVOList;
    }

    public void setCartProductVOList(List<CartProductVO> cartProductVOList) {
        this.cartProductVOList = cartProductVOList;
    }

    public boolean isIsallchecked() {
        return isallchecked;
    }

    public void setIsallchecked(boolean isallchecked) {
        this.isallchecked = isallchecked;
    }

    public BigDecimal getCarttotalprice() {
        return carttotalprice;
    }

    public void setCarttotalprice(BigDecimal carttotalprice) {
        this.carttotalprice = carttotalprice;
    }
}
