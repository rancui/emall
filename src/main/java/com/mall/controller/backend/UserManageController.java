package com.mall.controller.backend;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by rancui on 2017/10/11.
 */
@Controller
@RequestMapping("/manage/user/")
public class UserManageController {

    @Autowired
    private IUserService iUserService;


    public ServerResponse checkAdminRole(HttpSession session,String username, String password){

          ServerResponse response = iUserService.login(username,password);

          if(response.isSucess()){
              User user = (User)response.getData();

              if(user.getRole()==Const.Role.ROLE_ADMIN){
                  session.setAttribute(Const.CURRENT_USER,user);
                  return response;
              }else {
                  return ServerResponse.createByErrorMessage("非管理员权限，无法登录");
              }
          }
          return response;

    }



}
