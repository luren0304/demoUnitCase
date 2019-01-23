package com.excelhk.openapi.demoservice.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

/**
 * @author anita
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RateInfo {
	
	private String ccyCde;
	private String relvtCcyCde;
	private BigDecimal mid = null;
	private BigDecimal bid = null;
	private BigDecimal ask = null;
	private String feedSource;
	private String lastDate;

	public String getCcyCde() {
		return ccyCde;
	}
	public void setCcyCde(String ccyCde) {
		this.ccyCde = ccyCde;
	}
	public String getRelvtCcyCde() {
		return relvtCcyCde;
	}
	public void setRelvtCcyCde(String relvtCcyCde) {
		this.relvtCcyCde = relvtCcyCde;
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
	public String getFeedSource() {
		return feedSource;
	}
	public void setFeedSource(String feedSource) {
		this.feedSource = feedSource;
	}
	public String getLastDate() {
		return lastDate;
	}
	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}
	
}
