package com.neuedu.service.impl;

import com.google.common.collect.Lists;
import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CartMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICartService;
import com.neuedu.utils.BigDecimalUtils;
import com.neuedu.vo.CartProductVO;
import com.neuedu.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ICartServiceImpl implements ICartService {
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;

    /**
     * 购物车中添加商品
     * */
    @Override
    public ServerResponse add(Integer userId,Integer productId, Integer count) {
        //step1:参数非空校验
        if(productId==null || count==null){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //step2：根据productId userId 查询购物车信息
       Cart cart= cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if (cart==null){
            //添加
            Cart cart1=new Cart();
            cart1.setUserId(userId);
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            //一个默认选中状态
            cart1.setChecked(ResponseCode.CartCheckedEnum.PRODUCT_CHECKED.getCode());
            cartMapper.insert(cart1);
        }else {
            //更新
            Cart cart1=new Cart();
            cart1.setId(cart.getId());
            cart1.setProductId(productId);
            cart1.setUserId(userId);
            cart1.setQuantity(count);
            cart1.setChecked(cart.getChecked());
            cartMapper.updateByPrimaryKey(cart1);
        }
        //step3：
        CartVO cartVO=getCartVOLimit(userId);
        return ServerResponse.createServerResponseBySucess(null,cartVO);
    }
    /**
     * 购物车列表
     * */

    @Override
    public ServerResponse list(Integer userId) {


       CartVO  cartVO= getCartVOLimit(userId);
        return ServerResponse.createServerResponseBySucess(null,cartVO);
    }
    /**
     * 更新购物车中的某个商品的数量
     * */
    @Override
    public ServerResponse update(Integer userId, Integer productId, Integer count) {
        //参数判定
        if(productId==null || count==null){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        Product product =productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.createServerResponseByError("要添加的商品不存在");
        }
        //根据userId和productId查找
        Cart cart= cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if (cart!=null){
            //更新数量
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKey(cart);
        }
        //返回结果
        return ServerResponse.createServerResponseBySucess(null,getCartVOLimit(userId));
    }

    /**
     * 移除购物车的某个商品
     * */
    @Override
    public ServerResponse delete_product(Integer userId, String productIds) {
        //参数非空校验
        if(productIds==null || productIds.equals("")){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //products--->List<Integer>集合

        List<Integer> productList=Lists.newArrayList();
        //得到字符串类型的数组
       String[] productIdsArr= productIds.split(",");
        //遍历他 字符串转化成Integer类型
       if (productIdsArr!=null && productIdsArr.length>0){
           for (String productIdstr:productIdsArr) {
               Integer productId=Integer.parseInt(productIdstr);
               productList.add(productId);
           }
       }
        //调用dao
        cartMapper.deleteByUserIdAndProductIds(userId,productList);

        //返回结果
        return ServerResponse.createServerResponseBySucess(null,getCartVOLimit(userId));
    }
    /**
     * 取消/选中某个商品
     * **/
    @Override
    public ServerResponse select(Integer userId, Integer productId,Integer check) {
        //非空校验
    /*    if(productId==null){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }*/
        //dao层更新选中
       cartMapper.selectOrUselectProduct(userId,productId,check);
        //返回结果
        return ServerResponse.createServerResponseBySucess(null,getCartVOLimit(userId));
    }

    @Override
    public ServerResponse get_cart_product_count(Integer userId) {
    int count= cartMapper.get_cart_product_count(userId);
        return ServerResponse.createServerResponseBySucess(null,count);
    }

    //获取cartVO  网前端返回的高复用模型
    private CartVO getCartVOLimit(Integer userId){
        CartVO cartVO=new CartVO();
        //step1:根据userId 查询购物信息--->List<Cart>
        List<Cart> cartList=cartMapper.selectCartByUserId(userId);
        //step2:List<Cart>-->List<CartProductVO>
        List<CartProductVO> cartProductVOList =Lists.newArrayList();
        //购物车总价格 执行step3 先定义
        BigDecimal carttotalpirce = new BigDecimal("0");
        if (cartList!=null && cartList.size()>0){
            for (Cart c :cartList) {
                CartProductVO cartProductVO = new CartProductVO();
                cartProductVO.setId(c.getId());
                cartProductVO.setQuantity(c.getQuantity());
                cartProductVO.setUserId(userId);
                cartProductVO.setProductChecked(c.getChecked());
                //查询商品id
                Product product=productMapper.selectByPrimaryKey(c.getProductId());
                if (product!=null){
                    cartProductVO.setProductId(c.getProductId());
                    cartProductVO.setProductMainImage(product.getMainImage());
                    cartProductVO.setProductName(product.getPname());
                    cartProductVO.setProductPrice(product.getPrice());
                    cartProductVO.setProductStatus(product.getPstatus());
                    cartProductVO.setProductStock(product.getStock());
                    cartProductVO.setProductSubtitle(product.getSubtitle());//标题
                    int stock=product.getStock();
                    int limitProductCount=0;
                    if (stock>c.getQuantity()){
                        limitProductCount=c.getQuantity();
                        //字符串型
                        cartProductVO.setLimitQuantity("LIMIT_NUM_SUCCESS");
                    }else {//商品库存不足 购买的最大数量是库存数
                        limitProductCount=stock;
                        //更新一下购物车中商品的数量
                        Cart cart=new Cart();
                        cart.setId(cart.getId());
                        cart.setQuantity(stock);
                        cart.setProductId(cart.getProductId());
                        cart.setChecked(cart.getChecked());
                        cart.setUserId(userId);
                        cartMapper.updateByPrimaryKey(cart);
                        cartProductVO.setLimitQuantity("LIMIT_NUM_FAIL");
                    }
                    cartProductVO.setQuantity(limitProductCount);
                    //getQuantity  Integer 类型转化Double类型用Double.valueOf  总价=单价*数量
                    cartProductVO.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),Double.valueOf(cartProductVO.getQuantity())));
                    //被选中状态
                    if (cartProductVO.getProductChecked()==ResponseCode.CartCheckedEnum.PRODUCT_CHECKED.getCode()){
                        carttotalpirce= BigDecimalUtils.add(carttotalpirce.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());
                    }
                }

               cartProductVOList.add(cartProductVO);
            }
        }
        //购物车信息
        cartVO.setCartProductVOList(cartProductVOList);
        //step3:计算总价格购物车
         cartVO.setCarttotalprice(carttotalpirce);
        //step4:判断购物车是否全选
       int count =cartMapper.isCheckedAll(userId);
       if (count>0){
           cartVO.setIsallchecked(false);
       }else {
           cartVO.setIsallchecked(true);
       }

        //step5:返回结果
        return cartVO;
    }
}
