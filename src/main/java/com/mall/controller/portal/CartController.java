package com.mall.controller.portal;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.ICartService;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by rancui on 2017/10/13.
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;




    //购物车中添加商品
    @RequestMapping(value = "add.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCart(HttpServletRequest httpServletRequest, Integer productId, Integer count){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);


        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.addCart(user.getId(),productId,count);

    }


    //购物车中产品List列表
    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse list(HttpServletRequest httpServletRequest){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);



        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.list(user.getId());

    }



    //更新购物车某个产品的数量
    @RequestMapping(value = "update.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateCartQuautity(HttpServletRequest httpServletRequest,Integer productId,Integer count){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);



        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.updateCartQuautity(user.getId(),productId,count);

    }

    //删除购物车中的某个产品
    @RequestMapping(value = "delete.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse deleteCartProduct(HttpServletRequest httpServletRequest,String productIds){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);



        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.deleteCartProduct(user.getId(),productIds);

    }




    //购物车全选
    @RequestMapping(value = "select_all.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse selectAll(HttpServletRequest httpServletRequest,Integer productId){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);



        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.CHECKED);

    }


    //购物车取消全选
    @RequestMapping(value = "un_select_all.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse unSelectAll(HttpServletRequest httpServletRequest,Integer productId){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);



        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.UN_CHECKED);

    }


    //购物车选中某个商品
    @RequestMapping(value = "select.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse select(HttpServletRequest httpServletRequest,Integer productId){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);



        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.CHECKED);

    }


    //取消选中某个商品
    @RequestMapping(value = "un_select.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse unSelect(HttpServletRequest httpServletRequest,Integer productId){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);



        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.UN_CHECKED);

    }



    //查询在购物车里的产品数量
    @RequestMapping(value = "get_cart_product_count.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getCartProductCount(HttpServletRequest httpServletRequest){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);



        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.getCartProductCount(user.getId());

    }











































































}
