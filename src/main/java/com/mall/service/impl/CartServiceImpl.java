package com.mall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.dao.CartMapper;
import com.mall.dao.ProductMapper;
import com.mall.pojo.Cart;
import com.mall.pojo.Product;
import com.mall.service.ICartService;
import com.mall.util.BigDecimalUtil;
import com.mall.util.PropertiesUtil;
import com.mall.vo.CartProductVo;
import com.mall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created by rancui on 2017/10/13.
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService{

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    /**
     *  新增购物车中的商品
     * @param userId  用户id
     * @param productId 产品id
     * @param count 购物车中该商品的数量
     * @return
     */
    public ServerResponse<CartVo> addCart(Integer userId,Integer productId,Integer count){
        if(productId == null || count == null){
                      return  ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.ILLEGAL_ARGUMENT.getCode(),Const.ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        }


        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if(cart == null){
            //这个产品不在这个购物车里,需要新增一个这个产品的记录
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            cartMapper.insert(cartItem);
        }else{
            //这个产品已经在购物车里了.
            //如果产品已存在,数量相加
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }

        return this.list(userId);
    }


    public ServerResponse<CartVo> list(Integer userId){

        CartVo  cartVo = this.getCartVoLimit(userId);

        return ServerResponse.createBySucessData(cartVo);

    }

    /**
     *  购物车的VO，库存的限制
     * @param userId
     * @return
     */
    private CartVo getCartVoLimit(Integer userId){

        logger.info("===========================");
        logger.info("userId是:{}",userId);
        logger.info("===========================");
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);

        logger.info("===========================");
        logger.info("cartList:{}",cartList);
        logger.info("===========================");


        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        BigDecimal cartTotalPrice = new BigDecimal("0");

        if(CollectionUtils.isNotEmpty(cartList)){
            for(Cart cartItem : cartList){
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());

                logger.info("===========================");
                logger.info("product:{}",product);
                logger.info("===========================");


                if(product != null){
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    //判断库存
                    int buyLimitCount = 0;
                    if(product.getStock() >= cartItem.getQuantity()){
                        //库存充足的时候
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else{
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //购物车中更新有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    //计算总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }

                if(cartItem.getChecked() == Const.Cart.CHECKED){
                    //如果已经勾选,增加到整个的购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        return cartVo;
    }


    /**
     *  获取购物车中的产品，是否处于全选中的状态
     * @param userId
     * @return
     */
  public boolean getAllCheckedStatus(Integer userId){

    if(userId==null){
        return false;
    }
    return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
  }


    /**
     *  更新购物车中某产品数量
     * @param count 产品数量
     * @param productId 产品id
     * @return
     */
    public  ServerResponse<CartVo>  updateCartQuautity(Integer userId,Integer productId,Integer count){

        if(productId==null || count==null){
            return  ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.ILLEGAL_ARGUMENT.getCode(),Const.ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId,productId);

        if(cart!=null){

            cart.setQuantity(count);
        }

        cartMapper.updateByPrimaryKeySelective(cart);

        return list(userId);

    }


    /**
     * 删除购物车中的某个产品
     *
     * @param userId 用户id
     * @param productIds 产品id 集合
     * @return
     */
    public  ServerResponse<CartVo>  deleteCartProduct(Integer userId,String productIds){

        List<String> productIdList = Splitter.on(",").splitToList(productIds);

        if(CollectionUtils.isEmpty(productIdList)){
            return  ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.ILLEGAL_ARGUMENT.getCode(),Const.ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        cartMapper.deleteByUserIdProductIds(userId,productIdList);

        return list(userId);

    }


    /**
     *  勾选或不勾选购物车中的商品(全选或取消全选)
     *
     * @param userId 用户id
     * @param productId 产品id
     * @param checked 是否勾选
     * @return
     */
    public  ServerResponse<CartVo>  selectOrUnSelect(Integer userId,Integer productId,Integer checked){


        cartMapper.checkedOrUncheckedProduct(userId,productId,checked);
        return this.list(userId);

    }


    /**
     *  获取购物车中的产品总数
     * @param userId
     * @return
     */
    public  ServerResponse<Integer> getCartProductCount(Integer userId){

        int count = cartMapper.selectProductCountByUserId(userId);

        return ServerResponse.createBySucessData(count);

    }



}
