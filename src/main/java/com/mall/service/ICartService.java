package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.vo.CartVo;

/**
 * Created by rancui on 2017/10/13.
 */
public interface ICartService {


    ServerResponse<CartVo> addCart(Integer userId, Integer productId, Integer count);
    ServerResponse<CartVo> list(Integer userId);
    ServerResponse<CartVo>  updateCartQuautity(Integer userId, Integer productId, Integer count);
    ServerResponse<CartVo>  deleteCartProduct(Integer userId, String productIds);
    ServerResponse<CartVo>  selectOrUnSelect(Integer userId, Integer productId, Integer checked);
    ServerResponse<Integer> getCartProductCount(Integer userId);







}
