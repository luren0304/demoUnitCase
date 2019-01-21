package com.excelhk.openapi.demoservice.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class FtpFileLister {

	@Autowired
	private InterfaceFileFtpProcess interfaceFileFtpProcess;
	@Autowired
	private CommonUtils commonUtils;
	
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
	
	public boolean ftpFileHandle(String as_FileName) throws Exception {
		return interfaceFileFtpProcess.download(as_FileName);
	}
	
}
