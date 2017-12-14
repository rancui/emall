package com.mall.controller.portal;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.defineAnnotation.ControllerAnotation;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.RedisShardedPoolUtil;
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
@Controller("UserController")
@RequestMapping("/user/")
@Component
public class UserController {

    @Autowired
    private IUserService iUserService;


    @RequestMapping(value="/test",method=RequestMethod.POST)
    @ResponseBody
    @ControllerAnotation(description = "测试")
    public void test(){
        System.out.println("调用controller里面的方法！！！");
    }


    //用户登录
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login (String username, String password, HttpSession session, HttpServletResponse httpServletResponse){

        ServerResponse<User> response = iUserService.login(username,password);

        if(response.isSucess()){

            //设置cookie
            CookieUtil.setLoginToken(httpServletResponse,session.getId());

            //存储到redis
            RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()),Const.RedisCacheExpireTime.REDIS_SESSION_EXTIME);

        }

        return response;
    }




    //用户登出
    @RequestMapping(value = "logOut.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> logOut (HttpServletRequest request,HttpServletResponse response){

        String loginToken = CookieUtil.getLoginToken(request);
        CookieUtil.delLoginToken(request,response);
        RedisShardedPoolUtil.del(loginToken);
        return ServerResponse.createBySucess();

    }


    //用户注册
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
//    @ControllerAnotation(description="注册用户信息")
    public ServerResponse<String> register (User user){

        return iUserService.register(user);

    }

    //获取登录用户信息
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo( HttpServletRequest httpServletRequest){


         String loginToken = CookieUtil.getLoginToken(httpServletRequest);

         if(StringUtils.isEmpty(loginToken)){
             return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
         }

         String jsonUserStr = RedisShardedPoolUtil.get(loginToken);

         User user = JsonUtil.string2Obj(jsonUserStr,User.class);

         if(user==null){
             return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
         }

         return ServerResponse.createBySucessData(user);


    }




    //忘记密码的情况下根据用户名列出问题
    @RequestMapping(value = "forget_password_questionList.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetPwdQuestionList(String username){

        return iUserService.forgetPwdQuestion(username);

    }

    //忘记密码的情况下核对问题的答案
    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        return iUserService.checkAnswer(username,question,answer);
    }

    //忘记密码的情况下重置密码
    @RequestMapping(value = "forget_rest_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken){
        return iUserService.forgetRestPassword(username,passwordNew,forgetToken);
    }



    //已登录的情况下重置密码
    @RequestMapping(value = "rest_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> restPassword(HttpServletRequest httpServletRequest,String passwordOld, String passwordNew){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisShardedPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);


        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        return iUserService.restPassword(passwordOld,passwordNew,user);
    }




    //更新用户信息
    @RequestMapping(value = "update_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateInfo(HttpServletRequest httpServletRequest,User user){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisShardedPoolUtil.get(loginToken);

        User currentUser = JsonUtil.string2Obj(jsonUserStr,User.class);

        if(currentUser==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
        }

         user.setId(currentUser.getId());
         user.setUsername(currentUser.getUsername());

         ServerResponse<User>  userServerResponse = iUserService.updateUserInfo(user);

         if(userServerResponse.isSucess()){
             userServerResponse.getData().setUsername(currentUser.getUsername()); // 因为返回来的userServerResponse中没有username
             RedisShardedPoolUtil.setEx(loginToken, JsonUtil.obj2String(userServerResponse.getData()),Const.RedisCacheExpireTime.REDIS_SESSION_EXTIME);

         }

         return userServerResponse;

    }



    //获取当前登录用户的详细信息，并强制登录（需要前端配合）
    @RequestMapping(value = "get_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getInfo(HttpServletRequest httpServletRequest){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisShardedPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);


        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        ServerResponse<User> userInfoResponse = iUserService.getUserInfo(user.getId());

        return userInfoResponse;

    }



}
