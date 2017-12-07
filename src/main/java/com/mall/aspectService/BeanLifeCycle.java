package com.mall.aspectService;//package com.mall.aspectService;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by rancui on 2017/10/31.
 */
    public class BeanLifeCycle{


    public void destroy() throws Exception {
        System.out.println( "Bean destory");
    }


    public void init() throws Exception {
        System.out.println( "Bean init");
    }
}
