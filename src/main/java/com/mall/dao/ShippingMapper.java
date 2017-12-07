package com.mall.dao;

import com.mall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteByUserIdAndShippingId(@Param(value = "userId") Integer userId, @Param(value = "shippingId") Integer shippingId);

    Shipping selectByUserIdAndShippingId(@Param(value = "userId") Integer userId, @Param(value = "shippingId") Integer shippingId);

    List<Shipping> selectByUserId(Integer userId);


}