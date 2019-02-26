package com.excelhk.openapi.demoservice.controller;

import com.excelhk.openapi.demoservice.bean.Deposit;
import com.excelhk.openapi.demoservice.service.DepositService;
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
 *
 */
@RestController
@RequestMapping("/deposits")
public class DepositController {

    private static final Logger logger = LoggerFactory.getLogger(DepositController.class);

    @Autowired
	DepositService depositService;

	@Autowired
	private CommonUtils commonUtils;

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
	public Object findByProdId(@PathVariable("prodid") String prodId, @RequestHeader(value = "${sftp.conn.type}", required = false) String connType) {

        logger.info("findByProdId" + prodId);
		logger.info("connType " + connType);
		if(StringUtils.isNotEmpty(connType) && DemoConstants.CONNECT_TYPE_FTP.equalsIgnoreCase(connType)) {
			Deposit deposit = new Deposit();
			deposit.setProdId(prodId);
			deposit.setProduct(DemoConstants.PROD_TYPE_DEPOSIT);
			return commonUtils.responseByFtp(deposit);
		}else {
			return depositService.findByProdId(prodId);

		}

    }

    @RequestMapping(method = RequestMethod.GET, value="/findall")
    public List<Deposit> findAll() {
        logger.info("findAll");
        return depositService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value="/findProd")
	public Object findAllProd(@RequestHeader(value = "${sftp.conn.type}", required = false) String connType) {
        logger.info("findAllProd");
		logger.info("connType " + connType);
		if(StringUtils.isNotEmpty(connType) && DemoConstants.CONNECT_TYPE_FTP.equalsIgnoreCase(connType)) {
            return commonUtils.responseByFtp(DemoConstants.PROD_TYPE_DEPOSIT,new Deposit());
		}else {
            return depositService.findAllProdId();
		}
    }

}
