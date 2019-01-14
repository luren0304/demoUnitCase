package com.excelhk.openapi.demoservice.controller;

import com.excelhk.openapi.demoservice.bean.Loan;
import com.excelhk.openapi.demoservice.service.LoanService;
import com.excelhk.openapi.demoservice.utils.CommonUtils;
import com.excelhk.openapi.demoservice.utils.constants.DemoConstants;
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
	@Autowired
	private CommonUtils commonUtils;



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
	public Object findByProdId(@PathVariable("prodid") String as_ProdId, @RequestHeader(value = DemoConstants.REQUEST_TOKEN_HEADER, required = false) String as_ConnType) {
        logger.info("findByProdId" + as_ProdId);
		logger.info("as_ConnType " + as_ConnType);
		if(as_ConnType != null && as_ConnType.equalsIgnoreCase("ftp")) {
			Loan loan = new Loan();
			loan.setProdId(as_ProdId);
			loan.setProduct("Loans");
			return commonUtils.responseFtpError(loan);
		}else {
			return loanService.findByProdId(as_ProdId);
		}
    }

    @RequestMapping(method = RequestMethod.GET, value="/findall")
    public List<Loan> findAll() {
        logger.info("findAll");
        return loanService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value="/findProd")
	public Object findAllProd(@RequestHeader(value = DemoConstants.REQUEST_TOKEN_HEADER, required = false) String as_ConnType) {
        logger.info("findAllProd");
		logger.info("as_ConnType " + as_ConnType);
		if(as_ConnType != null && as_ConnType.equalsIgnoreCase("ftp")) {
			return commonUtils.responseFtpError("Loans", new Loan());
		}else {
			return loanService.findAllProdId();
		}
    }
}
