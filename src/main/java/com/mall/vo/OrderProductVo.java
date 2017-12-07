package com.mall.vo;

import com.mall.pojo.OrderItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by rancui on 2017/10/20.
 */
public class OrderProductVo {
    private List<OrderItemVo> orderItemVoList;
    private String imageHost;
    private BigDecimal productTotalPrice; // 产品总价

    public List<OrderItemVo> getOrderItemVoList() {
        return orderItemVoList;
    }

    public void setOrderItemVoList(List<OrderItemVo> orderItemVoList) {
        this.orderItemVoList = orderItemVoList;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }
}
