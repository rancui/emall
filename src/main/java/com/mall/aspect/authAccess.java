package com.mall.aspect;//

import org.springframework.stereotype.Component;

import org.aspectj.lang.annotation.*;



/**
 * Created by rancui on 2017/10/30.
 */
@Aspect
@Component
public class authAccess {
    @Pointcut("@annotation(com.mall.defineAnnotation.ControllerAnotation)")
    private void authCheck() {//切入点签名
    }

    @Before("authCheck()")
    public void beforeMethod() {
        System.out.println("方法调用之前");
    }


    @After("authCheck()")
    public void finalMethod() {

        System.out.println("方法调用之后");
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        HttpSession session = request.getSession();
//        System.out.println( session);
    }




}
