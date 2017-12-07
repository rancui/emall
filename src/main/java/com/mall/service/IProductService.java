package com.mall.service;

import com.github.pagehelper.PageInfo;
import com.mall.common.ServerResponse;
import com.mall.pojo.Product;

/**
 * Created by rancui on 2017/10/11.
 */
public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);
    ServerResponse setSaleStatus(Integer productId, Integer status);
    ServerResponse manageProductDetail(Integer productId);
    ServerResponse  manageProductList(Integer categoryId, Integer pageNum, Integer pageSize);
    ServerResponse<PageInfo> manageproductSearch(String productName, Integer productId, Integer pageNum, Integer pageSize);
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy);
}
