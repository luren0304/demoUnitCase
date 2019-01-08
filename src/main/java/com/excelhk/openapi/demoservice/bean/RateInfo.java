package com.excelhk.openapi.demoservice.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.math.BigDecimal;

@JsonInclude(Include.NON_NULL)
public class RateInfo {
	
	private String ccy_Cde;
	private String relvt_Ccy_Cde;
	private BigDecimal mid = null;
	private BigDecimal bid = null;
	private BigDecimal ask = null;
	private String feed_Source;
	private String lastDate;
/*	
	@Transient
	private String errorcde;
	@Transient
	private String errormsg;
	
	
	public String getErrorcde() {
		return errorcde;
	}
	public void setErrorcde(String errorcde) {
		this.errorcde = errorcde;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	
	*/
	public String getCcy_Cde() {
		return ccy_Cde;
	}
	public void setCcy_Cde(String ccy_Cde) {
		this.ccy_Cde = ccy_Cde;
	}
	public String getRelvt_Ccy_Cde() {
		return relvt_Ccy_Cde;
	}
	public void setRelvt_Ccy_Cde(String relvt_Ccy_Cde) {
		this.relvt_Ccy_Cde = relvt_Ccy_Cde;
	}
	public BigDecimal getMid() {
		return mid;
	}
	public void setMid(BigDecimal mid) {
		this.mid = mid;
	}
	public BigDecimal getBid() {
		return bid;
	}
	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}
	public BigDecimal getAsk() {
		return ask;
	}
	public void setAsk(BigDecimal ask) {
		this.ask = ask;
	}
	public String getFeed_Source() {
		return feed_Source;
	}
	public void setFeed_Source(String feed_Source) {
		this.feed_Source = feed_Source;
	}
	public String getLastDate() {
		return lastDate;
	}
	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}
	
}
