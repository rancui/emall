package com.mall.dao;

import com.mall.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    List<Order>   selectByUserId(Integer userId);

    Order selectByOrderNoUserId(@Param("orderNo") long orderNo, @Param("userId") Integer userId);

    Order selectByOrderNo(long orderNo);

    List<Order> selectAllOrder();

    List<Order> selectCloseOrderByStatusAndCloseTime(@Param("status") int status, @Param("closeTime")String closeTime);

    int closeOrderByOrderId(Integer orderId);

}