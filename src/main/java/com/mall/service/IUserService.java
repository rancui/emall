package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.pojo.User;

/**
 * Created by rancui on 2017/10/9.
 */
public interface IUserService {
    ServerResponse<User>  login(String username, String password);
    ServerResponse<String> register(User user);
    ServerResponse forgetPwdQuestion(String username);
    ServerResponse checkAnswer(String username, String question, String answer);
    ServerResponse forgetRestPassword(String username, String passwordNew, String forgetToken);
    ServerResponse<String> restPassword(String passwordOld, String passwordNew, User user);
    ServerResponse<User> updateUserInfo(User user);
    ServerResponse<User> getUserInfo(Integer userId);
    ServerResponse checkAdminRole(User user);

}
