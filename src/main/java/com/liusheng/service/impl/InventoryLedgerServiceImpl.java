package com.liusheng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liusheng.model.InventoryLedger;
import com.liusheng.service.InventoryLedgerService;
import com.liusheng.mapper.InventoryLedgerMapper;
import org.springframework.stereotype.Service;

/**
* @author 35793
* @description 针对表【inventory_ledger(商品进出库台账)】的数据库操作Service实现
* @createDate 2024-03-20 11:09:49
*/
@Service
public class InventoryLedgerServiceImpl extends ServiceImpl<InventoryLedgerMapper, InventoryLedger>
    implements InventoryLedgerService{

}




