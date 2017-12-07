package com.mall.util;

import com.mall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rancui on 2017/11/28.
 */
@Slf4j
public class JsonUtil {

   private  static ObjectMapper objectMapper = new ObjectMapper();

   static {
       //======以下设置都是为了影响序列化=====

       //对象的所有字段全部列入
       objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
       //所有的日期格式都统一为以下格式，即yyyy-MM-dd HH:mm:ss
       objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));
       //取消默认转换timestamp形式
       objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);
       //忽略空Bean转json错误
       objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);

       //忽略 在字符串中存在，但在java对象中不存在对应的属性。防止出错。
       objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
   }


    /**
     * 对象转成字符串
     * @param obj
     * @param <T>
     * @return
     */
   public static <T> String obj2String(T obj){
       if(obj==null){
           return null;
       }

       try {
           return obj instanceof String? (String) obj:objectMapper.writeValueAsString(obj);
       } catch (Exception e) {
           log.info("对象转成字符串出错：{}",e);
          return  null;
       }

   }

    /**
     * 对象转成字符串（格式化的样式）
     * @param obj
     * @param <T>
     * @return
     */

    public static <T> String obj2StringPretty(T obj){
        if(obj==null){
            return null;
        }

        try {
            return obj instanceof String? (String) obj:objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.info("对象转字符串出错：{}",e);
            return  null;
        }

    }


    /**
     * 字符串转对象
     * @param str  字符串
     * @param clazz 要转成的对象类型
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str,Class<T> clazz){

        if(StringUtils.isEmpty(str)|| clazz==null){
            return null;
        }

        try {
            return clazz.equals(String.class)?(T)str :  objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            log.info("字符串转对象出错：{}",e);
            return null;
        }

    }

    public static <T> T string2Obj(String str, TypeReference<T> typeReference){

        if(StringUtils.isEmpty(str)|| typeReference==null){
            return null;
        }

        try {
            return (T) (typeReference.getType().equals(String.class)? str: objectMapper.readValue(str, typeReference));
        } catch (Exception e) {
            log.info("字符串转对象出错：{}",e);
            return null;
        }
    }


    public static <T> T string2Obj(String str,Class<?> collectionClass, Class<?>... elementClass){

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClass);

        try {
            return objectMapper.readValue(str,javaType);
        } catch (Exception e) {
            log.info("字符串转对象出错：{}",e);
            return null;
        }

    }


    public static void main(String[] args) {

        User u1 = new User();
        u1.setUsername("xiaoming");
        u1.setEmail("xiaoming@qq.com");

        User u2 = new User();
        u2.setUsername("xiaoqiang");
        u2.setEmail("xiaoqiang@163.com");

        List<User> userList = new ArrayList<User>();

        userList.add(u1);
        userList.add(u2);

       String userListStr = JsonUtil.obj2StringPretty(userList);

       List<User> userList2 = JsonUtil.string2Obj(userListStr,List.class,User.class);

        System.out.println("end");



    }












}
