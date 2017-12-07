package com.mall.controller.portal;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IOrderService;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by rancui on 2017/10/18.
 */


@Controller
@RequestMapping("/order/")
public class OrderController {

    @Autowired
    private IOrderService iOrderService;


    // 创建订单
    @RequestMapping(value = "create.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse create(HttpServletRequest httpServletRequest,Integer shippingId){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);


        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(), Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        return iOrderService.createOrder(user.getId(),shippingId);

    }



    // 获取订单的商品信息
    @RequestMapping(value = "get_order_cart_product.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getOrderProductInfo(HttpServletRequest httpServletRequest, Long orderNo){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);


        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(), Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        return iOrderService.getOrderProductInfo(user.getId(),orderNo);

    }



    // 订单List
    @RequestMapping(value = "get_order_list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getOrderList(HttpServletRequest httpServletRequest, @RequestParam(value="pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value="pageSize",defaultValue = "10")Integer pageSize){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);


        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(), Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        return iOrderService.getOrderList(user.getId(),pageNum,pageSize);

    }



    // 订单详情
    @RequestMapping(value = "get_order_detail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getOrderDetail(HttpServletRequest httpServletRequest, long orderNo){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);


        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(), Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        return iOrderService.getOrderDetail(user.getId(),orderNo);

    }



    // 取消订单
    @RequestMapping(value = "cancel_order.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse cancelOrder(HttpServletRequest httpServletRequest, long orderNo){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);


        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(), Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        return iOrderService.cancelOrder(user.getId(),orderNo);

    }




    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay(HttpServletRequest httpServletRequest, Long orderNo, HttpServletRequest request){
        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);

        if(user ==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(), Const.ResponseCode.NEED_LOGIN.getDesc());
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo,user.getId(),path);
    }





































}
