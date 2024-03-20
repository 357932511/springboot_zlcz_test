package com.liusheng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liusheng.model.Product;
import com.liusheng.service.ProductService;
import com.liusheng.mapper.ProductMapper;
import org.springframework.stereotype.Service;

/**
* @author 35793
* @description 针对表【product】的数据库操作Service实现
* @createDate 2024-03-20 10:44:25
*/
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService{

}




