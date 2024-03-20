package com.liusheng.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liusheng
 * @date 2024/3/2010:02
 * @desc TODO
 */
public class ProductUpdateDto {
    /**
     *
     */
    private Integer id;

    /**
     * 商品ID（关联其他表中的商品）
     */
    private Integer productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品规格数量
     */
    private Integer productSpec;

    /**
     * 单个商品的价格
     */
    private BigDecimal productPrice;

    /**
     * 订单状态 (0: 未入库, 1: 库内, 2: 已出库)
     */
    private Integer productStatus;

    /**
     * 标识商品是否被删除
     */
    private Integer isDeleted;

    /**
     * 商品创建时间
     */
    private Date createTime;

    /**
     * 商品入库时间
     */
    private Date inboundTime;

    /**
     * 商品出库时间
     */
    private Date outboundTime;
}
