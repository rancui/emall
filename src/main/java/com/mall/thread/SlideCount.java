package com.mall.thread;

/**
 * Created by rancui on 2017/11/21.
 */
public class SlideCount {


    public static void main(String[] args) {
        final Bussiness bussiness = new Bussiness();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=1;i<=50;i++) {
                    bussiness.sub(i);
                }

            }
        }).start();



        for(int i=1;i<=50;i++){
            bussiness.main(i);
        }



    }






}


class Bussiness{

     private static boolean shouldSub = true;
     public synchronized void sub(int i){

         if(!shouldSub){
             try {
                 this.wait();
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }

         for(int j=1;j<=10;j++){
             System.out.println(Thread.currentThread().getName()+"循环了"+i+"次，内部循环了:"+j+"次");
         }

         shouldSub = false;
         this.notify();

     }



     public synchronized void main(int i){

         if(shouldSub){
             try {
                 this.wait();
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }

         for(int j=1;j<=100;j++){
             System.out.println(Thread.currentThread().getName()+"循环了"+i+"次，内部循环了:"+j+"次");
         }

         shouldSub = true;
         this.notify();

     }




}