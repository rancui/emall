package com.mall.dao;

import com.mall.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    int checkUserName(String username);

    User checkUser(@Param(value = "username") String username, @Param(value = "password") String password);

    int checkEmail(String email);

    String selectQuestionByUsername(String username);

    int getAnswerByUserNameAndQuestion(@Param(value = "username") String username, @Param(value = "question") String question, @Param(value = "answer") String answer);


    int updatePasswordByUsername(@Param(value = "username") String username, @Param(value = "password") String password);

    int checkPassword(@Param(value = "passwordOld") String passwordOld, @Param(value = "userId") Integer userId);

    int checkEmailByUserId(@Param(value = "email") String email, @Param(value = "userId") Integer userId);















}