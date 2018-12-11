package com.neuedu.service.impl;

import com.google.common.collect.Sets;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryServive;
import javafx.scene.effect.SepiaTone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryServive {

    @Autowired
    CategoryMapper categoryMapper;
    /**
     * 获取品类的子节点（平级)
     * **/
    @Override
    public ServerResponse get_category(Integer categoryId) {
      //step1:非空校验
        if (categoryId==null){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //step2:根据categoryid查询类别
      Category category= categoryMapper.selectByPrimaryKey(categoryId);
        if (category==null){
            return ServerResponse.createServerResponseByError("查询类别不存在");
        }
        //step3:查询子类
        List<Category> categoryList=categoryMapper.findChildCategory(categoryId);
        //step4:返回结果
        return ServerResponse.createServerResponseBySucess(null,categoryList);
    }

    /**
     * 增加节点
     * **/
    @Override
    public ServerResponse add_category(Integer parentId, String categoryName) {
        //step1:非空校验
        if (categoryName==null || categoryName.equals("")){
            return ServerResponse.createServerResponseByError("类别名称不能为空");
        }
        //step2：添加节点  插入到数据库中
       Category category=new Category();
        category.setCname(categoryName);
        category.setParentId(parentId);
        category.setCstatus(1);
       int result= categoryMapper.insert(category);
        //step3：返回结果
        if (result>0){
           return ServerResponse.createServerResponseBySucess("添加成功");
        }
        return ServerResponse.createServerResponseByError("添加失败");
    }


    /**
     * 修改节点
     * **/
    @Override
    public ServerResponse set_category_name(Integer categoryId, String categoryName) {
        //step1:非空校验
        if (categoryId==null || categoryId.equals("")){
            return ServerResponse.createServerResponseByError("类别id不能为空");
        }
        if (categoryName==null || categoryName.equals("")){
            return ServerResponse.createServerResponseByError("类别名称不能为空");
        }
        //step2:查询根据categoryId
       Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if (category==null){
            return ServerResponse.createServerResponseByError("要修改的类别不存在");
        }
        //step3:修改
       category.setCname(categoryName);
        int result = categoryMapper.updateByPrimaryKey(category);
        //step4:返回结果
        if (result>0){
            return ServerResponse.createServerResponseBySucess("修改成功");
        }
        return ServerResponse.createServerResponseByError("修改失败");
    }
    /**
     * 递归子节点查询
     * 有一个结束的语句
     * **/

    @Override
    public ServerResponse get_deep_category(Integer categoryId) {
        //step1:参数非空校验
       if (categoryId==null){
           return ServerResponse.createServerResponseByError("参数id不能为空");
       }
        //step2：查询
        Set<Category> categorySet=Sets.newHashSet();
        categorySet= findAllChildCategory(categorySet,categoryId);
       //最后得到的是id  需要遍历
        Set<Integer> integerSet =Sets.newHashSet();
        //遍历set集合
       Iterator<Category> categoryIterator= categorySet.iterator();
       while (categoryIterator.hasNext()){
           //得到泛型
           Category category=categoryIterator.next();
           integerSet.add(category.getId());
       }
        return ServerResponse.createServerResponseBySucess(null,integerSet);
    }
   //写一个私有的set方法查询categoryId 下面的所有子节点
    //方法中的Set<Category>与形参中的Set<Category>一样的
    private Set<Category> findAllChildCategory(Set<Category> categorySet,Integer categoryId){
      //查找本节点
       Category category= categoryMapper.selectByPrimaryKey(categoryId);
       if (category!=null){
           categorySet.add(category);
       }
       //查找categoryId下的子节点(平级)
      List<Category> categoryList=  categoryMapper.findChildCategory(categoryId);
       //递归结束的条件categoryList等于null或者等于0时
       if (categoryList!=null && categoryList.size()>0){
           for (Category c:categoryList) {
               //集合中每一个节点都要调用findChildCategory方法
               findAllChildCategory(categorySet,c.getId());
           }
       }
        return categorySet;
    }


}
