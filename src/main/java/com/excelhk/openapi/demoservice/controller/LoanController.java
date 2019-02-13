package com.excelhk.openapi.demoservice.controller;

import com.excelhk.openapi.demoservice.bean.Loan;
import com.excelhk.openapi.demoservice.service.LoanService;
import com.excelhk.openapi.demoservice.utils.CommonUtils;
import com.excelhk.openapi.demoservice.utils.constants.DemoConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author anita
 */
@RestController
@RequestMapping("/loans")
public class LoanController {
    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);
    @Autowired
    LoanService loanService;
	@Autowired
	private CommonUtils commonUtils;

    @RequestMapping(method = RequestMethod.POST, value="/createObj")
    public boolean createObj(Loan loan) {
        logger.info("createObj start");
        loanService.createLoan(loan);
        logger.info("createObj End");
        return true;
    }

    @RequestMapping(method = RequestMethod.POST, value="/createObjList")
    public boolean createObjList(@RequestBody List<Loan> loanLst) {
        logger.info("createObjList start");
        loanService.createLoan(loanLst);
        logger.info("createObjList End");
        return true;
    }

    @RequestMapping(method = RequestMethod.GET, value="/findone/prodid/{prodid}")
	public Object findByProdId(@PathVariable("prodid") String prodId, @RequestHeader(value = "${sftp.conn.type}", required = false) String connType) {
        logger.info("findByProdId" + prodId);
		logger.info("connType " + connType);
        if(StringUtils.isNotEmpty(connType) && DemoConstants.CONNECT_TYPE_FTP.equalsIgnoreCase(connType)) {
			Loan loan = new Loan();
			loan.setProdId(prodId);
			loan.setProduct(DemoConstants.PROD_TYPE_LOANS);
			return commonUtils.responseFtpError(loan);
		}else {
			return loanService.findByProdId(prodId);
		}
    }

    @RequestMapping(method = RequestMethod.GET, value="/findall")
    public List<Loan> findAll() {
        logger.info("findAll");
        return loanService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value="/findProd")
	public Object findAllProd(@RequestHeader(value = "${sftp.conn.type}", required = false) String connType) {
        logger.info("findAllProd");
		logger.info("connType " + connType);
        if(StringUtils.isNotEmpty(connType) && DemoConstants.CONNECT_TYPE_FTP.equalsIgnoreCase(connType)) {
			return commonUtils.responseFtpError(DemoConstants.PROD_TYPE_LOANS, new Loan());
		}else {
			return loanService.findAllProdId();
		}
    }
}
