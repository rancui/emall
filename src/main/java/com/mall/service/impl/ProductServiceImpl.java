package com.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.dao.CategoryMapper;
import com.mall.dao.ProductMapper;
import com.mall.pojo.Category;
import com.mall.pojo.Product;
import com.mall.service.ICategoryService;
import com.mall.service.IProductService;
import com.mall.util.DateTimeUtil;
import com.mall.util.PropertiesUtil;
import com.mall.vo.ProductDetailVo;
import com.mall.vo.ProductBrifVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rancui on 2017/10/11.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ICategoryService iCategoryService;


    /***
     * 添加或更新产品
     * @param product
     * @return
     */
    public ServerResponse saveOrUpdateProduct(Product product){

        if(product!=null){

            if(StringUtils.isNotBlank(product.getSubImages())){
                String[] subImages = product.getSubImages().split(",");
                if(subImages.length>0){
                    product.setMainImage(subImages[0]);
                }
            }

            if(product.getId()!=null){ // 产品已存在，属于更新行为

                int rowCount = productMapper.updateByPrimaryKey(product);
                if(rowCount>0){
                    return  ServerResponse.createBySucessMessage("保存成功");
                }
                return  ServerResponse.createByErrorMessage("保存失败");


            }else { //产品不存在，属于新增行为

                int insertCount = productMapper.insert(product);

                if(insertCount>0){
                    return  ServerResponse.createBySucessMessage("新增产品成功");
                }

                return  ServerResponse.createByErrorMessage("新增产品失败");
            }

        }

        return ServerResponse.createByErrorMessage("新增或更新产品参数不正");

    }


    /**
     * 设置产品的销售状态
     * @param productId  产品id
     * @param status  产品状态（在售，下架）
     * @return
     */
    public ServerResponse setSaleStatus(Integer productId,Integer status){

           if(productId==null || status==null){
                return  ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.ILLEGAL_ARGUMENT.getCode(),Const.ResponseCode.ILLEGAL_ARGUMENT.getDesc());
           }

           Product product = productMapper.selectByPrimaryKey(productId);
           if(product == null){
              return ServerResponse.createByErrorMessage("产品已下架或者删除");
           }
           product.setStatus(status);
           int rowCount =  productMapper.updateByPrimaryKeySelective(product);
           if(rowCount>0){
               return ServerResponse.createBySucessMessage("产品状态设置成功");
           }

           return ServerResponse.createByErrorMessage("产品状态设置失败");

    }

    /**
     * 产品的详细信息
     * @param productId 产品id
     * @return
     */
    public ServerResponse manageProductDetail(Integer productId){
        if(productId==null){
            return  ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.ILLEGAL_ARGUMENT.getCode(),Const.ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }

        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://image.rancui.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setParentCategoryId(0);//默认根节点
        }else{
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return ServerResponse.createBySucessData(productDetailVo);

    }


    /**
     * 根据分类的id，获取其下面的产品列表
     * @param categoryId 分类的id
     * @param pageNum 当前页
     * @param pageSize 当前页的记录数
     * @return
     */


   public  ServerResponse  manageProductList(Integer categoryId,Integer pageNum, Integer pageSize){
       PageHelper.startPage(pageNum,pageSize);
       List<Product> productList = productMapper.selectByCategoryId(categoryId);

       List<ProductBrifVo> productBrifVoList = Lists.newArrayList();

       for(Product product: productList){

           ProductBrifVo productBrifVo = assembleProductBrifVo(product);
           productBrifVoList.add(productBrifVo);

       }

       PageInfo pageInfo = new PageInfo(productList);

       pageInfo.setList(productBrifVoList);

       return ServerResponse.createBySucessData(pageInfo);

   }

    /**
     * 产品的简要信息（在产品列表中，一般只会有产品的简要信息，而不是详细信息）
     * @param product
     * @return
     */
    private ProductBrifVo assembleProductBrifVo(Product product){
        ProductBrifVo productListVo = new ProductBrifVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }


    /***
     *  根据产品名搜索产品
     * @param productName 产品名
     * @param pageNum   当前页
     * @param pageSize  当前页记录数
     * @return
     */

  public ServerResponse<PageInfo> manageproductSearch(String productName,Integer productId, Integer pageNum, Integer pageSize){
      PageHelper.startPage(pageNum,pageSize);

       if(StringUtils.isNotBlank(productName)){
           productName = new StringBuilder().append("%").append(productName).append("%").toString();
       }


      List<Product> productList = productMapper.selectByNameOrProductId(productName,productId);


      List<ProductBrifVo> productBrifVoList = Lists.newArrayList();


      for(Product product:productList){
          ProductBrifVo productBrifVo = assembleProductBrifVo(product);
          productBrifVoList.add(productBrifVo);
      }


      PageInfo pageInfo = new PageInfo(productList);

      pageInfo.setList(productBrifVoList);

      return  ServerResponse.createBySucessData(pageInfo);


  }


    /**
     *  根据关键词或分类id 查询
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,Integer pageNum,Integer pageSize,String orderBy){

        if(categoryId==null && StringUtils.isBlank(keyword)){

              return  ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.ILLEGAL_ARGUMENT.getCode(),Const.ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }


        List<Integer> categoryIdList = Lists.newArrayList();

        if(categoryId!=null){

            Category category = categoryMapper.selectByPrimaryKey(categoryId);

           if(category==null && StringUtils.isBlank(keyword)){// 分类不存在，关键词有没有，返回空的结果集，不报错

               PageHelper.startPage(pageNum,pageSize);

               List<ProductBrifVo> productBrifVoList = Lists.newArrayList();

               PageInfo pageInfo = new PageInfo();

               pageInfo.setList(productBrifVoList);

               return ServerResponse.createBySucessData(pageInfo);

           }
            categoryIdList = iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();
        }

        if(StringUtils.isNotBlank(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }

        PageHelper.startPage(pageNum,pageSize);

        //排序
        if(StringUtils.isNotBlank(orderBy)){

            if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){

                String[] orderArray = orderBy.split("_");

                PageHelper.orderBy(orderArray[0]+" "+orderArray[1]);
            }
        }


        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);
        List<ProductBrifVo> productBrifVoList = Lists.newArrayList();
        for(Product product:productList){
            ProductBrifVo productBrifVo = assembleProductBrifVo(product);
            productBrifVoList.add(productBrifVo);
        }

        PageInfo pageInfo = new PageInfo(productList);

        pageInfo.setList(productBrifVoList);

        return  ServerResponse.createBySucessData(pageInfo);

    }













}
