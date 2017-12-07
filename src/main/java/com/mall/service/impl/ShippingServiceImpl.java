package com.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mall.common.ServerResponse;
import com.mall.dao.ShippingMapper;
import com.mall.pojo.Shipping;
import com.mall.service.IShippingService;
import com.mall.vo.ShippingVo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rancui on 2017/10/16.
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService{


    private static Logger logger = LoggerFactory.getLogger(ShippingServiceImpl.class);


        @Autowired
        private ShippingMapper shippingMapper;

    /**
     *   添加收货地址
     * @param userId 用户id
     * @param shipping  收货地址对象，包括mmall_shipping数据库中一些对应的字段
     * @return
     */
    public ServerResponse add(Integer userId, Shipping shipping){

        shipping.setUserId(userId);

        int rowCount = shippingMapper.insert(shipping);

        if(rowCount>0){
            return  ServerResponse.createBySucessMsgAndData("收货地址添加成功",shipping);
        }

        return  ServerResponse.createByErrorMessage("收货地址添加失败！");

    }

    /**
     * 删除订单地址
     * @param userId
     * @param shippingId
     * @return
     */
    public  ServerResponse<String> delete(Integer userId,Integer shippingId){

            int rowCount =  shippingMapper.deleteByUserIdAndShippingId(userId,shippingId);

            if(rowCount>0){
                return  ServerResponse.createBySucessMessage("删除成功");
            }
            return  ServerResponse.createByErrorMessage("删除失败");

    }


    /**
     * 收货地址详情
     *
     * @param shippingId 地址id
     * @return
     */
    public  ServerResponse<ShippingVo> select(Integer userId, Integer shippingId){

       Shipping shipping = shippingMapper.selectByUserIdAndShippingId(userId,shippingId);

        if(shipping!=null){

           ShippingVo shippingVo = new ShippingVo();
           shippingVo.setReceiverAddress(shipping.getReceiverAddress());
           shippingVo.setReceiverCity(shipping.getReceiverCity());
           shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
           shippingVo.setReceiverMobile(shipping.getReceiverMobile());
           shippingVo.setReceiverName(shipping.getReceiverName());
           shippingVo.setReceiverProvince(shipping.getReceiverProvince());
           shippingVo.setReceiverZip(shipping.getReceiverZip());

           return  ServerResponse.createBySucessData(shippingVo);
       }

       return  ServerResponse.createByErrorMessage("该收货地址不存在");

    }

    /**
     *  地址列表
     * @param userId
     * @return
     */

    public  ServerResponse<PageInfo> list(Integer userId,int pageNum,int pageSize){

        PageHelper.startPage(pageNum,pageSize);

        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);

        List<ShippingVo> shippingVoList = Lists.newArrayList();

        if(CollectionUtils.isNotEmpty(shippingList)){
            for(Shipping shipping:shippingList){
                ShippingVo shippingVo = new ShippingVo();
                shippingVo.setReceiverAddress(shipping.getReceiverAddress());
                shippingVo.setReceiverCity(shipping.getReceiverCity());
                shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
                shippingVo.setReceiverMobile(shipping.getReceiverMobile());
                shippingVo.setReceiverName(shipping.getReceiverName());
                shippingVo.setReceiverProvince(shipping.getReceiverProvince());
                shippingVo.setReceiverZip(shipping.getReceiverZip());
                shippingVoList.add(shippingVo);
            }


            PageInfo pageInfo = new PageInfo(shippingList);

            pageInfo.setList(shippingVoList);

            return ServerResponse.createBySucessData(pageInfo);
        }



         return  ServerResponse.createByErrorMessage("收货地址不存在");

    }





















    }
