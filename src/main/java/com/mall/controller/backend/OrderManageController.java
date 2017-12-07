package com.mall.controller.backend;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IOrderService;
import com.mall.service.IUserService;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.RedisPoolUtil;
import com.mall.vo.OrderVo;
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
 * Created by rancui on 2017/10/23.
 */
@Controller
@RequestMapping("/manage/order/")
public class OrderManageController {


    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private IUserService iUserService;


    // 订单列表
    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse list(HttpServletRequest httpServletRequest, @RequestParam(value="pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value="pageSize",defaultValue = "10")Integer pageSize){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);



        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(), Const.ResponseCode.NEED_LOGIN.getDesc());
        }
        if(iUserService.checkAdminRole(user).isSucess()){
            //填充我们增加产品的业务逻辑
            return iOrderService.manageList(pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }

    }



    // 按订单号查询
    @RequestMapping(value = "search.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse search(HttpServletRequest httpServletRequest,long orderNo, @RequestParam(value="pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value="pageSize",defaultValue = "10")Integer pageSize){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);



        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(), Const.ResponseCode.NEED_LOGIN.getDesc());
        }
        if(iUserService.checkAdminRole(user).isSucess()){
            //填充我们增加产品的业务逻辑
            return iOrderService.manageSearch(orderNo,pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }

    }


    //订单详情
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> orderDetail(HttpServletRequest httpServletRequest, Long orderNo){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);

        if(user == null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(), Const.ResponseCode.NEED_LOGIN.getDesc());
        }
        if(iUserService.checkAdminRole(user).isSucess()){
            //填充我们增加产品的业务逻辑

            return iOrderService.manageDetail(orderNo);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }





































































}
