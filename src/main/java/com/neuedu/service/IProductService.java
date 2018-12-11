package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


public interface IProductService {

    /**
     * 商品添加或更新
     * */
   ServerResponse saveOrUpdate(Product product);
   /**
    * 商品上下架
    * productId 商品id
    * pstatus 商品状态
    * **/
   ServerResponse set_sale_status(Integer productId,Integer pstatus);
   /**
    *后台- 查看商品详情
    * **/
   ServerResponse detail(Integer productId);
   /**
    * 后台-商品列表-分页
    * **/
   ServerResponse list(Integer pageNum,Integer pageSize);
   /**
    * 后台搜索商品
    * ***/
   ServerResponse search(Integer productId,String productName,Integer pageNum,Integer pageSize);
   /**
    *图片上传
    * **/
   ServerResponse upload(MultipartFile file,String path);
   /**
    *前台-查看商品详情
    */

   ServerResponse detail_portal(Integer productId);
   /**
    * 前台-商品搜索
    * categoryId  类别
    * keyword 关键字
    * pageNum 第几页
    * pageSize 每页个数
    * orderBy 排序字段
    * ***/
   ServerResponse list_portal( Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);

   }
