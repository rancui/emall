package com.mall.task;

import com.mall.pojo.Product;
import com.mall.service.IOrderService;
import com.mall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CloseOrderTask {

    @Autowired

    private IOrderService iOrderService;

    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV1(){

        log.info("关闭订单开始");
        int hour = Integer.parseInt( PropertiesUtil.getProperty("close.order.task.time.hour","2"));
        iOrderService.closeOrderSchedule(hour);
        log.info("关闭订单结束");
    }





}
