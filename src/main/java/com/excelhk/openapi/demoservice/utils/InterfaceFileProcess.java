package com.excelhk.openapi.demoservice.utils;


import com.excelhk.openapi.demoservice.bean.Deposit;
import com.excelhk.openapi.demoservice.bean.Loan;
import com.excelhk.openapi.demoservice.bean.RateInfo;
import com.jcraft.jsch.SftpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anita
 */

@Component
public class InterfaceFileProcess {

	@Value("${sftp.waitTime}")
	private long waitTime;

	@Value("${sftp.retry}")
	private int loopCnt;
	
	@Autowired
	private InterfaceFileFtpProcess interfaceFileFtpProcess;
	
	@Autowired
	private ApplicationContext applicationContext; 
	
	@Autowired
	private CommonUtils commonUtils;
	private static final Logger logger = LoggerFactory.getLogger(InterfaceFileProcess.class);
	
	

	/**
	 * get details
	 * 
	 * @param obj
	 * @return
	 */
	public List getDetails(Object obj) throws SftpException{
		List details = new ArrayList();
		String fileName=null;
		if(obj instanceof RateInfo   ) {
			fileName = commonUtils.getFileName("exch" + "." + ((RateInfo)obj).getCcyCde() + "." + ((RateInfo)obj).getRelvtCcyCde());
		}else if (obj instanceof Deposit) {
			fileName =  commonUtils.getFileName("prod" + "." + ((Deposit)obj).getProduct() + "." + ((Deposit)obj).getProdId());
		}else if (obj instanceof Loan) {
			fileName =  commonUtils.getFileName("prod" + "." + ((Loan)obj).getProduct() + "." + ((Loan)obj).getProdId());
		}
		
		boolean success = commonUtils.generateFile(fileName, obj );
		if(success) {
			/**
			 * put out file
			 */
			try {
				interfaceFileFtpProcess.upload(fileName);
			} catch (Exception e) {
				throw commonUtils.handleErr(e);
			}
			/**
			 * Get remote in file
			 */
			logger.info("download file start");

			success = downloadLoop(fileName);
			if(success) {
				logger.info("download file successfully");
				logger.info("get file details");
				commonUtils.getDetailByFile(obj, fileName, details);
			}
			logger.info("download file end");

		}else {
			details.add(obj);
		}
		
		return details;
	}
	
	/**
	 * Get the product id
	 * 
	 * @param product
	 * @param obj
	 * @return
	 */
	public List getProds(String product, Object obj) throws SftpException{
		String fileName = null;
		String content = product;
		List pordLst = new ArrayList();
		fileName = commonUtils.getFileName("prod"+ "." + product);
		boolean success = commonUtils.generateFile(fileName,  content);
		if(success) {
			/**
			 * put out file
			 */
			try {
				interfaceFileFtpProcess.upload(fileName);
			} catch (Exception e) {
				throw commonUtils.handleErr(e);
			}
			/**
			 * get remote in file
			 */
			logger.info("download file start");
			success = downloadLoop(fileName);
			if(success) {
				logger.info("download file successfully");
				logger.info("get file details");
				commonUtils.getProdsByFile(pordLst, fileName, obj);
			}
			logger.info("download file end");
		}
		return pordLst;
	}

	private boolean downloadLoop(String fileName) throws SftpException {
		boolean success = false;
		SftpException sftpException = null;
		FtpFileEvent ftpFileEvent = new FtpFileEvent(this, fileName);
		for(int i = 0; i < loopCnt; i ++) {
			logger.info("loop i = " + i);
			try {
				applicationContext.publishEvent(ftpFileEvent);
				success = ftpFileEvent.isDownloadFlag();
				if (success) {
					break;
				} else {
					sftpException = ftpFileEvent.getSftpException();
					if (sftpException != null) {
						throw sftpException;
					}
				}

				if (i != loopCnt - 1) {
					Thread.sleep(waitTime * 1000 / loopCnt);
				}
			}catch(SftpException e){
				throw e;
			} catch (Exception e) {
				throw commonUtils.handleErr(e);
			}
		}
		return success;
	}


}
