package com.excelhk.openapi.demoservice.controller;

import com.excelhk.openapi.demoservice.bean.RateInfo;
import com.excelhk.openapi.demoservice.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/rateinfo")
public class RateInfoController {
	
	private static final Logger logger = LoggerFactory.getLogger(RateInfoController.class);
	@Autowired
	//private InterfaceFileProcess interfaceFileProcess;
	private CommonUtils commonUtils;
	
	
	
	@RequestMapping("/index.html")
	public String index() {
		return "hello world";
	}
	
	@RequestMapping("infor")
	@ResponseBody
	public Object getRateInfo() {
		RateInfo l_RateInfo = new RateInfo();
		l_RateInfo.setAsk(new BigDecimal(7.7500 + Math.random()).setScale(8, BigDecimal.ROUND_HALF_UP));
		l_RateInfo.setBid(new BigDecimal(7.7500 + Math.random()).setScale(8, BigDecimal.ROUND_HALF_UP));
		l_RateInfo.setBid(new BigDecimal(7.7500 + Math.random()).setScale(8, BigDecimal.ROUND_HALF_UP));
		l_RateInfo.setCcy_Cde("USD");
		l_RateInfo.setRelvt_Ccy_Cde("HKD");
		l_RateInfo.setFeed_Source("BLOOMBERG");
		//l_RateInfo.setLts_Last_Date(new Timestamp(new Date().getTime()));
		return l_RateInfo;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/RateCcy/{RateCcy}/RateRelvtCcy/{RateRelvtCcy}")
	public Object getRateDetails(@PathVariable("RateCcy") String as_Ccy, @PathVariable("RateRelvtCcy") String as_RelvtCcy) {
		logger.info("getRateDetails ccy = " + as_Ccy + " relvtCcy = " + as_RelvtCcy);
		RateInfo rateInfo = new RateInfo();
		rateInfo.setCcy_Cde(as_Ccy);
		rateInfo.setRelvt_Ccy_Cde(as_RelvtCcy);
		return commonUtils.responseFtpError(rateInfo);
		/*try {
			return interfaceFileProcess.getDetails(rateInfo);
		} catch (SftpException e) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", e.getMessage());
			return new ResponseEntity<Object>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
		
	}	
	
}
