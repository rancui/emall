package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.pojo.Category;

import java.util.List;

/**
 * Created by rancui on 2017/10/11.
 */
public interface ICategoryService {
    ServerResponse<Category> addCategory(String categoryName, Integer parentId);
    ServerResponse updateCategoryName(Integer categoryId, String categoryName);
    ServerResponse<List<Category>> getChildParallelCategory(Integer parentId);
    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
