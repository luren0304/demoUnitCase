package com.excelhk.openapi.demoservice.utils;


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
	private InterfaceFileFtpProcess interfaceFileFtpProcess;
	@Autowired
	private CommonUtils commonUtils;

	/**
	 *  Check if download file successfully
	 *
	 * @param ftpFileEvent
	 * @throws Exception
	 */
	@EventListener
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
	}

	/**
	 *
	 *  Download ftp file
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public boolean ftpFileHandle(String fileName) throws Exception {
		return interfaceFileFtpProcess.download(fileName);
	}
	
}
