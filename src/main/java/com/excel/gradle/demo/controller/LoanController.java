package com.excel.gradle.demo.controller;

import com.excel.gradle.demo.bean.Loan;
import com.excel.gradle.demo.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);
    @Autowired
    LoanService loanService;

    @RequestMapping(method = RequestMethod.POST, value="/createObj")
    public boolean createObj(Loan Loan) {
        logger.info("createObj start");
        loanService.createLoan(Loan);
        logger.info("createObj End");
        return true;
    }

    @RequestMapping(method = RequestMethod.POST, value="/createObjList")
    public boolean createObjList(@RequestBody List<Loan> LoanLst) {
        logger.info("createObjList start");
        loanService.createLoan(LoanLst);
        logger.info("createObjList End");
        return true;
    }

    @RequestMapping(method = RequestMethod.GET, value="/findone/prodid/{prodid}")
    public List<Loan> findByProdId(@PathVariable("prodid") String as_ProdId) {
        logger.info("findByProdId" + as_ProdId);
        return loanService.findByProdId(as_ProdId);

    }

    @RequestMapping(method = RequestMethod.GET, value="/findall")
    public List<Loan> findAll() {
        logger.info("findAll");
        return loanService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value="/findProd")
    public List<Loan> findAllProd() {
        logger.info("findAllProd");
        return loanService.findAllProdId();
    }

}
