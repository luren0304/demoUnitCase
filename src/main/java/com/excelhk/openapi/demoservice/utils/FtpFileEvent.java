package com.excelhk.openapi.demoservice.utils;


import com.jcraft.jsch.SftpException;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author anita
 *
 */
public class FtpFileEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

    private String fileName;
    private boolean downloadFlag = false;
	private SftpException sftpException;
	private Object object;
	private List prodLst;
	private String eventType;

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
	 *
	 * Constructor
	 * @param source
	 * @param fileName
	 * @param obj
	 * @param prodLst
	 */
    public FtpFileEvent(Object source, String fileName, Object obj, List prodLst) {
        super(source);
        this.fileName = fileName;
        this.object = obj;
        this.prodLst = prodLst;
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

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public List getProdLst() {
		return prodLst;
	}

	public void setProdLst(List prodLst) {
		this.prodLst = prodLst;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
}
