package com.mall.dao;

import com.mall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectByCategoryId(Integer categoryId);

    List<Product> selectByNameOrProductId(@Param(value = "productName") String productName, @Param(value = "productId") Integer productId);

    List<Product> selectByNameAndCategoryIds(@Param(value = "keyword") String keyword, @Param(value = "categoryIdList") List<Integer> categoryIdList);

    List<Product> selectByUserIdAndProductId(@Param(value = "userId") Integer userId, @Param(value = "productId") Integer productId);

    Integer selectStockByProductId(Integer id);



}