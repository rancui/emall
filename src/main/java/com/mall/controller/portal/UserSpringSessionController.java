package com.mall.controller.portal;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.defineAnnotation.ControllerAnotation;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by rancui on 2017/10/9.
 */
@Controller
@RequestMapping("/user/springsession/")
@Component
public class UserSpringSessionController {

    @Autowired
    private IUserService iUserService;



    //用户登录
    @RequestMapping(value = "login.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> login (String username, String password, HttpSession session, HttpServletResponse httpServletResponse){

        ServerResponse<User> response = iUserService.login(username,password);

        if(response.isSucess()){

            session.setAttribute(Const.CURRENT_USER,response.getData());

//            //设置cookie
//            CookieUtil.setLoginToken(httpServletResponse,session.getId());
//
//            //存储到redis
//            RedisPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()),Const.RedisCacheExpireTime.REDIS_SESSION_EXTIME);


        }

        return response;
    }




    //用户登出
    @RequestMapping(value = "logOut.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> logOut (HttpSession session,HttpServletRequest request,HttpServletResponse response){

//        String loginToken = CookieUtil.getLoginToken(request);
//        CookieUtil.delLoginToken(request,response);
//        RedisPoolUtil.del(loginToken);
//        return ServerResponse.createBySucess();
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySucess();

    }



    //获取登录用户信息
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session, HttpServletRequest httpServletRequest){

//
//         String loginToken = CookieUtil.getLoginToken(httpServletRequest);
//
//         if(StringUtils.isEmpty(loginToken)){
//             return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
//         }
//
//         String jsonUserStr = RedisPoolUtil.get(loginToken);
//
//         User user = JsonUtil.string2Obj(jsonUserStr,User.class);
//
//         if(user==null){
//             return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
//         }
//
//         return ServerResponse.createBySucessData(user);

          User user = (User)session.getAttribute(Const.CURRENT_USER);
          if(user==null){
             return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
          }
          return ServerResponse.createBySucessData(user);


    }






}
