package com.neuedu.service;
/**
 * 增加节点
 * **/
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Category;

public interface ICategoryServive {

    /**
     *获取品类的子节点（平级)
     * **/
    ServerResponse get_category(Integer categoryId);

    /**
     *增加节点
     *  **/
    ServerResponse add_category(Integer parentId,String categoryName);
    /**
     * 修改节点
     * **/
   ServerResponse set_category_name(Integer categoryId,String categoryName);

   /**
    *获取当前分类id及递归子节点categoryId
    * 关于递归算法：要有一个结束的条件否则是死循环
    * **/
   ServerResponse get_deep_category(Integer categoryId);
   /**
    * 查询商品类别的父类
    * */

}
