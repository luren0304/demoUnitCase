package com.excelhk.openapi.demoservice.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 *
 * @author anita
 */

@Component
@ConfigurationProperties(prefix="sftp")
public class InterfaceFileFtpProcess {
	
	private String host;
	private int port;
	private String user;
	private String password;
	
	private String remoteInpath;
	private String localInpath;
	private String remoteOutpath;
	private String localOutpath;
	
	@Value("${sftp.romteOut.file.conv:txt}")
	private String remoteOutFileConv;
	
	@Value("${sftp.remoteIn.file.conv:txt}")
	private String remoteInFileConv;
	
	private static Logger LOGGER = LoggerFactory.getLogger(InterfaceFileFtpProcess.class);
	private SftpClientHandler sftpClientHandler = null;
	

	
	/**
	 * 
	 * Upload request file to sftp server
	 * 
	 * @param fileName
	 */
	
	public void upload (String fileName) throws Exception{
		try {
			fileName = fileName + "." + remoteOutFileConv;
			String localOutpathString = null;
			checkDirExist(localOutpath);
			if(!localOutpath.endsWith(File.separator)) {
				localOutpathString = localOutpath + File.separator + fileName;
			}else {
				localOutpathString = localOutpath + fileName ;
			}
			LOGGER.info("localOutpathString " + localOutpathString);
				
			if(sftpClientHandler == null) {
				sftpClientHandler = SftpClientHandler.getInstance(host, port);
			}
			sftpClientHandler.debugResponses(true);
			sftpClientHandler.login(user, password);
			sftpClientHandler.chdir(remoteOutpath);
			sftpClientHandler.put(localOutpathString, fileName);
		} catch (Exception e) {
			LOGGER.error("upload file failed. error message: " + e.getMessage() );
			e.printStackTrace();
			throw e;
		}finally {
			if(sftpClientHandler != null) {
				try {
					sftpClientHandler.quit();
				} catch (Exception e) {
				}
			}
		}
	}
	
	
	/**
	 * Download reply file from SFTP server
	 *
	 * @param fileName
	 * @return
	 */
	public boolean download(String fileName) throws Exception{
		try {
			fileName = fileName + "." + remoteInFileConv;
			String localInpathString = null;
			checkDirExist(localInpath);
			if(!localInpath.endsWith(File.separator)) {
				localInpathString = localInpath + File.separator + fileName;
			}else {
				localInpathString = localInpath + fileName ;
			}
			LOGGER.info("localInpathString " + localInpathString);
			
			if(sftpClientHandler == null) {
				sftpClientHandler = SftpClientHandler.getInstance(host, port);
			}
			sftpClientHandler.debugResponses(true);
			sftpClientHandler.login(user, password);
			sftpClientHandler.chdir(remoteInpath);
			String [] sFileList = sftpClientHandler.dir(fileName);
			/**
			 * check file exist
			 */
			if(sFileList.length > 0) {
				/**
				 * file exist
				 */
				sftpClientHandler.get(localInpathString, fileName);
				LOGGER.info("The remote file download successfully.");
				LOGGER.info("Rename the remote file start");
				sftpClientHandler.rename(fileName, fileName.substring(0, fileName.lastIndexOf(".")) + ".bak" );
				LOGGER.info("Rename the remote file successfully End");
				return true;
			}else {
				LOGGER.info("The remote file doesn't exist. File Name: " + fileName);
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("download file failed. error message: " + e.getMessage() );
			e.printStackTrace();
			throw e;
		}finally {
			if(sftpClientHandler != null) {
				try {
					sftpClientHandler.quit();
				} catch (Exception e) {
				}
			}
		}
	}
	

	/**
	 * check if directory exists, if not, create it.
	 * @param dir
	 */
	public void checkDirExist(String dir) {
		LOGGER.info("dir " + dir);
		File fileDir = new File(dir);
		if(!fileDir.exists()) {
			fileDir.mkdirs();
		}
	}

	public String getRemoteInpath() {
		return remoteInpath;
	}
	public void setRemoteInpath(String remoteInpath) {
		this.remoteInpath = remoteInpath;
	}
	public String getLocalInpath() {
		return localInpath;
	}
	public void setLocalInpath(String localInpath) {
		this.localInpath = localInpath;
	}
	public String getRemoteOutpath() {
		return remoteOutpath;
	}
	public void setRemoteOutpath(String remoteOutpath) {
		this.remoteOutpath = remoteOutpath;
	}
	public String getLocalOutpath() {
		return localOutpath;
	}
	public void setLocalOutpath(String localOutpath) {
		this.localOutpath = localOutpath;
	}
	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
