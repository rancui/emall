package com.mall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mall.common.ServerResponse;
import com.mall.dao.CategoryMapper;
import com.mall.pojo.Category;
import com.mall.service.ICategoryService;

import com.mall.service.IUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * Created by rancui on 2017/10/11.
 */

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{


    @Autowired
    private IUserService iUserService;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     *  添加分类
     * @param categoryName 分明名称
     * @param parentId 分类id
     * @return
     */
    public ServerResponse<Category> addCategory(String categoryName, Integer parentId){

        if(parentId!=null && StringUtils.isNotBlank(categoryName)){

            Category category = new Category();

            category.setParentId(parentId);
            category.setName(categoryName);
            category.setStatus(true);

            int insertCount = categoryMapper.insert(category);

            if(insertCount>0){
                return  ServerResponse.createBySucessMsgAndData("产品分类添加成功",category);
            }

            return ServerResponse.createByErrorMessage("产品分类添加失败");

        }

        return ServerResponse.createByErrorMessage("增加产品分类的参数有误");

    }

    /**
     * 更新分类的名称
     * @param categoryId 分类id
     * @param categoryName 新类名
     * @return
     */

    public ServerResponse updateCategoryName(Integer categoryId, String categoryName){


          if(categoryId==null || StringUtils.isBlank(categoryName)){
              return ServerResponse.createByErrorMessage("更新产品分类名称的方法传入的参数有误");
          }

          Category updateCategory = new Category();

          updateCategory.setId(categoryId);
          updateCategory.setName(categoryName);

          int updateCount = categoryMapper.updateByPrimaryKeySelective(updateCategory);

          if(updateCount>0){
              return ServerResponse.createBySucessMessage("产品分类名称更新成功");
          }

          return ServerResponse.createByErrorMessage("产品分流名称更新失败");


    }


    /**
     * 根据parentId查找子节点
     * @param parentId 父类id
     * @return
     */
  public  ServerResponse<List<Category>> getChildParallelCategory(Integer parentId){

        List<Category> categoryList = categoryMapper.selectCategoryChildrenListByParentId(parentId);


        if(CollectionUtils.isEmpty(categoryList)){
            return ServerResponse.createByErrorMessage("暂未找到分类");
        }

        return  ServerResponse.createBySucessData(categoryList);

  }



    /**
     * 递归查询本节点的id及孩子节点的id
     * @param categoryId
     * @return
     */
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId){

        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);

        List<Integer> categoryIdList  = Lists.newArrayList();

        if(categoryId!=null){
            for(Category category: categorySet){
                categoryIdList.add(category.getId());
            }


        }else {

            return ServerResponse.createByErrorMessage("产品id不存在");
        }

        return ServerResponse.createBySucessData(categoryIdList);

    }


    /**
     * 递归算法，查找所有子节点
     * @param categorySet  set集合（去重）
     * @param categoryId 分类id
     * @return
     */
    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId){

        Category category = categoryMapper.selectByPrimaryKey(categoryId);

        if(category!=null){

            categorySet.add(category);

        }

        List<Category> categoryList = categoryMapper.selectCategoryChildrenListByParentId(categoryId);

        for(Category categoryItem:categoryList){

           findChildCategory(categorySet,categoryItem.getId());

        }

        return categorySet;

    }


//    /**
//     *  另一种写法，测试结果正确
//     *
//     */
//
//    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId){
//
//        Set<Integer> categoryIdSet = Sets.newHashSet();
//
//        findChildCategory(categoryIdSet,categoryId);
//
//        List<Integer> categoryIdList  = Lists.newArrayList();
//
//        if(categoryId!=null){
//
//            for(Integer cateId: categoryIdSet){
//                categoryIdList.add(cateId);
//            }
//
//        }else {
//
//            return ServerResponse.createByErrorMessage("产品id不存在");
//        }
//
//        return ServerResponse.createBySucessData(categoryIdList);
//
//    }
//    private Set<Integer> findChildCategory(Set<Integer> categoryIdSet, Integer categoryId){
//
//        List<Category> categoryList = categoryMapper.selectCategoryChildrenListByParentId(categoryId);
//
//        for(Category categoryItem:categoryList){
//
//            int cateId = categoryItem.getId();
//
//            categoryIdSet.add(cateId);
//
//            findChildCategory(categoryIdSet,cateId);
//
//        }
//
//        return categoryIdSet;
//
//    }






































}
