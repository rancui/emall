package com.mall.thread;

import java.util.Random;

/**
 * Created by rancui on 2017/11/22.
 */
public class ThreadLocalDemo {

    private static  ThreadLocal<MyThreadScopeData> map = new ThreadLocal<MyThreadScopeData>();
    public static void main(String[] args) {

        for(int i=0;i<2;i++){

            new Thread(new Runnable() {
                @Override
                public void run() {

                    int num = new Random().nextInt();

                    MyThreadScopeData myThreadScopeData = MyThreadScopeData.getCurrentThreadInstance();

                    myThreadScopeData.setName("name:"+num);
                    myThreadScopeData.setAge(num);
                    map.set(myThreadScopeData);
                    new A().get();
                    new B().get();




                }
            }).start();

        }



    }


    static  class A{

       public void get(){

           MyThreadScopeData instance= MyThreadScopeData.getCurrentThreadInstance();


           System.out.println("A从线程"+Thread.currentThread().getName()+"中获取到的Name值是："+instance.getName()+",Age值是："+instance.getAge());



       }

   }


    static  class B{

        public void get(){

            MyThreadScopeData instance= MyThreadScopeData.getCurrentThreadInstance();


            System.out.println("B从线程"+Thread.currentThread().getName()+"中获取到的Name值是："+instance.getName()+",Age值是："+instance.getAge());



        }

    }



}


class MyThreadScopeData{


    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private MyThreadScopeData(){

    }



    private static  MyThreadScopeData instance=null;

    public static MyThreadScopeData getCurrentThreadInstance(){

        if(instance==null){

            instance = new MyThreadScopeData();
        }

        return instance;

    }









}