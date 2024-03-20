package com.liusheng.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author liusheng
 * @date 2024/3/2011:13
 * @desc TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVo {
    private Integer id;
    private Integer spec;
    private String productName;
    private BigDecimal totalPrice;
}
