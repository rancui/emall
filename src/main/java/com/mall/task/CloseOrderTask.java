package com.mall.task;

import com.mall.common.Const;
import com.mall.pojo.Product;
import com.mall.service.IOrderService;
import com.mall.util.PropertiesUtil;
import com.mall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CloseOrderTask {

    @Autowired

    private IOrderService iOrderService;

//    @Scheduled(cron = "0 */1 * * * ?")
//    public void closeOrderTaskV1(){
//
//        log.info("关闭订单开始");
//        int hour = Integer.parseInt( PropertiesUtil.getProperty("close.order.task.time.hour","2"));
//        iOrderService.closeOrderSchedule(hour);
//        log.info("关闭订单结束");
//    }


    /**
     * 防死锁之分布式锁
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV2(){
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout","50000"));

        Long result = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis())+lockTimeout);

        if(result!=null && result.intValue()==1){ //如果返回值是1，代表设置成功，获取锁

            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK); //关闭订单

        }else { // 未获取到锁

            //未获取到锁，继续判断,判断时间戳,看是否可以重置获取到锁
            String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            //如果lockValue不是空,并且当前时间大于锁的有效期,说明之前的lock的时间已超时,执行getset命令.
            if(lockValueStr!=null && System.currentTimeMillis()>Long.parseLong(lockValueStr)){

                String getsetResult = RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeout));

                if(getsetResult==null || (getsetResult!=null && StringUtils.equals(getsetResult,lockValueStr))){

                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);//关闭订单

                }else{
                    log.info("未获取到锁，无法关闭订单");
                }

            }else {//当前时间小于锁的有效期,说明之前的lock的时间未超时
                log.info("未获取到锁，无法关闭订单");
            }

        }

    }










 private void  closeOrder(String lockName){

     RedisShardedPoolUtil.expire(lockName,50);//有效期50秒
     int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
     log.info("====订单关闭开始===");
     iOrderService.closeOrderSchedule(hour);
     RedisShardedPoolUtil.del(lockName);
     log.info("====订单关闭结束===");

 }















}
