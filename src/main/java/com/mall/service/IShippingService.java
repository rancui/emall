package com.mall.service;

import com.github.pagehelper.PageInfo;
import com.mall.common.ServerResponse;
import com.mall.pojo.Shipping;
import com.mall.vo.ShippingVo;

/**
 * Created by rancui on 2017/10/16.
 */
public interface IShippingService {


    ServerResponse add(Integer userId, Shipping shipping);
    ServerResponse<String> delete(Integer userId, Integer shippingId);
    ServerResponse<ShippingVo> select(Integer userId, Integer shippingId);
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);



}
