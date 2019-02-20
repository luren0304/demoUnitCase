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

	/**
	 *
	 * @param ftpFileEvent
	 */
	@EventListener
	public void handleEvent(FtpFileEvent ftpFileEvent) {
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
}
