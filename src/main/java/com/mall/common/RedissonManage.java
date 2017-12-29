package com.mall.common;

import com.mall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.DirectoryNotEmptyException;

@Component
@Slf4j
public class RedissonManage {


    private static  String  redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    private static  Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));
    private static  String  redis2Ip = PropertiesUtil.getProperty("redis2.ip");
    private static  Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));


    private Redisson redisson =null;
    Config config = new Config();


    @PostConstruct
    private void init(){
        try {
            config.useSingleServer().setAddress(new StringBuilder().append(redis1Ip).append(":").append(redis1Port).toString());
            redisson = (Redisson)Redisson.create(config);
            log.info("初始化Redisson成功");
        } catch (Exception e) {
            log.info("初始化Redisson失败",e);
        }
    }


    public Redisson getRedisson(){
        return redisson;
    }





















}
