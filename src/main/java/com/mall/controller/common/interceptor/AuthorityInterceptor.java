package com.mall.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.RedisPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        log.info("=================");
        log.info("preHandle");
        log.info("==============");
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        String className = handlerMethod.getBean().getClass().getSimpleName();//获取类名
        String methodName = handlerMethod.getMethod().getName();//获取方法名

        Map paramMap = httpServletRequest.getParameterMap();

        StringBuffer requestParamBuffer = new StringBuffer();

        Iterator it = paramMap.entrySet().iterator();

        while (it.hasNext()){

            Map.Entry entry = (Map.Entry)it.next();
            String mapKey = (String) entry.getKey();

            String mapValue ="";

            Object obj = entry.getValue();

            if(obj instanceof String[]){

              String[]  strs = (String[]) obj;

              mapValue = Arrays.toString(strs);

            }

            requestParamBuffer.append(mapKey).append("=").append(mapValue);

        }

        //对于拦截器中拦截manage下的login.do的处理,对于登录不拦截，直接放行
        if(StringUtils.equals(className,"UserManageController") && StringUtils.equals(methodName,"login")){
            log.info("权限拦截器拦截到请求,className:{},methodName:{}",className,methodName);//如果是拦截到登录请求，不打印参数，因为参数里面有密码，全部会打印到日志中，防止日志泄露
            return true;
        }

        User user =null;

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);
        if(StringUtils.isNotEmpty(loginToken)){
            String userStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.string2Obj(userStr,User.class);
        }


        if(user==null||(user.getRole().intValue()!= Const.Role.ROLE_ADMIN)){
            httpServletResponse.reset();
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json;UTF-8");

            PrintWriter printWriter = httpServletResponse.getWriter();

            if(user==null){ // 未登录状态

                if(StringUtils.equals(className,"ProductManageController") && StringUtils.equals(methodName,"richtext_img_upload")){

                    Map result = Maps.newHashMap();
                    result.put("success",false);
                    result.put("msg","请登录");
                    printWriter.print(JsonUtil.obj2String(result));

                }else{
                    printWriter.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截。请登录！")));
                }

            }else { // 无权限状态
                if(StringUtils.equals(className,"ProductManageController") && StringUtils.equals(methodName,"richtext_img_upload")){
                    Map result = Maps.newHashMap();
                    result.put("success",false);
                    result.put("msg","无权限操作");
                    printWriter.print(JsonUtil.obj2String(result));

                }else{
                    printWriter.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截。无权限操作！")));
                }
            }

            printWriter.flush();
            printWriter.close();

            return false;

        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        log.info("afterCompletion");
    }




}
