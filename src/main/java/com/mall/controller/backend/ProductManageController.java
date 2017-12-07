package com.mall.controller.backend;

import com.google.common.collect.Maps;
import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.pojo.Product;
import com.mall.pojo.User;
import com.mall.service.IFileService;
import com.mall.service.IProductService;
import com.mall.service.IUserService;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.PropertiesUtil;
import com.mall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by rancui on 2017/10/11.
 */
@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {

    @Autowired
    private IProductService iProductService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IFileService iFileService;

    //保存或更新商品
    @RequestMapping(value = "save_update_product.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse saveOrUpdateProduct(HttpServletRequest httpServletRequest,Product product){

        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);


        if(user==null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
        }

        if(iUserService.checkAdminRole(user).isSucess()){
            //填充我们增加产品的业务逻辑
            return iProductService.saveOrUpdateProduct(product);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }

    }

    // 设置产品的销售状态（在售，已下架）
    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpServletRequest httpServletRequest, Integer productId,Integer status){
        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);

        if(user == null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
        }
        if(iUserService.checkAdminRole(user).isSucess()){
            return iProductService.setSaleStatus(productId,status);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }


    //产品详情
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpServletRequest httpServletRequest, Integer productId){
        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);

        if(user == null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());

        }
        if(iUserService.checkAdminRole(user).isSucess()){
            //填充业务
            return iProductService.manageProductDetail(productId);

        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }






    //产品列表（分页）
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse productList(HttpServletRequest httpServletRequest,Integer categoryId, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);

        if(user == null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());

        }
        if(iUserService.checkAdminRole(user).isSucess()){
            //填充业务
            return iProductService.manageProductList(categoryId,pageNum,pageSize);

        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }


    //搜索（分页）
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpServletRequest httpServletRequest,String productName, Integer productId, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);

        if(user == null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
        }
        if(iUserService.checkAdminRole(user).isSucess()){
            //填充业务
            return iProductService.manageproductSearch(productName,productId,pageNum,pageSize);

        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }


    /**
     *  上传产品图片
     * @param
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(HttpServletRequest httpServletRequest, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("当前用户尚未登录，获取不到信息");
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);

        if(user == null){
            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSucess()){

            String path = request.getSession().getServletContext().getRealPath("upload");

            String targetFileName = iFileService.upload(file,path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+"image/"+targetFileName;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return ServerResponse.createBySucessData(fileMap);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }


    /**
     * 富文本上传图片
     * @param
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map uploadRichImage(HttpServletRequest httpServletRequest, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        Map resultMap = Maps.newHashMap();
        String loginToken = CookieUtil.getLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){

            resultMap.put("success",false);
            resultMap.put("msg","用户未登录,请登录管理员");
            return resultMap;
        }

        String jsonUserStr = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(jsonUserStr,User.class);


        if(user == null){

            resultMap.put("success",false);
            resultMap.put("msg","用户未登录,请登录管理员");
            return resultMap;
        }

        if(iUserService.checkAdminRole(user).isSucess()){

            //富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }

            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }

            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+"image/"+targetFileName;

            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);
            return resultMap;

        }else{
            resultMap.put("success",false);
            resultMap.put("msg","非管理员权限，不能上传图片");
            return resultMap;
        }




    }

























































}
