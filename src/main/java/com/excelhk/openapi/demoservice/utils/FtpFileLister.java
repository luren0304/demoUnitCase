package com.excelhk.openapi.demoservice.utils;


import com.excelhk.openapi.demoservice.utils.constants.DemoConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author anita
 *
 */
@Component
public class FtpFileLister {

	@Autowired
	private CommonUtils commonUtils;

	@EventListener
	public void handleEvent(FtpFileEvent ftpFileEvent) throws Exception {
		boolean fileExist = false;
		try{
			fileExist = commonUtils.checkFileExist(ftpFileEvent.getFileName());
			if(fileExist) {
				System.out.println("file exists");
				ftpFileEvent.setDownloadFlag(true);
				if(DemoConstants.EVENT_TYPE_PROD.equals(ftpFileEvent.getEventType())){
					getProdsByFile(ftpFileEvent);
				}else if(DemoConstants.EVENT_TYPE_DETAILS.equals(ftpFileEvent.getEventType())){
					getDetailByFile(ftpFileEvent);
				}else {
					System.out.println("Event type : " + ftpFileEvent.getEventType() + "no handle");
				}
			}else {
				System.out.println("file doesn't exist");
				ftpFileEvent.setDownloadFlag(false);
			}
		}catch(Exception e){
			ftpFileEvent.setDownloadFlag(false);
			System.out.println("download exception :" + e.getMessage());
			ftpFileEvent.setSftpException(commonUtils.handleErr(e));
		}

	}
/*

	public void ftpFileHandle(FtpFileEvent ftpFileEvent){
		boolean fileExist = false;
		try{
			fileExist = interfaceFileFtpProcess.download(ftpFileEvent.getFileName());
			if(fileExist) {
				System.out.println("file exists");
				ftpFileEvent.setDownloadFlag(true);
			}else {
				System.out.println("file doesn't exist");
				ftpFileEvent.setDownloadFlag(false);
			}
		}catch(Exception e){
			ftpFileEvent.setDownloadFlag(false);
			System.out.println("download exception :" + e.getMessage());
			ftpFileEvent.setSftpException(commonUtils.handleErr(e));
		}
	}
*/

	public void getProdsByFile(FtpFileEvent ftpFileEvent){
		System.out.println("getProdsByFile start");
		commonUtils.getProdsByFile(ftpFileEvent.getProdLst(),ftpFileEvent.getFileName(), ftpFileEvent.getObject());
		System.out.println("getProdsByFile End");
	}

	public void getDetailByFile(FtpFileEvent ftpFileEvent){
		System.out.println("getDetailByFile start");
		commonUtils.getDetailByFile(ftpFileEvent.getObject(),ftpFileEvent.getFileName(),ftpFileEvent.getProdLst());
		System.out.println("getDetailByFile End");
	}


	/**
	 *  Check if download file successfully
	 *
	 * @param ftpFileEvent
	 * @throws Exception
	 */
	/*@EventListener
	public void handleEvent(FtpFileEvent ftpFileEvent) throws Exception {
		boolean fileExist = false;
		try{
			fileExist =ftpFileHandle( ftpFileEvent.getFileName());
			if(fileExist) {
				System.out.println("file exists");
				ftpFileEvent.setDownloadFlag(true);
			}else {
				System.out.println("file doesn't exist");
				ftpFileEvent.setDownloadFlag(false);
			}
		}catch(Exception e){
			ftpFileEvent.setDownloadFlag(false);
			System.out.println("download exception :" + e.getMessage());
			ftpFileEvent.setSftpException(commonUtils.handleErr(e));
		}
	}*/

	/*
	*//**
	 *
	 *  Download ftp file
	 * @param fileName
	 * @return
	 * @throws Exception
	 *//*
	public boolean ftpFileHandle(String fileName) throws Exception {
		return interfaceFileFtpProcess.download(fileName);
	}


	public void getProdsByFile(List prodLst, String fileName, Object obj){
		commonUtils.getProdsByFile(prodLst,fileName,obj);
	}

	public void getDetailByFile(Object obj, String fileName, List detailsLst){
		commonUtils.getDetailByFile(obj, fileName, detailsLst);
	}*/
}
