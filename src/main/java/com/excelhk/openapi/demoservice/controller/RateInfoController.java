package com.excelhk.openapi.demoservice.controller;

import com.excelhk.openapi.demoservice.bean.RateInfo;
import com.excelhk.openapi.demoservice.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author anita 
 */

@RestController
@RequestMapping("/rateinfo")
public class RateInfoController {
	
	private static final Logger logger = LoggerFactory.getLogger(RateInfoController.class);
	@Autowired
	private CommonUtils commonUtils;
	
	@RequestMapping("/index.html")
	public String index() {
		return "hello world";
	}
	
	@RequestMapping("infor")
	@ResponseBody
	public Object getRateInfo() {
		RateInfo rateInfo = new RateInfo();
		rateInfo.setAsk(new BigDecimal(7.7500 + Math.random()).setScale(8, BigDecimal.ROUND_HALF_UP));
		rateInfo.setBid(new BigDecimal(7.7500 + Math.random()).setScale(8, BigDecimal.ROUND_HALF_UP));
		rateInfo.setBid(new BigDecimal(7.7500 + Math.random()).setScale(8, BigDecimal.ROUND_HALF_UP));
		rateInfo.setCcyCde("USD");
		rateInfo.setRelvtCcyCde("HKD");
		rateInfo.setFeedSource("BLOOMBERG");
		return rateInfo;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/RateCcy/{RateCcy}/RateRelvtCcy/{RateRelvtCcy}")
	public Object getRateDetails(@PathVariable("RateCcy") String ccy, @PathVariable("RateRelvtCcy") String relvtCcy) {
		logger.info("getRateDetails ccy = " + ccy + " relvtCcy = " + relvtCcy);
		RateInfo rateInfo = new RateInfo();
		rateInfo.setCcyCde(ccy);
		rateInfo.setRelvtCcyCde(relvtCcy);
		return commonUtils.responseByFtp(rateInfo);
	}
	
}
