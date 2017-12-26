package com.mall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.hamcrest.core.Is;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
public class FTPServerConnection {

    private String ip;
    private int port;
    private String username;
    private String password;
    private FTPClient ftpClient;


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

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public FTPServerConnection(String ip, int port, String username, String password){
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;

    }



    public static final String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    public static final int ftpPort = Integer.parseInt(PropertiesUtil.getProperty("ftp.port"));
    public static final String ftpUsername = PropertiesUtil.getProperty("ftp.user");
    public static final String ftpPassword = PropertiesUtil.getProperty("ftp.pass");
    public static final String ftpRemotePath = PropertiesUtil.getProperty("ftp.remotePath");

    /*
    *   连接FTPServer服务器
    *
    * */
    public static boolean ConnectFTPServer(String ip,int port,String username,String password){

        boolean isSuccess = false;

        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(username,password);
        } catch (IOException e) {
             log.info("ftpServer链接出错：{}",e);

        }

        return isSuccess;
    }



    public  boolean uploadFile(String remotePath,List<File> fileList) throws IOException {

        boolean isSuccess = true;
        FileInputStream fis = null;

        if(ConnectFTPServer(this.ip,this.port,this.username,this.password)){

            try {
                FTPClient ftpClient = new FTPClient();

                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setBufferSize(1024);

                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();

                for(File file:fileList){

                    fis = new FileInputStream(file);
                    ftpClient.storeFile(file.getName(),fis);
                }

            }catch (IOException e){
                log.error("上传文件出现异常：{}",e);
                isSuccess = false;
            }finally {

                ftpClient.disconnect();
                fis.close();
            }

        }

        return isSuccess;

    }




    public boolean uploadFile(List<File> fileList) throws IOException {

        FTPServerConnection ftpServerConnection = new FTPServerConnection(ftpIp,ftpPort,ftpUsername,ftpPassword);
        log.info("开始连接FTP服务器");
        boolean isSuccess = ftpServerConnection.uploadFile(ftpRemotePath,fileList);
        log.info("连接FTP服务器结束");
        return isSuccess;
    }












}
