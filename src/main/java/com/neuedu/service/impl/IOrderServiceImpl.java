package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.*;
import com.neuedu.pojo.*;
import com.neuedu.service.IOrderService;
import com.neuedu.utils.BigDecimalUtils;
import com.neuedu.utils.DateUtil;
import com.neuedu.utils.PropertiesUtils;
import com.neuedu.vo.AddressVO;
import com.neuedu.vo.CartOrderItemVO;
import com.neuedu.vo.OrderItemVO;
import com.neuedu.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class IOrderServiceImpl implements IOrderService {

    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductMapper productMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    AddressMapper addressMapper;
    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {
        //step1:参数非空校验
        if (shippingId==null){
            return ServerResponse.createServerResponseByError("地址参数不能为空");
        }

        //step2：userId 查询购物车已选中的商品->List<Cart>
        List<Cart> cartList=cartMapper.findCartListByUserIdAndChecked(userId);
        //step3：List<Cart> -->List<OrderItem>
       ServerResponse serverResponse= getCartOrderItem(userId,cartList);
       if (!serverResponse.isSuccess()){
           return serverResponse;
       }
        //step4:创建订单order并将其保存到数据库
        //计算订单的价格
        BigDecimal orderTotalPrice = new BigDecimal("0");
       List<OrderItem> orderItemList=(List<OrderItem>) serverResponse.getData();
        if (orderItemList==null || orderItemList.size()==0){
            return ServerResponse.createServerResponseByError("购物车为空");
        }
        orderTotalPrice=getOrderPrice(orderItemList);
       Order order=createOrder(userId,shippingId,orderTotalPrice);
       if (order==null){
           return ServerResponse.createServerResponseByError("订单创建失败");
       }
        //step5:将订单明细OrderItem保存到数据库
        for (OrderItem orderItem:orderItemList) {
            orderItem.setOrderNo(order.getOrderNo());
        }
        //orderItemList是集合 设计到mybatis批量插入
        orderItemMapper.insertBatch(orderItemList);
        //step6：扣除商品库存
       reduceProductStock(orderItemList);
        //step7:购物车中清空已下单的商品
       cleanCart(cartList);
        //step8:前台返回orderVO
       OrderVO orderVO= assemnleOrderVO(order,orderItemList,shippingId);
        return ServerResponse.createServerResponseBySucess(null,orderVO);
    }
    /**
     * 取消订单
     * **/
    @Override
    public ServerResponse cancel(Integer userId, Long orderNo) {
       if (orderNo==null){
           return ServerResponse.createServerResponseByError("参数不能为空");
       }
       //根据userId和orderNo查询订单
     Order order= orderMapper.findOrderByUserIdAndOrderNo(userId,orderNo);
       if (order==null){
           return ServerResponse.createServerResponseByError("查找信息不存在");
       }
       //判断订单并取消
       if (order.getOstatus()==ResponseCode.OrderStatusEnum.ORDER_UN_PAY.getCode()){
           return ServerResponse.createServerResponseByError("订单不可取消");
       }
       //返回结果
        order.setOstatus(ResponseCode.OrderStatusEnum.ORDER_CLOSED.getCode());
          int result= orderMapper.updateByPrimaryKey(order);
          if (result>0){
              return ServerResponse.createServerResponseBySucess("取消成功");
          }

        return ServerResponse.createServerResponseByError("取消失败");
    }


    /**
     * 获取订单的商品信息
     * ***/
    @Override
    public ServerResponse get_order_cart_product(Integer userId) {
        //查询购物车
        List<Cart> cartList= cartMapper.findCartListByUserIdAndChecked(userId);
        //List<Cart>-->List<OrderItem>
        ServerResponse serverResponse= getCartOrderItem(userId,cartList);
        if (!serverResponse.isSuccess()){
           return serverResponse;
        }
        //组装VO
        CartOrderItemVO cartOrderItemVO=new CartOrderItemVO();
        cartOrderItemVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        List<OrderItem> orderItemList=(List<OrderItem>) serverResponse.getData();
        List<OrderItemVO> orderItemVOList=Lists.newArrayList();
        if (orderItemList==null || orderItemList.size()==0){
            return ServerResponse.createServerResponseByError("购物车为空");
        }
        for (OrderItem orderItem:orderItemList) {
           orderItemVOList.add(assemnleOrderItemVO(orderItem));
        }
        cartOrderItemVO.setOrderItemVOList(orderItemVOList);
        cartOrderItemVO.setTotalPrice(getOrderPrice(orderItemList));
        //返回结果
        return ServerResponse.createServerResponseBySucess(null,cartOrderItemVO);
    }
    /**
     * 订单list
     * **/

    @Override
    public ServerResponse list(Integer userId ,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList=Lists.newArrayList();
        if (userId==null){
            //查询所有
            orderList=orderMapper.selectAll();
        }else {
            //查询当前用户
        orderList=orderMapper.findOrderByUderId(userId);
        }
        //orde=---转orderVO
        if (orderList==null || orderList.size()==0){
            return ServerResponse.createServerResponseByError("未查询到订单信息");
        }
        List<OrderVO> orderVOList=Lists.newArrayList();
        for (Order order: orderList) {
          List<OrderItem> orderItemList=orderItemMapper.findOrderItems(order.getOrderNo());
          OrderVO orderVO= assemnleOrderVO(order,orderItemList,order.getShippingId());
          orderVOList.add(orderVO);
        }
        PageInfo pageInfo=new PageInfo();

        return ServerResponse.createServerResponseBySucess(null,pageInfo);
    }
    /**
     * 订单详情detail
     * **/
    @Override
    public ServerResponse detail(Long orderNo) {
        //参数非空校验
        if (orderNo==null){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //查询订单
        Order order=orderMapper.findOrderByOrderNo(orderNo);
        if (order==null){
            return ServerResponse.createServerResponseByError("订单不存在");
        }
        //获取ordervo
        List<OrderItem> orderItemList= orderItemMapper.findOrderItems(order.getOrderNo());
       OrderVO orderVO= assemnleOrderVO(order,orderItemList,order.getShippingId());
        return ServerResponse.createServerResponseBySucess(null,orderVO);
    }

    /**
      * 创建orderVO并调用
      * ***/
    private OrderVO assemnleOrderVO(Order order,List<OrderItem> orderItemList,Integer shipiingId){

        OrderVO orderVO=new OrderVO();
        List<OrderItemVO> orderItemVOList=Lists.newArrayList();
        for (OrderItem orderItem:orderItemList) {
           OrderItemVO orderItemVO=assemnleOrderItemVO(orderItem);
           orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemList(orderItemVOList);
        orderVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        //addressMapper 查询
        Address address= addressMapper.selectByPrimaryKey(shipiingId);
        if (address!=null){
            orderVO.setShippingId(shipiingId);
            AddressVO addressVO=assemnleAddressVO(address);
            orderVO.setAddressVO(addressVO);
            orderVO.setReceiverName(address.getReceiverName());
        }
        orderVO.setStatus(order.getOstatus());
        //Status 状态 StatusDesc状态描述 在ResponseCode中遍历code枚举状态并调用
        ResponseCode.OrderStatusEnum orderStatusEnum=ResponseCode.OrderStatusEnum.codeOf(order.getOstatus());
        if (orderStatusEnum!=null){
            orderVO.setStatusDesc(orderStatusEnum.getDesc());
        }
        orderVO.setPostage(0);
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentType(order.getPaymentType());
        //类似于支付状态需要遍历
        ResponseCode.PayEnum payEnum=ResponseCode.PayEnum.codeOf(order.getPaymentType());
        if (payEnum!=null){
            orderVO.setPaymentTypeDesc(payEnum.getDesc());
        }
        orderVO.setOrderNo(order.getOrderNo());
        return orderVO;
    }
    /**
     *
     * ***/
    private AddressVO assemnleAddressVO(Address address){
        AddressVO addressVO=new AddressVO();
        addressVO.setReceiverAddress(address.getReceiverAddress());
        addressVO.setReceiverCity(address.getReceiverCity());
        addressVO.setReceiverDistrict(address.getReceiverDistrict());
        addressVO.setReceiverMobile(address.getReceiverMobile());
        addressVO.setReceiverName(address.getReceiverName());
        addressVO.setReceiverPhone(address.getReceiverPhone());
        addressVO.setReceiverPrivince(address.getReceiverPrivince());
        addressVO.setReceiverZip(address.getReceiverZip());
        return addressVO;
    }
    /**
     *
     * ***/
private OrderItemVO assemnleOrderItemVO(OrderItem orderItem){
    OrderItemVO orderItemVO=new OrderItemVO();
    if (orderItem!=null){
        orderItemVO.setQuantity(orderItem.getQuantity());
        orderItemVO.setCreateTime(DateUtil.dateToStr(orderItem.getCreateTime()));
        orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVO.setOrderNo(orderItem.getOrderNo());
        orderItemVO.setProductId(orderItem.getProductId());
        orderItemVO.setProductImage(orderItem.getProductImage());
        orderItemVO.setProductName(orderItem.getProductName());
        orderItemVO.setTotalPrice(orderItem.getTotalPrice());
    }
    return orderItemVO;
}
    /**
     * 购物车中清空已下单的商品
     * ***/
    private void cleanCart(List<Cart> cartList){
        //对应批量删除
        if (cartList!=null && cartList.size()>0) {
            cartMapper.batchDelete(cartList);
        }
    }
    /**
     * 扣库存
     * **/
    private void reduceProductStock(List<OrderItem> orderItemList){
        if (orderItemList!=null && orderItemList.size()>0){
            for (OrderItem orderItem: orderItemList) {
                Integer productId=orderItem.getProductId();
                Integer quantity=orderItem.getQuantity();
                Product product=productMapper.selectByPrimaryKey(productId);
                product.setStock(product.getStock()-quantity);
                productMapper.updateByPrimaryKey(product);
            }
        }
    }

    /***
     * 计算订单的总价格
     * **/

    private BigDecimal getOrderPrice(List<OrderItem> orderItemList){
        BigDecimal bigDecimal=new BigDecimal("0");
        for (OrderItem orderItem:orderItemList
             ) {
           bigDecimal= BigDecimalUtils.add(bigDecimal.doubleValue(),orderItem.getTotalPrice().doubleValue());

        }
        return bigDecimal;
    }

    //创建订单
    private Order createOrder (Integer userId, Integer shippingId, BigDecimal orderTotalPrice){
        Order order=new Order();
        order.setOrderNo(generateOrderNO());
        order.setUserId(userId);
        order.setShippingId(shippingId);
        order.setOstatus(ResponseCode.OrderStatusEnum.ORDER_UN_PAY.getCode());
        //订单金额
        order.setPayment(orderTotalPrice);
        order.setPostage(0);
        order.setPaymentType(ResponseCode.PayEnum.ONLINE.getCode());
        //保存订单
        int result =orderMapper.insert(order);
        if (result>0){
            return order;
        }
        return null;
    }

    //生成订单编号
    private Long generateOrderNO(){
       //利用下单时间的时间戳来表示订单编号+随机值防止多人下单失败
        return System.currentTimeMillis()+new Random().nextInt(100);

    }

    private ServerResponse getCartOrderItem(Integer userId,List<Cart> cartList){
        if (cartList==null || cartList.size()==0){
            return ServerResponse.createServerResponseByError("购物车空");
        }
        List<OrderItem> orderItemList = Lists.newArrayList();
        for (Cart cart:cartList) {
        OrderItem orderItem=new OrderItem();
        //如何转
            orderItem.setUserId(userId);
          Product product= productMapper.selectByPrimaryKey(cart.getProductId());
            if (product==null){
                return ServerResponse.createServerResponseByError("商品不存在");
            }
            if (product.getPstatus()==ResponseCode.ProductStatusEnum.PRODUCT_OFFLINE.getCode()) {
                //商品下架
                return ServerResponse.createServerResponseByError("商品下架");
            }
            if (product.getStock()<cart.getQuantity()){
                //库存不足
                return ServerResponse.createServerResponseByError("此商品库存不足");
            }
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setProductId(product.getId());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setProductName(product.getPname());
            orderItem.setTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),cart.getQuantity().doubleValue()));

            orderItemList.add(orderItem);
        }
        return ServerResponse.createServerResponseBySucess(null,orderItemList);
    }
}
