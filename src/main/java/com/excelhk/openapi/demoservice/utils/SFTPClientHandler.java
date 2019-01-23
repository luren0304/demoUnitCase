package com.excelhk.openapi.demoservice.utils;

import com.excelhk.openapi.demoservice.utils.constants.DemoConstants;
import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;


/**
 *
 * @author anita
 */
public class SftpClientHandler {

	
	private ChannelSftp sftpClient = null;
	private static int port = 22;
	private static JSch jsch = null;	
	private Session sshSession = null;
	private Channel channel = null;
	private static String hostName;
	private static Logger LOGGER = LoggerFactory.getLogger(SftpClientHandler.class);

	public static SftpClientHandler getInstance(String hostNameParam) throws Exception{
		LOGGER.info("Create SftpClientHandler " + hostNameParam);
		hostName = hostNameParam;
		jsch = new JSch();
		return new SftpClientHandler();
	}

	public static SftpClientHandler getInstance(String hostNameParam, int portParam) throws Exception{
		port = portParam;
		return getInstance(hostNameParam);
	}
	
	public void chdir(String dstPath) throws Exception{
		sftpClient.cd(dstPath);
	}
	
	public void mkdir(String dirPath) throws IOException, SftpException{
		try{
			sftpClient.mkdir(dirPath);
		}catch(SftpException ex){
			ex.printStackTrace();
			throw ex;
		}
	}
	
	public void delete(String outDest) throws IOException, SftpException{
		try{
			sftpClient.rm(outDest);
		}catch(SftpException ex){
			ex.printStackTrace();
			throw ex;
		}
	}	
	
	public void rmdir(String outDest) throws IOException, SftpException{
		try{
			sftpClient.rmdir(outDest);
		}catch(SftpException ex){
			ex.printStackTrace();
			throw ex;
		}
	}

	public void debugResponses(boolean debugRes) throws Exception{
		if (debugRes == true){
			JSch.setLogger(new DebugLogger());
		}else{
			JSch.setLogger(null);
		}
	}

	public void login(String userName, String password) throws Exception{
		this.login(userName, password, port);
	}

	public void put(String outFile, String fileName, boolean modeParam) throws IOException, SftpException{
		int mode = modeParam ? ChannelSftp.APPEND : ChannelSftp.OVERWRITE;
		try{
			File file = new File(outFile);
			FileInputStream fileInputStream = new FileInputStream(file);
			sftpClient.put(fileInputStream, fileName, mode);
			try{
				fileInputStream.close();
			}catch(Exception ex){	
			}
		}catch(SftpException ex){
			ex.printStackTrace();
			throw ex;
		}
	}
	
	public void put(String outFile, String fileName) throws IOException, SftpException{
		this.put(outFile, fileName, false);
	}
	
	public void get(String inFile, String fileName) throws Exception{
		File file = new File(inFile);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		sftpClient.get(fileName, fileOutputStream);
		try{
			fileOutputStream.close();
		}catch(Exception ex){	
		}
	}
	
	public String[] dir(String tag) throws Exception{
		
		String[] fileNameList;
		Vector allFile = null;
		String splitDelimiter = ",";
		String fielNameString = "";
		
		if ((tag.indexOf(DemoConstants.SLASH) >= 0) || (tag.indexOf(DemoConstants.BACK_SLASH_SLASH) >= 0)){
			allFile = sftpClient.ls(tag);
			tag = "*.*";
		}else{
			if ((tag != null) && (tag.indexOf(DemoConstants.PERIOD)) < 0){
				tag = tag + ".*";
			}
		
			allFile = sftpClient.ls(".");
		}
		for (int count=0;count < allFile.size();count++ ){
			Object obj = allFile.elementAt(count);			
			if(obj instanceof LsEntry){
				LsEntry entry = (LsEntry)obj;
				String tmpFileName = entry.getFilename();				               
                if (StringUtils.isEmpty(tmpFileName)
                		|| (".".equals(tmpFileName)) || ("..".equals(tmpFileName))){
                	continue;
                }

                //List all files
                if (("*".equals(tag)) || ("*.*".equals(tag))){
                	if (StringUtils.isEmpty(fielNameString)){
                		fielNameString = tmpFileName;
                	}else{
                		fielNameString = fielNameString + splitDelimiter + tmpFileName;
                	}
                } else if (tag.indexOf('.')>= 0) {
                	int pos = tag.lastIndexOf('.');
                	String mchPrefix = tag;
                	String mchExt = "";
                	if (pos >= 0){
                		mchPrefix = tag.substring(0, pos);                	
                		mchExt = tag.substring(pos + 1);
                	}
                	
                	String filePrefix = tmpFileName;
                	String fileExt = "";
                	pos = tmpFileName.lastIndexOf('.');
                	if (pos >= 0){
                		filePrefix = tmpFileName.substring(0, pos);
                		fileExt = tmpFileName.substring(pos + 1);
                	}
                	if (matchStr(filePrefix, mchPrefix) && matchStr(fileExt, mchExt)){
                		if ((fielNameString == null) || ("".equals(fielNameString))){
                    		fielNameString = tmpFileName;
                    	}else{
                    		fielNameString = fielNameString + splitDelimiter + tmpFileName;
                    	}
                	}
                } else {
                	//List appointed file
                	if (tmpFileName.equals(tag)){
                		fielNameString = tmpFileName;
                	}
                }
            
            }
		}

		if ((fielNameString != null) && (!"".equals(fielNameString))){
			fileNameList = fielNameString.split(splitDelimiter);
		}else{
			fileNameList = new String[0];
		}
		try{
			Arrays.sort(fileNameList);
		}catch(Exception ex){
			
		}
		return fileNameList;
	} 
	
	private static boolean matchStr(String srcStr, String mchStr) throws Exception{
		boolean result = false;
		String tmpStr = "";
		
		if (mchStr.indexOf(DemoConstants.ASTERISK) == -1) {
			if (srcStr.equalsIgnoreCase(mchStr)){
				result = true;
			}
		} else if (mchStr.startsWith(DemoConstants.ASTERISK)){
			tmpStr = mchStr.substring(1); 
			if (srcStr.endsWith(tmpStr)){
				result = true;
			}
		} else if (mchStr.endsWith(DemoConstants.ASTERISK)){
			tmpStr = mchStr.substring(0, mchStr.length() - 1);
			if (srcStr.startsWith(tmpStr)){
				result = true;
			}
		} else {	
			int count = mchStr.indexOf(DemoConstants.ASTERISK);
			if ((srcStr.startsWith(mchStr.substring(0,count))) && (srcStr.endsWith(mchStr.substring(count + 1)))){
				result = true;
			}
		}
		
		return result;
	}
	
	public void rename(String oldName, String newName) throws Exception{
		sftpClient.rename(oldName, newName);
	}
	
	public String system() throws Exception{
		String result = execCommand("uname");
		return result; 
	}
	
	public String execCommand(String command) throws Exception{
		if (sshSession == null){
			throw new Exception("No SFTP has been connected.");
		}
		
		ChannelExec channelExec = (ChannelExec)sshSession.openChannel("exec");
		channelExec.setCommand(command);
		channelExec.setInputStream(null);
		channelExec.connect();
				
		final BufferedReader errReader = new BufferedReader(new InputStreamReader(((ChannelExec)channelExec).getErrStream()));
	    BufferedReader inReader = new BufferedReader(new InputStreamReader(channelExec.getInputStream()));
		
	    final StringBuffer errorMessage = new StringBuffer();
		ExecCommErrMsgThread errorThread = new ExecCommErrMsgThread(errReader, errorMessage);

		try {
		      errorThread.start();
		} catch (IllegalStateException ex) {		
			ex.printStackTrace();
			LOGGER.info("SftpClientHandler.execCommand.errorThread:Error message : " + ex);
		}
		
		String outContents = "";
		try {
			outContents = parseExecResult(inReader);			

		    if(channelExec.isClosed()) {
		    	int exitCode = channelExec.getExitStatus();
		    	LOGGER.info("SftpClientHandler.execCommand.execChannel closed,exitCode = " + exitCode);
		    }

		    try {
		        // make sure that the error thread exits
		    	errorThread.join();
		    } catch (InterruptedException ex) {		    	
		    	LOGGER.info("SftpClientHandler.execCommand.wait thread excetpion = " + ex);
		    }
		    
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new IOException(ex.toString());
		}finally {
			try {
				inReader.close();
			} catch (IOException ex) {
				LOGGER.info("SftpClientHandler.execCommand.close read string excetpion = " + ex);
			}

		    try {
		    	errReader.close();
		    } catch (IOException ex) {
		    	LOGGER.info("SftpClientHandler.execCommand.close error string excetpion = " + ex);
		    }
		    
		    if ((errorMessage.toString() != null) && (!"".equals(errorMessage.toString()))){
				throw new Exception(errorMessage.toString());
			}

		    channelExec.disconnect();
		}    
		return outContents.trim();
	}

	/**
	 * Parse output string
	 * @param lines
	 * @return
	 * @throws IOException
	 */
	protected String parseExecResult(BufferedReader lines) throws IOException {
		StringBuffer output = new StringBuffer();
		char[] buf = new char[512];
		int readCount;
		
		while ( (readCount = lines.read(buf, 0, buf.length)) > 0 ) {
			output.append(buf, 0, readCount);
		}

		return output.toString();
	}

	
	public void quit() throws Exception{
		sftpClient.quit();
    	channel.disconnect();
    	sshSession.disconnect();
	}

	
	public void login(String userName, String password, int portParam) throws Exception{
   		sshSession = jsch.getSession(userName, hostName, portParam);
   		sshSession.setPassword(password);
    		
   		Properties sshConfig = new Properties();
   		sshConfig.setProperty("StrictHostKeyChecking", "no");
   		sshConfig.setProperty("PreferredAuthentications", "password");
   		sshSession.setConfig(sshConfig);
   		sshSession.setTimeout(30000);
   		sshSession.connect();
    		
   		channel = sshSession.openChannel("sftp");
   		channel.connect();
   		sftpClient = (ChannelSftp)channel;
   		LOGGER.info("SftpClientHandler login successfully, JSch version = " + sftpClient.version());
    }
	
	public static class ExecCommErrMsgThread extends Thread {
		
		BufferedReader errReader = null;
		StringBuffer errorMessage = null;

		public ExecCommErrMsgThread(BufferedReader errReader, StringBuffer errorMessage){
			this.errReader = errReader;
			this.errorMessage = errorMessage;
		}

	    @Override
	    public void run() {
	        try {
	        	String line = errReader.readLine();
	        	while((line != null) && !isInterrupted()) {
	        		errorMessage.append(line);
	        		line = errReader.readLine();
	        	}
	        } catch(IOException ex) {	
	        	ex.printStackTrace();
	        	LOGGER.info("SftpClientHandler.execCommand.ExecCommErrMsgThread:Error reading the error stream : " + ex);
	        }
	    }
	};
	
	public static class DebugLogger implements com.jcraft.jsch.Logger {
				
	    static Hashtable lh_para = new Hashtable();
	    
	    static{
	    	lh_para.put(new Integer(DEBUG), "DEBUG: ");
	    	lh_para.put(new Integer(INFO), "INFO: ");
	    	lh_para.put(new Integer(WARN), "WARN: ");
	    	lh_para.put(new Integer(ERROR), "ERROR: ");
	    	lh_para.put(new Integer(FATAL), "FATAL: ");
	    }

	    @Override
	    public boolean isEnabled(int level){
	      return true;
	    }

	    @Override
	    public void log(int level, String message){
	    	LOGGER.info("[SftpClientHandler] " + (String)lh_para.get(new Integer(level)) + message);
	    }
	    
	}


}
