package com.excel.gradle.demo.controller;

import com.excel.gradle.demo.bean.Deposit;
import com.excel.gradle.demo.service.DepositService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deposits")
public class DepositController {

    private static final Logger logger = LoggerFactory.getLogger(DepositController.class);

    @Autowired
    DepositService depositService;

    @RequestMapping(method = RequestMethod.POST, value="/createObj")
    public boolean createObj(Deposit deposit) {
        logger.info("createObj start");
        depositService.createDeposit(deposit);
        logger.info("createObj End");
        return true;
    }

    @RequestMapping(method = RequestMethod.POST, value="/createObjList")
    public boolean createObjList(@RequestBody List<Deposit> depositLst) {
        logger.info("createObjList start");
        depositService.createDeposit(depositLst);
        logger.info("createObjList End");
        return true;
    }

    @RequestMapping(method = RequestMethod.GET, value="/findone/prodid/{prodid}")
    public List<Deposit> findByProdId(@PathVariable("prodid") String as_ProdId) {
        logger.info("findByProdId" + as_ProdId);
        return depositService.findByProdId(as_ProdId);

    }

    @RequestMapping(method = RequestMethod.GET, value="/findall")
    public List<Deposit> findAll() {
        logger.info("findAll");
        return depositService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value="/findProd")
    public List<Deposit> findAllProd() {
        logger.info("findAllProd");
        return depositService.findAllProdId();
    }

}
