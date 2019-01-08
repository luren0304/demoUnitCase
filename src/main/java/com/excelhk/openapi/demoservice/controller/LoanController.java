package com.excelhk.openapi.demoservice.controller;

import com.excelhk.openapi.demoservice.bean.Loan;
import com.excelhk.openapi.demoservice.service.LoanService;
import com.excelhk.openapi.demoservice.utils.InterfaceFileProcess;
import com.jcraft.jsch.SftpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);
    @Autowired
    LoanService loanService;
	@Autowired
	private InterfaceFileProcess interfaceFileProcess;

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
	public Object findByProdId(@PathVariable("prodid") String as_ProdId, @RequestHeader(value = "connection-type", required = false) String as_ConnType) {
        logger.info("findByProdId" + as_ProdId);
		logger.info("as_ConnType " + as_ConnType);
		if(as_ConnType !=null && !as_ConnType.equalsIgnoreCase("ftp")) {
			return loanService.findByProdId(as_ProdId);
		}else {
			Loan loan = new Loan();
			loan.setProdId(as_ProdId);
			loan.setProduct("Loans");
			try {
				return interfaceFileProcess.getDetails(loan);
			} catch (SftpException e) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("error", e.getMessage());
				logger.error("findByProdId" +  e.getMessage());
				return new ResponseEntity<Object>(map, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
    }

    @RequestMapping(method = RequestMethod.GET, value="/findall")
    public List<Loan> findAll() {
        logger.info("findAll");
        return loanService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value="/findProd")
	public Object findAllProd(@RequestHeader(value = "connection-type", required = false) String as_ConnType) {
        logger.info("findAllProd");
		logger.info("as_ConnType " + as_ConnType);
		if(as_ConnType !=null && !as_ConnType.equalsIgnoreCase("ftp")) {
			return loanService.findAllProdId();
		}else {
			try {
				return interfaceFileProcess.getProds("Loans", new Loan());
			} catch (SftpException e) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("error", e.getMessage());
				logger.error("findByProdId" +  e.getMessage());
				return new ResponseEntity<Object>(map, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
    }

}
