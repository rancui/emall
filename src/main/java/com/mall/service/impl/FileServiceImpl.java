package com.mall.service.impl;

import com.google.common.collect.Lists;
import com.mall.service.IFileService;
import com.mall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by rancui
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);


    public String upload(MultipartFile file,String path){
        String fileName = file.getOriginalFilename(); // 上传的原始文件名
        //扩展名
        //abc.jpg
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName; //新文件名
        logger.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);

        File fileDir = new File(path); // 创建upload文件夹
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        logger.info("fileDir的路径是:{}",fileDir);

        File targetFile = new File(path,uploadFileName); // 路径+名称，上传的文件在tomcat的webapps/ROOT/upload文件夹下

        logger.info("targetFile:{}",targetFile);
        try {
            file.transferTo(targetFile);//文件已经上传成功了，在tomcat的webapps/ROOT/upload文件夹下

            FTPUtil.uploadFile(Lists.newArrayList(targetFile)); //再上传到ftp服务器 http://image.rancui.com

            targetFile.delete();//删除tomcat的webapps/ROOT/upload文件夹下的图片，防止过多占空间

        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return null;
        }
        //A:abc.jpg
        //B:abc.jpg
        return targetFile.getName();
    }

}
