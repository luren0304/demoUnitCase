package com.excelhk.openapi.demoservice.controller;

import com.excelhk.openapi.demoservice.bean.Deposit;
import com.excelhk.openapi.demoservice.service.DepositService;
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
@RequestMapping("/deposits")
public class DepositController {

    private static final Logger logger = LoggerFactory.getLogger(DepositController.class);

    @Autowired
	DepositService depositService;

	@Autowired
	private InterfaceFileProcess interfaceFileProcess;
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
	public Object findByProdId(@PathVariable("prodid") String as_ProdId, @RequestHeader(value = "connection-type", required = false) String as_ConnType) {
        logger.info("findByProdId" + as_ProdId);
		logger.info("as_ConnType " + as_ConnType);
		if(as_ConnType !=null && !as_ConnType.equalsIgnoreCase("ftp")) {
			return depositService.findByProdId(as_ProdId);
		}else {
			Deposit deposit = new Deposit();
			deposit.setProdId(as_ProdId);
			deposit.setProduct("Deposits");
			try {
				return interfaceFileProcess.getDetails(deposit);
			} catch (SftpException e) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("error", e.getMessage());
				logger.error("findByProdId" +  e.getMessage());
				return new ResponseEntity<Object>(map, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
    }

    @RequestMapping(method = RequestMethod.GET, value="/findall")
    public List<Deposit> findAll() {
        logger.info("findAll");
        return depositService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value="/findProd")
	public Object findAllProd(@RequestHeader(value = "connection-type", required = false) String as_ConnType) {
        logger.info("findAllProd");
		logger.info("as_ConnType " + as_ConnType);
		if(as_ConnType !=null && !as_ConnType.equalsIgnoreCase("ftp")) {
			return depositService.findAllProdId();
		}else {
			try {
				return interfaceFileProcess.getProds("Deposits", new Deposit());
			} catch (SftpException e) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("error", e.getMessage());
				logger.error("findByProdId" +  e.getMessage());
				return new ResponseEntity<Object>(map, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
    }

}
