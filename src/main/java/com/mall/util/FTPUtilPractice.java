package com.mall.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
public class FTPUtilPractice {

    private static final String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static final String ftpUsername = PropertiesUtil.getProperty("ftp.user");
    private static final String ftpPassword = PropertiesUtil.getProperty("ftp.pass");




    /**
     * 连接FTP服务器
     * @param ip
     * @param port
     * @param username
     * @param password
     * @return
     */
   public static boolean connectFTPServer(String ip,int port,String username,String password){

        boolean isSuccess = false;
        FTPClient ftpClient =new FTPClient();
        try {
           ftpClient.connect(ip);
           isSuccess = ftpClient.login(username,password);
        } catch (IOException e) {
           log.error("连接FTP服务器异常",e);
        }
        return isSuccess;
   }

   public boolean uploadFile(List<File> fileList) throws IOException {

       FTPUtilPractice ftpUtilPractice = new FTPUtilPractice(ftpIp,21,ftpUsername,ftpPassword);
       log.info("开始连接ftp服务器");
       boolean isSuccess = ftpUtilPractice.uploadFile("image",fileList);
       log.info("开始连接ftp服务器,结束上传,上传结果:{}");
       return isSuccess;

   }


   public  boolean uploadFile(String remotePath, List<File> fileList) throws IOException {

       boolean isSuccess = true;
       FileInputStream fis =null;

       if(connectFTPServer(this.ip,this.port,this.username,this.password)){

            FTPClient ftpClient = new FTPClient();

           try {
               //设置上传目录
               ftpClient.changeWorkingDirectory(remotePath);
               ftpClient.setControlEncoding("UTF-8");
               ftpClient.setBufferSize(1024);

               //设置文件类型（二进制）
               ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

               // 设置被动模式,
               // FTPClient连接FTP服务的时候，Java中org.apache.commons.net.ftp.FTPClient默认使用的应该是主动模式。
               // 所谓主动模式:就是指客户端连接服务端的时候，告诉服务端，我们之间要进行通讯，数据交换。我申请开辟一个端口，专门用于我们之间的通信，也即C(client)端主动向S(Server)端发起的请求。
               // 但主动模式很可能被防火墙阻止，导致文件上传失败
               // 所谓被动模式:就是指一开始服务一起来，S端变开启一个端口告诉C端，我们之间的通讯就在这个端口下。也就C端被动的接受服务端。
               // 此模式下，上传文件会顺利
               ftpClient.enterLocalPassiveMode();

               for(File file: fileList){
                   fis = new FileInputStream(file);
                   ftpClient.storeFile(file.getName(),fis);
               }

           } catch (IOException e) {
             log.error("上传文件出现异常：{}",e);
             isSuccess = false;
           } finally {
               fis.close();
               ftpClient.disconnect();
           }

       }

       return isSuccess;

   }



    private String ip;
    private int port;
    private String username;
    private String password;
    private FTPClient ftpClient;

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }



    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public  FTPUtilPractice(String ip,int port,String username,String password){
        this.ip =ip;
        this.port = port;
        this.username = username;
        this.password = password;
    }



}
