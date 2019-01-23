package com.excelhk.openapi.demoservice.utils;


import com.jcraft.jsch.SftpException;
import org.springframework.context.ApplicationEvent;

/**
 * @author anita
 *
 */
public class FtpFileEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

    private String fileName;
    private boolean downloadFlag = false;
	private SftpException sftpException;

    /**
     * Constructor
     *
     * @param source
     * @param fileName
     */
    public FtpFileEvent(Object source, String fileName) {
        super(source);
        this.fileName = fileName;
    }

    /**
     * Get File name
     *
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    /**
     *  Set file name
     * @param fileName
     */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

    /**
     *
     *  Download or not
     * @return
     */
	public boolean isDownloadFlag() {
		return downloadFlag;
	}

    /**
     *
     * Set the download flag
     * @param downloadFlag
     */
	public void setDownloadFlag(boolean downloadFlag) {
		this.downloadFlag = downloadFlag;
	}

    /**
     * Get sftp exception
     *
     * @return
     */
	public SftpException getSftpException() {
		return sftpException;
	}

    /**
     * Set sftp exception
     * @param sftpException
     */
	public void setSftpException(SftpException sftpException) {
		this.sftpException = sftpException;
	}
}
