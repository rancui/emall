package com.mall.aspectService;//package com.mall.aspectService;
//
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * Created by rancui on 2017/10/30.
 */
@Service
@Component
public class AdminCheckImpl implements AdminCheck {

    public void adminCheck(){

         System.out.println( "进入到了adminCheckImpl方法");

    }

}
