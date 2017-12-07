package com.mall.dao;

import com.mall.pojo.Order;
import com.mall.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    void batchInsert(@Param("orderItemList") List<OrderItem> orderItemList);

    List<OrderItem> selectByUserIdOrderNo(@Param("userId") Integer userId, @Param("orderNo") long orderNo);

    List<OrderItem> selectByOrderNo(long orderNo);

    List<OrderItem> getByOrderNo(long orderNo);

    List<OrderItem> getByOrderNoUserId(@Param("orderNo") long orderNo, @Param("userId") Integer userId);












}