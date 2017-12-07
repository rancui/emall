package com.mall.service.impl;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.dao.UserMapper;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import com.mall.util.MD5Util;
import com.mall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by rancui on 2017/10/9.
 */

@Service("iUserService")
@Component
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public ServerResponse<User> login(String username, String password){

         int usernameCount = userMapper.checkUserName(username);

         if(usernameCount==0){
             return  ServerResponse.createByErrorMessage("用户名不存在");
         }

        String md5Password = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.checkUser(username,md5Password);

        if(user==null){
            return  ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);

        return ServerResponse.createBySucessMsgAndData("登录成功",user);


    }


    /**
     *  注册
     * @param user
     * @return
     */
    public ServerResponse<String> register(User user){

        ServerResponse usernameValid =  checkVaild(user.getUsername(),Const.USERNAME);

        if(!usernameValid.isSucess()){
            return usernameValid;
        }

        ServerResponse emailValid =  checkVaild(user.getUsername(),Const.EMAIL);

        if(!emailValid.isSucess()){
            return emailValid;
        }


        //设置权限
        user.setRole(Const.Role.ROLE_CUSTOMER);

        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int userCount = userMapper.insert(user);


        if(userCount==0){
            return  ServerResponse.createByErrorMessage("注册失败！");
        }

        return  ServerResponse.createBySucessMessage("注册成功！");

    }




    /**
     * 验证注册的用户名和邮箱的唯一性
     * @param name 名称
     * @param type 类型
     * @return
     */
    private ServerResponse checkVaild(String name, String type){

        if(StringUtils.isNotBlank(type)){

            if(Const.USERNAME.equals(type)){

                int count = userMapper.checkUserName(name);

                if(count>0){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }

            if(Const.EMAIL.equals(type)){

                int count = userMapper.checkEmail(name);

                if(count>0){
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }

            }

        }else{
            return  ServerResponse.createByErrorMessage("参数type不能为空");
        }

        return ServerResponse.createBySucessMessage("校验成功");

    }



    /**
     * 忘记密码的情况下，根据问题重设密码
     * @param username 用户名
     * @return
     */
    public ServerResponse forgetPwdQuestion(String username){

        ServerResponse usrnameValid = checkVaild(username,Const.USERNAME);

        if(usrnameValid.isSucess()){
            return  ServerResponse.createByErrorMessage("用户名不存在");
        }

        String  question = userMapper.selectQuestionByUsername(username);

        if(StringUtils.isNotBlank(question)){
            return  ServerResponse.createBySucessData(question);
        }

        return  ServerResponse.createByErrorMessage("该用户的问题不存在");

    }


    /**
     *  根据问题和答案，核实信息
     * @param username 用户名
     * @param question 问题
     * @param answer 答案
     * @return
     */

    public ServerResponse checkAnswer(String username, String question, String answer){

          int answerCount = userMapper.getAnswerByUserNameAndQuestion(username,question,answer);

          if(answerCount>0){
             String TokenId = UUID.randomUUID().toString();
//             TokenCache.setKeyValue(TokenCache.TOKEN_PREFIX+username,TokenId);

              RedisPoolUtil.setEx(Const.TOKEN_PREFIX+username,TokenId,60*60*12);
              return ServerResponse.createBySucessData(TokenId);
          }
          return ServerResponse.createByErrorMessage("问题的答案错误");
    }


    /***
     * 在忘记密码的情况下，重置密码
     * @param username 用户名
     * @param passwordNew  新密码
     * @param forgetToken  token
     * @return
     */

    public ServerResponse forgetRestPassword(String username, String passwordNew, String forgetToken){


          if(StringUtils.isBlank(forgetToken)){
              return ServerResponse.createByErrorMessage("传入的参数forgetToken有误");
          }

          ServerResponse<String> usernameValid = this.checkVaild(username,Const.USERNAME);

          if(usernameValid.isSucess()){
              return ServerResponse.createByErrorMessage("该用户不存在");
          }


          String tokenId = RedisPoolUtil.get(Const.TOKEN_PREFIX+username);

          if(StringUtils.isBlank(tokenId)){
                return  ServerResponse.createByErrorMessage("tokenId过期或无效");
          }

          if(StringUtils.equals(forgetToken,tokenId)){

              String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);

              int rowCount = userMapper.updatePasswordByUsername(username,md5Password);

              if(rowCount>0){
                    return ServerResponse.createBySucessMessage("修改密码成功");
              }

          }else{
              return  ServerResponse.createByErrorMessage("tokenId有误，请重新获取重置密码的tokenId");

          }

        return  ServerResponse.createByErrorMessage("修改密码失败");

    }


    /**
     * 已登录的情况下，修改密码
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    public ServerResponse<String> restPassword(String passwordOld, String passwordNew,User user){

         String md5Password = MD5Util.MD5EncodeUtf8(passwordOld);

         int rowCount = userMapper.checkPassword(md5Password, user.getId());
         if(rowCount==0){
              return ServerResponse.createByErrorMessage("旧密码错误");
         }

         user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));

         int updateUser = userMapper.updateByPrimaryKeySelective(user);

         if(updateUser>0){
                return ServerResponse.createBySucessMessage("更新密码成功");
         }

         return ServerResponse.createByErrorMessage("更新密码");


    }

    /**
     *  更新用户信息
     * @param user
     * @return
     */

    public ServerResponse<User> updateUserInfo(User user){

        int emailCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());

        if(emailCount>0){
            return ServerResponse.createByErrorMessage("该邮箱已存在，请更换邮箱！");
        }

        User updateUser = new User();

        updateUser.setId(user.getId());
        updateUser.setPassword(user.getPassword());
        updateUser.setEmail(user.getEmail());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setPhone(user.getPhone());

        int rowCount = userMapper.updateByPrimaryKeySelective(updateUser);

        if(rowCount==0){
            return ServerResponse.createByErrorMessage("用户信息更新失败！");
        }

        return ServerResponse.createBySucessMsgAndData("更新用户信息成功",updateUser);

    }

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    public ServerResponse<User> getUserInfo(Integer userId){

        User user = userMapper.selectByPrimaryKey(userId);

        if(user==null){
            return  ServerResponse.createByErrorMessage("该用户不存在");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySucessData(user);

    }

    /**
     * backend
     * 检查是否是管理员权限
     * @param user
     * @return
     */

    public ServerResponse checkAdminRole(User user){

      if(user!=null && user.getRole()==Const.Role.ROLE_ADMIN){

          return ServerResponse.createBySucess();

      }

      return ServerResponse.createByError();



    }















































}
