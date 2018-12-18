package com.neuedu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICategoryServive;
import com.neuedu.service.IProductService;
import com.neuedu.utils.DateUtil;
import com.neuedu.utils.PropertiesUtils;
import com.neuedu.vo.ProductDetailVO;
import com.neuedu.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class IProductServiceImpl implements IProductService {
    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    ICategoryServive categoryServive;
    /**
     * 商品添加或更新
     * */
    @Override
    public ServerResponse saveOrUpdate(Product product) {
        //step1：参数非空校验
        if (product==null){
            return ServerResponse.createServerResponseByError("参数为空");
        }

        //step2：主图的构成sub_images-->1.jpg,2.jpg,3.png
        String subImages=product.getSubImages();
        //得到一个数组
        if (subImages!=null && !subImages.equals("")){
           String[] subImageArr=subImages.split(",");
           if (subImageArr.length>0){
               //设置商品主图
               product.setMainImage(subImageArr[0]);
           }
        }
        //step3：添加或者是更新
        if (product.getId()==null){
            //添加
            int result=productMapper.insert(product);
            if (result>0){
                return ServerResponse.createServerResponseBySucess("添加成功");
            }else {
                return ServerResponse.createServerResponseByError("添加失败");
            }

        }else {
            //更新
            int result=productMapper.updateByPrimaryKey(product);
            if (result>0){
                return ServerResponse.createServerResponseBySucess("更新成功");
            }else {
                return ServerResponse.createServerResponseByError("更新失败");
            }
        }
    }
/**
 * 商品上下架
 * **/
    @Override
    public ServerResponse set_sale_status(Integer productId, Integer pstatus) {
        //step1:参数非空校验
        if (productId==null){
            return ServerResponse.createServerResponseByError("商品id不能为空");
        }
        if (pstatus==null){
            return  ServerResponse.createServerResponseByError("商品状态参数不能为空");
        }
        //step2：更新商品的状态
       Product product=new Product();
        product.setId(productId);
        product.setPstatus(pstatus);
        int result =productMapper.updateProductKeySelective(product);
        //step3：返回结果
        if (result>0){
            return ServerResponse.createServerResponseBySucess("更新成功");
        }else {
            return ServerResponse.createServerResponseByError("更新失败");
        }
    }
    /**
     *查看商品详情
     * **/

    @Override
    public ServerResponse detail(Integer productId) {
        //step1：参数非空校验
        if (productId==null){
            return ServerResponse.createServerResponseByError("商品id不能为空");
        }
        //step2：查询商品
        Product product=productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.createServerResponseByError("商品不存在");
        }
        //step3：商品转换-->productDetailVO
        ProductDetailVO productDetailVO=assembleProductDetailVO(product);
        //step4：结果返回
        return ServerResponse.createServerResponseBySucess(null,productDetailVO);
    }



    //封装时间转换，读取配置文件
    private ProductDetailVO assembleProductDetailVO(Product product){
        ProductDetailVO productDetailVO=new ProductDetailVO();
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setCreateTime(DateUtil.dateToStr(product.getCreateTime()));
        productDetailVO.setDetail(product.getDetail());
        //封装域名
        productDetailVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        productDetailVO.setName(product.getPname());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setId(product.getId());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getPstatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setUpdateTime(DateUtil.dateToStr(product.getUpdateTime()));
        Category category=categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category != null) {
            productDetailVO.setParentCategoryId(category.getParentId());
        }else {
            //默认根节点
            productDetailVO.setParentCategoryId(0);
        }
        return productDetailVO;
    }
    /**
     *后台-商品列表-分页
     * **/

    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
        //查询全部之前调用分页 不可调换，否则分页失效
        // 原因是插件实现的原理是springAOP  在sql语句执行之前加上分页的代码

        PageHelper.startPage(pageNum,pageSize);
        //step1:查询商品数据   默认的值不需要判断
        List<Product> productList=productMapper.selectAll();
        List<ProductListVO> productListVOList=Lists.newArrayList();
        if (productList!=null && productList.size()>0){
            for (Product product:productList) {
                ProductListVO productListVO=assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        //PageInfo  代表返回的对象返回到前端
        PageInfo pageInfo=new PageInfo(productListVOList);

        return ServerResponse.createServerResponseBySucess(null,pageInfo);
    }



    private ProductListVO assembleProductListVO(Product product){
        ProductListVO productListVO=new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getPname());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getPstatus());
        productListVO.setSubtitle(product.getSubtitle());
        return productListVO;
    }
    /**
     * 后台搜索商品
     * ***/

    @Override
    public ServerResponse search(Integer productId, String productName,
                                 Integer pageNum, Integer pageSize) {
        //模糊查询通过id或者name
        PageHelper.startPage(pageNum,pageSize);
        //不等于空和不等于空字符串
        if (productName!=null && !productName.equals("")){
            productName="%"+productName+"%";
        }
        List<Product> productList= productMapper.findProductByProductIdAndProductName(productId,productName);
        List<ProductListVO> productListVOList=Lists.newArrayList();
        if (productList!=null && productList.size()>0) {
            for (Product product : productList) {
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo=new PageInfo(productListVOList);
        return ServerResponse.createServerResponseBySucess(null,pageInfo);
    }
    /**
     * 图片上传
     * **/

    @Override
    public ServerResponse upload(MultipartFile file, String path) {
        //step:非空判断
        if (file==null){
            return ServerResponse.createServerResponseByError("没有图片");
        }
        //step2:获取图片的名称
        String orignalFileName= file.getOriginalFilename();
        //截取图片的扩展名，从.开始会把.街去掉
        String exName= orignalFileName.substring(orignalFileName.lastIndexOf("."));
        //为图片生成新的唯一的名字
        String newFileName=UUID.randomUUID().toString()+exName;
        File pathFile =new File(path);
        //path如果不存在
        if (!pathFile.exists()){
            //设置可写 并生成目录
            pathFile.setWritable(true);
            pathFile.mkdirs();
        }
        //如果 存在，将文件写到file1目录下面
        File file1=new File(path,newFileName);
        try {
            file.transferTo(file1);
            //上传到图片服务器
            Map<String,String> map=Maps.newHashMap();
            map.put("uri",newFileName);
            map.put("url",PropertiesUtils.readByKey("imageHost")+"/"+newFileName);
            return ServerResponse.createServerResponseBySucess(null,map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     *前台-查看商品详情
     */
    @Override
    public ServerResponse detail_portal(Integer productId) {
        if (productId==null){
            return ServerResponse.createServerResponseByError("商品id不能为空");
        }
        //step2：查询商品
        Product product=productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.createServerResponseByError("商品不存在");
        }
        //step3:校验商品的状态
       if (product.getPstatus()!=ResponseCode.ProductStatusEnum.PRODUCT_ONLINE.getCode()){
           return ServerResponse.createServerResponseByError("商品下架或者删除");
       }
        //step4:获取productDetailVO
        ProductDetailVO productDetailVO=assembleProductDetailVO(product);
        //step：返回结果
        return ServerResponse.createServerResponseBySucess(null,productDetailVO);
    }
    /**
     * 前台-搜索
     * **/

    @Override
    public ServerResponse list_portal(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        //step1：categoryId与keyword不能同时为空
        if (categoryId==null && (keyword==null || keyword.equals(""))){
            return ServerResponse.createServerResponseByError("参数错误");
        }
        //step2：根据categoryId查询
        Set<Integer> integerSet=Sets.newHashSet();
        if (categoryId!=null){
            Category category=categoryMapper.selectByPrimaryKey(categoryId);
            if (category==null &&(keyword==null || keyword.equals(""))){
                //说明没有商品同时也得按照格式返回
                //进行分页
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVO> productListVOList =Lists.newArrayList();
                PageInfo pageInfo=new PageInfo(productListVOList);
                return ServerResponse.createServerResponseBySucess(null,pageInfo);
            }
            //categoryId!=null 查询类别下所有的子类 用递归 调用categoryServive
            ServerResponse serverResponse=categoryServive.get_deep_category(categoryId);

            if (serverResponse.isSuccess()){
                //如果serverResponse 成功获取到他的数据getData类别  set无序不重复
                //integerSet 类别下所有子类
                 integerSet=(Set<Integer>) serverResponse.getData();
            }

        }
        //step3：keyword查询

        if (keyword!=null && !keyword.equals("")){
            keyword="%"+keyword+"%";
        }
        //orderBy需要排序 有可能是空字符串不需要排序 否则需要排序
        if(orderBy.equals("")) {
            PageHelper.startPage(pageNum, pageSize);
        }else {
            //传参的字段名_升序/降序
           String[] orderByArr= orderBy.split("_");
           if (orderByArr.length>1){
               //orderByArr[0]+orderByArr[1]   参数含义：第一个长度，第二个升序还是降序
               PageHelper.startPage(pageNum,pageSize,orderByArr[0]+" "+orderByArr[1]);
           }else {
               PageHelper.startPage(pageNum, pageSize);
           }
        }
        //step4:List<Product>-->List<ProductListVO>
        List<Product> productList=  productMapper.searchProduct(integerSet,keyword);
        List<ProductListVO> productListVOList=Lists.newArrayList();
        if (productList !=null && productList.size()>0){
            for (Product product:productList) {
                ProductListVO productListVO=assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        //step5:分页
        PageInfo pageInfo =new PageInfo();
        pageInfo.setList(productListVOList);
        //step6:返回结果
        return ServerResponse.createServerResponseBySucess(null,pageInfo);
    }
}
