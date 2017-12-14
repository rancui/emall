package com.mall.controller.backend;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by rancui on 2017/10/11.
 */
@Controller
@RequestMapping("/manage/user/")
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value="login.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse login(HttpSession session, HttpServletResponse httpServletResponse,String username, String password){

          ServerResponse response = iUserService.login(username,password);

          if(response.isSucess()){
              User user = (User)response.getData();
              if(user.getRole() == Const.Role.ROLE_ADMIN){
                  //新增redis共享cookie
                  CookieUtil.setLoginToken(httpServletResponse,session.getId());
                  RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()),Const.RedisCacheExpireTime.REDIS_SESSION_EXTIME);

                  return response;
              }else{
                  return ServerResponse.createByErrorMessage("不是管理员,无法登录");
              }
          }
          return response;

    }



}
