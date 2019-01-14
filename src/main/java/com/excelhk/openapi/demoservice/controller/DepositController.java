package com.excelhk.openapi.demoservice.controller;

import com.excelhk.openapi.demoservice.bean.Deposit;
import com.excelhk.openapi.demoservice.service.DepositService;
import com.excelhk.openapi.demoservice.utils.CommonUtils;
import com.excelhk.openapi.demoservice.utils.constants.DemoConstants;
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
	public Object findByProdId(@PathVariable("prodid") String as_ProdId, @RequestHeader(value = DemoConstants.REQUEST_TOKEN_HEADER, required = false) String as_ConnType) {
        logger.info("findByProdId" + as_ProdId);
		logger.info("as_ConnType " + as_ConnType);
		if(as_ConnType !=null && as_ConnType.equalsIgnoreCase("ftp")) {
			Deposit deposit = new Deposit();
			deposit.setProdId(as_ProdId);
			deposit.setProduct("Deposits");
			return commonUtils.responseFtpError(deposit);
		}else {
			return depositService.findByProdId(as_ProdId);

		}
    }

    @RequestMapping(method = RequestMethod.GET, value="/findall")
    public List<Deposit> findAll() {
        logger.info("findAll");
        return depositService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value="/findProd")
	public Object findAllProd(@RequestHeader(value = DemoConstants.REQUEST_TOKEN_HEADER, required = false) String as_ConnType) {
        logger.info("findAllProd");
		logger.info("as_ConnType " + as_ConnType);
		if(as_ConnType !=null && as_ConnType.equalsIgnoreCase("ftp")) {
            return commonUtils.responseFtpError("Deposits",new Deposit());
		}else {
            return depositService.findAllProdId();
		}
    }

}
