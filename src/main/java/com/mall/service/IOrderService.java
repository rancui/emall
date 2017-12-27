package com.mall.service;

import com.github.pagehelper.PageInfo;
import com.mall.common.ServerResponse;
import com.mall.vo.OrderProductVo;
import com.mall.vo.OrderVo;

/**
 * Created by rancui on 2017/10/18.
 */
public interface IOrderService {
    ServerResponse<OrderVo> createOrder(Integer userId, Integer shippingId);
    ServerResponse<OrderProductVo> getOrderProductInfo(Integer userId, long orderNo);
    ServerResponse<PageInfo> getOrderList(Integer userId, Integer pageNum, Integer pageSize);
    ServerResponse<OrderVo> getOrderDetail(Integer userId, long orderNo);
    ServerResponse<String> cancelOrder(Integer userId, long orderNo);
    ServerResponse<PageInfo> manageList(int pageNum, int pageSize);
    ServerResponse<OrderVo> manageDetail(Long orderNo);
    ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);



    ServerResponse pay(long orderNo, Integer userId, String path);


    //二期，定时关单
    void closeOrderSchedule(int hour);


}
