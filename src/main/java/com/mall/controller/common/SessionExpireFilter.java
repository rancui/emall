package com.mall.controller.common;

import com.mall.common.Const;
import com.mall.pojo.User;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/** 单点登录，重置session有效期
 * Created by rancui on 2017/12/4.
 */

public class SessionExpireFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isNotEmpty(loginToken)){

            String jsonUserStr = RedisPoolUtil.get(loginToken);
            User user = JsonUtil.string2Obj(jsonUserStr,User.class);

            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExpireTime.REDIS_SESSION_EXTIME);
            }
        }


        filterChain.doFilter(servletRequest,servletResponse);


    }

    @Override
    public void destroy() {

    }
}
