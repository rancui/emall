package com.mall.controller.backend;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.ICategoryService;
import com.mall.service.IUserService;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rancui on 2017/10/11.
 */
@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private ICategoryService iCategoryService;
    @Autowired
    private IUserService iUserService;

    //添加产品分类
    @RequestMapping(value = "add_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpServletRequest httpServletRequest, String categoryName, Integer parentId){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisShardedPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);


        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        ServerResponse roleValid = iUserService.checkAdminRole(user);

        if(roleValid.isSucess()){

            // 是管理员，添加业务逻辑

            return iCategoryService.addCategory(categoryName,parentId);

        }else {

            return ServerResponse.createByErrorMessage("非管理员权限，不能添加产品分类");
        }


    }


    //更新产品分类名称
    @RequestMapping(value="update_category_name.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateCategoryName(HttpServletRequest httpServletRequest, Integer categoryId,String categoryName){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisShardedPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);


        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        ServerResponse roleValid = iUserService.checkAdminRole(user);

        if(roleValid.isSucess()){

            // 是管理员，添加业务逻辑

            return iCategoryService.updateCategoryName(categoryId,categoryName);

        }else {

            return ServerResponse.createByErrorMessage("非管理员权限，不能添加产品分类");
        }


    }


    // 根据父级，获取子节点以及平行子节点的信息
    @RequestMapping(value = "get_child_parallel_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getChildParallelCategory(HttpServletRequest httpServletRequest, @RequestParam(value = "parentId",defaultValue = "0") Integer parentId){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisShardedPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);


        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        ServerResponse roleValid = iUserService.checkAdminRole(user);

        if(roleValid.isSucess()){

            // 是管理员，添加业务逻辑
            return iCategoryService.getChildParallelCategory(parentId);

        }else {

            return ServerResponse.createByErrorMessage("非管理员权限，不能添加产品分类");
        }


    }




    // 获取所有子节点以及子节点后代的分类
    @RequestMapping(value = "get_deep_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpServletRequest httpServletRequest,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisShardedPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);

        if(user == null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());

        }
        if(iUserService.checkAdminRole(user).isSucess()){
            //查询当前节点的id和递归子节点的id
//            0->10000->100000
            return iCategoryService.selectCategoryAndChildrenById(categoryId);

        }else{
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }













































}
