package com.excelhk.openapi.demoservice.utils;


import org.springframework.context.ApplicationEvent;

public class FtpFileEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	public String getFileName() {
		return fileName;
	}

	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private String fileName;
	
	private boolean downloadFlag = false;
	
	public boolean isDownloadFlag() {
		return downloadFlag;
	}


	public void setDownloadFlag(boolean downloadFlag) {
		this.downloadFlag = downloadFlag;
	}


	public FtpFileEvent(Object source, String as_FileName) {
		super(source);
		this.fileName = as_FileName;
	}
}
