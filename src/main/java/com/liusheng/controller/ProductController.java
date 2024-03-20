package com.liusheng.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liusheng.config.ApiResponse;
import com.liusheng.model.InventoryLedger;
import com.liusheng.model.Message;
import com.liusheng.model.Product;
import com.liusheng.model.ProductVo;
import com.liusheng.service.InventoryLedgerService;
import com.liusheng.service.MessageService;
import com.liusheng.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liusheng
 * @date 2024/3/209:54
 * @desc TODO
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final InventoryLedgerService inventoryLedgerService;
    private final MessageService messageService;

    public ProductController(@Autowired MessageService messageService, @Autowired ProductService productService, @Autowired InventoryLedgerService inventoryLedgerService) {
        this.inventoryLedgerService = inventoryLedgerService;
        this.productService = productService;
        this.messageService = messageService;
    }

    /**
     * 修改商品
     * @param product 商品
     * @return 是否成功
     */
    @PostMapping("/update")
    @ResponseBody
    public ApiResponse updateProduct(@RequestBody Product product){

        // 判断该id是否存在
        if (product.getId() == null){
            return ApiResponse.error("id不能为空");
        }
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Product::getId, product.getId())
                .eq(Product::getIsDeleted, 0);
        Product product_db = productService.lambdaQuery()
                .eq(Product::getId, product.getId())
                .eq(Product::getIsDeleted, 0)
                .one();
        if (product_db == null){
            return ApiResponse.error("id不存在");
        }
        boolean b = productService.updateById(product);
        if (!b){
            return ApiResponse.error("更新失败");
        }else{
            return ApiResponse.ok();
        }
    }

    /**
     * 删除商品
     * @param id 商品id
     * @return 是否成功
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    public ApiResponse deleteProduct(@PathVariable Integer id){
        // 判断该id是否存在
        if (id == null){
            return ApiResponse.error("id不能为空");
        }
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Product::getId, id)
                .eq(Product::getIsDeleted, 0);
        Product product_db = productService.lambdaQuery()
                .eq(Product::getId, id)
                .eq(Product::getIsDeleted, 0)
                .one();
        if (product_db == null){
            return ApiResponse.error("id不存在");
        }
        product_db.setIsDeleted(1);
        boolean b = productService.updateById(product_db);
        if (!b){
            return ApiResponse.error("删除失败");
        }else{
            return ApiResponse.ok();
        }
    }

    /**
     * 获得商品分页
     * @param productName 商品名
     * @param page 页码
     * @param size 大小
     * @return 商品分页
     */
    @PostMapping("/page")
    @ResponseBody
    public ApiResponse getProductPage(@RequestParam String productName, @RequestParam Integer page, @RequestParam Integer size){
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Product::getIsDeleted, 0)
                .like(Product::getProductName, productName)
                .orderByAsc(Product::getCreateTime);
        return ApiResponse.ok(productService.page(new Page<>(page, size), queryWrapper));
    }

    @PostMapping("/input")
    @ResponseBody
    @Transactional
    public ApiResponse inputProduct(@RequestParam Integer productId, @RequestParam Integer storeId, @RequestParam Integer productSpec){
        // 首先对商品表操作
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        Product product = productService.lambdaQuery()
                .eq(Product::getProductId, productId)
                .eq(Product::getStoreId, storeId)
                .eq(Product::getIsDeleted, 0)
                .one();
        // 先判定该门店该商品是否存在
        if (product == null){
            return ApiResponse.error("该门店没有该商品");
        }
        // 存在则对其进行入库操作
        product.setProductSpec(product.getProductSpec() + productSpec);
        boolean b = productService.updateById(product);
        if (!b){
            return ApiResponse.error("入库失败");
        }
        // 根据操作将此次行为计入台账表,如果失败就回滚数据库操作
        InventoryLedger inventoryLedger = new InventoryLedger();
        inventoryLedger.setProductId(product.getProductId());
        inventoryLedger.setStoreId(product.getStoreId());
        inventoryLedger.setQuantity(productSpec);
        inventoryLedger.setType(1);
        inventoryLedger.setLedgerTime(new Date());
        inventoryLedger.setIsDeleted(0);
        boolean b1 = inventoryLedgerService.save(inventoryLedger);
        if (!b1){
            return ApiResponse.error("入账失败");
        }
        // 返回该门店该商品的现存数量
        return ApiResponse.ok(product.getProductSpec());
    }

    /**
     * 出库
     * @param productId 商品id
     * @param storeId 门店id
     * @param productSpec 商品出库数量
     * @return 该门店该商品的现存数量
     */
    @PostMapping("/output")
    @ResponseBody
    @Transactional
    public ApiResponse outputProduct(@RequestParam Integer productId, @RequestParam Integer storeId, @RequestParam Integer productSpec){
        // 首先对商品表操作
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        Product product = productService.lambdaQuery()
                .eq(Product::getProductId, productId)
                .eq(Product::getStoreId, storeId)
                .eq(Product::getIsDeleted, 0)
                .one();
        // 先判定该门店该商品是否存在
        if (product == null){
            return ApiResponse.error("该门店没有该商品或者门店不存在");
        }
        // 存在则对其进行出库操作
        if (product.getProductSpec() < productSpec){
            return ApiResponse.error("库存不足");
        }
        product.setProductSpec(product.getProductSpec() - productSpec);
        boolean b = productService.updateById(product);
        if (!b){
            return ApiResponse.error("出库失败");
        }
        // 根据操作将此次行为计入台账表,如果失败就回滚数据库操作
        InventoryLedger inventoryLedger = new InventoryLedger();
        inventoryLedger.setProductId(product.getProductId());
        inventoryLedger.setStoreId(product.getStoreId());
        inventoryLedger.setQuantity(productSpec);
        inventoryLedger.setType(0);
        inventoryLedger.setLedgerTime(new Date());
        inventoryLedger.setProductName(product.getProductName());
        inventoryLedger.setIsDeleted(0);
        boolean b1 = inventoryLedgerService.save(inventoryLedger);
        if (!b1){
            return ApiResponse.error("入账失败");
        }

        // 生成消息
        Message message = new Message();
        message.setOutboundTime(new Date());
        // TODO 假设有门店表store，则上方的查询应关联门店表获得门店名称，放入消息正体
        message.setMessageBody("门店xxx出库了" + productSpec + "个" + product.getProductName());
        message.setIsRead(0);
        message.setPushTime(new Date());
        // mybatis-plus自带的save方法会将插入后的自增id返回并赋值给实体类
        message.setLedgerId(inventoryLedger.getId());
        // TODO 假设有用户表user，则上方的查询应关联用户表获得admin用户的id，并设置。如果admin是指用户对应的角色，则要循环生成多条。
        message.setUserId(1);
        boolean isMessage = messageService.save(message);
        if (!isMessage){
            return ApiResponse.error("消息生成失败");
        }
        // 返回该门店该商品的现存数量
        return ApiResponse.ok(product.getProductSpec());
    }

    /**
     * 获得台账分页
     * @param productName 商品名
     * @param date 时间
     * @param page 页码
     * @param size 大小
     * @return 台账分页
     */
    @PostMapping("/pageLedger")
    @ResponseBody
    public ApiResponse getLedgerPage(@RequestParam String productName,
                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                     @RequestParam Integer page,
                                     @RequestParam Integer size){
        QueryWrapper<InventoryLedger> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(InventoryLedger::getProductName, productName)
                .eq(InventoryLedger::getIsDeleted, 0)
                .gt(InventoryLedger::getLedgerTime, date);
        return ApiResponse.ok(inventoryLedgerService.page(new Page<>(page, size), queryWrapper));
    }

    /**
     * 获得商品的总量
     * @param id 商品id
     * @return 商品总量
     */
    @GetMapping("/getProductTotal/{id}")
    @ResponseBody
    public ApiResponse getProductTotal(@PathVariable Integer id){
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Product::getId, id)
                .eq(Product::getIsDeleted, 0);
        Product product = productService.lambdaQuery()
                .eq(Product::getId, id)
                .eq(Product::getIsDeleted, 0)
                .one();
        if (product == null){
            return ApiResponse.error("id不存在");
        }
        BigDecimal totalPrice = product.getProductPrice().multiply(new BigDecimal(product.getProductSpec()));
        return ApiResponse.ok(new ProductVo(product.getId(), product.getProductSpec(), product.getProductName(), totalPrice));
    }

    /**
     * 用户读取信息
     * @param userId 用户ID
     * @param messageId 信息
     * @return 是否成功
     */
    @PostMapping("/readMessage")
    @ResponseBody
    public ApiResponse readMessage(@RequestParam Integer userId, @RequestParam Integer messageId){
        Message message = messageService.lambdaQuery()
                .eq(Message::getUserId, userId)
                .eq(Message::getIsRead, 0)
                .eq(Message::getId, messageId)
                .one();
        if (message == null){
            return ApiResponse.error("参数不正确");
        }
        message.setIsRead(1);
        boolean b = messageService.updateById(message);
        if (!b){
            return ApiResponse.error("读取消息失败");
        }else{
            return ApiResponse.ok();
        }
    }

    /**
     * 获得消息分页
     * @param userId 用户ID
     * @param page  页码
     * @param size  大小
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 消息分页
     */
    @PostMapping("/pageMessage")
    @ResponseBody
    public ApiResponse pageMessage(@RequestParam Integer userId,
                                   @RequestParam Integer page,
                                   @RequestParam Integer size,
                                   @RequestParam Date startTime,
                                   @RequestParam Date endTime
    ){
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Message::getUserId, userId)
                .gt(Message::getPushTime, startTime)
                .lt(Message::getPushTime, endTime);
        return ApiResponse.ok(messageService.page(new Page<>(page, size), queryWrapper));
    }
}
