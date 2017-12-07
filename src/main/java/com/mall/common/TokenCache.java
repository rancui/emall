package com.mall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by rancui on 2017/10/10.
 */
public class TokenCache {

     private static final Logger LOGGER = LoggerFactory.getLogger(TokenCache.class);

     public static final String TOKEN_PREFIX = "token_";


     public static LoadingCache<String,String> loadingCache = CacheBuilder.newBuilder().initialCapacity(100).maximumSize(100000).expireAfterAccess(24, TimeUnit.HOURS)
                                                                .build(new CacheLoader<String, String>() {
                                                                    @Override
                                                                    public String load(String s) throws Exception {
                                                                        return "null";
                                                                    }
                                                                });


     public static void setKeyValue(String key,String value){

         loadingCache.put(key,value);
     }


     public static String getValue(String key){

         String value = null;

         try {
             value = loadingCache.get(key);

             if(value.equals("null")){
                 return  null;
             }

             return value;
         } catch (ExecutionException e) {
             e.printStackTrace();
         }

         return null;


     }

}
