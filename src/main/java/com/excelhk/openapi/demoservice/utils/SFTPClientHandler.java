package com.excelhk.openapi.demoservice.utils;

import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

public class SFTPClientHandler {

	
	private ChannelSftp v_sFTPClient = null;
	private static int vi_port = 22;
	private static JSch v_jsch = null;	
	private Session v_sshSession = null;
	private Channel v_channel = null;
	private static String vs_hostName;
	private static Logger LOGGER = LoggerFactory.getLogger(SFTPClientHandler.class);

	public static SFTPClientHandler getInstance(String as_hostname) throws Exception{
		LOGGER.info("Create SFTPClientHandler " + as_hostname);
		vs_hostName = as_hostname;			
		v_jsch = new JSch();
		return new SFTPClientHandler();
	}
	
	public static SFTPClientHandler getInstance(String as_hostname, int ai_port) throws Exception{
		vi_port = ai_port;
		return getInstance(as_hostname);
	}
	
	public void chdir(String as_dstpath) throws Exception{
		v_sFTPClient.cd(as_dstpath);
	}
	
	public void mkdir(String as_dirPath) throws IOException, SftpException{
		try{
			v_sFTPClient.mkdir(as_dirPath);
		}catch(SftpException le_ex){
			le_ex.printStackTrace();
			throw le_ex;
		}
	}
	
	public void delete(String as_outDest) throws IOException, SftpException{
		try{
			v_sFTPClient.rm(as_outDest);
		}catch(SftpException le_ex){
			le_ex.printStackTrace();
			throw le_ex;
		}
	}	
	
	public void rmdir(String as_outDest) throws IOException, SftpException{
		try{
			v_sFTPClient.rmdir(as_outDest);
		}catch(SftpException le_ex){
			le_ex.printStackTrace();
			throw le_ex;
		}
	}

	public void debugResponses(boolean as_dRes) throws Exception{
		if (as_dRes == true){
			JSch.setLogger(new DebugLogger());
		}else{
			JSch.setLogger(null);
		}
	}

	public void login(String as_userName, String as_password) throws Exception{
		this.login(as_userName, as_password, vi_port);
	}

	public void put(String as_outFile, String as_fileName, boolean ab_mode) throws IOException, SftpException{
		int li_mode = ab_mode ? ChannelSftp.APPEND : ChannelSftp.OVERWRITE;
		try{
			File lf_file = new File(as_outFile);
			FileInputStream lfo_file = new FileInputStream(lf_file);
			v_sFTPClient.put(lfo_file, as_fileName, li_mode);
			try{
				lfo_file.close();
			}catch(Exception le_ex){	
			}
		}catch(SftpException le_ex){
			le_ex.printStackTrace();
			throw le_ex;
		}
	}
	
	public void put(String as_outFile, String as_fileName) throws IOException, SftpException{
		this.put(as_outFile, as_fileName, false);
	}
	
	public void get(String as_inFile, String as_fileName) throws Exception{
		File lf_file = new File(as_inFile);
		FileOutputStream lfo_file = new FileOutputStream(lf_file);
		v_sFTPClient.get(as_fileName, lfo_file);
		try{
			lfo_file.close();
		}catch(Exception le_ex){	
		}
	}
	
	public String[] dir(String as_tag) throws Exception{
		
		String[] lsa_fileNameList;
		Vector lv_allFile = null;
		String ls_split = ",";
		String ls_fielNameList = "";
		
		if ((as_tag.indexOf("/") >= 0) || (as_tag.indexOf("\\") >= 0)){
			lv_allFile = v_sFTPClient.ls(as_tag);
			as_tag = "*.*";
		}else{
			if ((as_tag != null) && (as_tag.indexOf(".")) < 0){
				as_tag = as_tag + ".*";
			}
		
			lv_allFile = v_sFTPClient.ls(".");
		}
		for (int li_count=0;li_count < lv_allFile.size();li_count++ ){
			Object lo_obj = lv_allFile.elementAt(li_count);			
			if(lo_obj instanceof LsEntry){
				LsEntry lo_entry = (LsEntry)lo_obj;
				String ls_tmpFileName = lo_entry.getFilename();				               
                if ((ls_tmpFileName == null) || (ls_tmpFileName.equals("")) 
                		|| (ls_tmpFileName.equals(".")) || (ls_tmpFileName.equals(".."))){
                	continue;
                }

                //List all files
                if ((as_tag.equals("*")) || (as_tag.equals("*.*"))){
                	if ((ls_fielNameList == null) || (ls_fielNameList.equals(""))){
                		ls_fielNameList = ls_tmpFileName;
                	}else{
                		ls_fielNameList = ls_fielNameList + ls_split + ls_tmpFileName;
                	}
                } else if (as_tag.indexOf('.')>= 0) {
                	int li_pos = as_tag.lastIndexOf('.');
                	String ls_mchPrefix = as_tag;
                	String ls_mchExt = "";
                	if (li_pos >= 0){
                		ls_mchPrefix = as_tag.substring(0, li_pos);                	
                		ls_mchExt = as_tag.substring(li_pos + 1);
                	}
                	
                	String ls_filePrefix = ls_tmpFileName;
                	String ls_fileExt = "";
                	li_pos = ls_tmpFileName.lastIndexOf('.');
                	if (li_pos >= 0){
                		ls_filePrefix = ls_tmpFileName.substring(0, li_pos);
                		ls_fileExt = ls_tmpFileName.substring(li_pos + 1);
                	}
                	if (matchStr(ls_filePrefix, ls_mchPrefix) && matchStr(ls_fileExt, ls_mchExt)){
                		if ((ls_fielNameList == null) || (ls_fielNameList.equals(""))){
                    		ls_fielNameList = ls_tmpFileName;
                    	}else{
                    		ls_fielNameList = ls_fielNameList + ls_split + ls_tmpFileName;
                    	}
                	}
                } else {
                	//List appointed file
                	if (ls_tmpFileName.equals(as_tag)){
                		ls_fielNameList = ls_tmpFileName;
                	}
                }
            
            }
		}

		if ((ls_fielNameList != null) && (!ls_fielNameList.equals(""))){
			lsa_fileNameList = ls_fielNameList.split(ls_split);
		}else{
			lsa_fileNameList = new String[0];
		}
		try{
			Arrays.sort(lsa_fileNameList);
		}catch(Exception le_ex){
			
		}
		return lsa_fileNameList;
	} 
	
	private static boolean matchStr(String as_srcStr, String as_mchStr) throws Exception{
		boolean lb_result = false;
		String ls_tmpStr = "";
		
		if (as_mchStr.indexOf("*") == -1) {
			if (as_srcStr.equalsIgnoreCase(as_mchStr)){
				lb_result = true;
			}
		} else if (as_mchStr.startsWith("*")){
			ls_tmpStr = as_mchStr.substring(1); 
			if (as_srcStr.endsWith(ls_tmpStr)){
				lb_result = true;
			}
		} else if (as_mchStr.endsWith("*")){
			ls_tmpStr = as_mchStr.substring(0, as_mchStr.length() - 1);
			if (as_srcStr.startsWith(ls_tmpStr)){
				lb_result = true;
			}
		} else {	
			int li_count = as_mchStr.indexOf("*");
			if ((as_srcStr.startsWith(as_mchStr.substring(0,li_count))) && (as_srcStr.endsWith(as_mchStr.substring(li_count + 1)))){
				lb_result = true;
			}
		}
		
		return lb_result;
	}
	
	public void rename(String as_oldName, String as_newName) throws Exception{
		v_sFTPClient.rename(as_oldName, as_newName);
	}
	
	public String system() throws Exception{
		String ls_result = execCommand("uname");
		return ls_result; 
	}
	
	public String execCommand(String as_command) throws Exception{
		if (v_sshSession == null){
			throw new Exception("No SFTP has been connected.");
		}
		
		ChannelExec lce_channelExec = (ChannelExec)v_sshSession.openChannel("exec");
		lce_channelExec.setCommand(as_command);
		lce_channelExec.setInputStream(null);
		lce_channelExec.connect();
				
		final BufferedReader lbr_errReader = new BufferedReader(new InputStreamReader(((ChannelExec)lce_channelExec).getErrStream()));
	    BufferedReader lbr_inReader = new BufferedReader(new InputStreamReader(lce_channelExec.getInputStream()));
		
	    final StringBuffer lsb_errorMessage = new StringBuffer();
		ExecCommErrMsgThread lt_errorThread = new ExecCommErrMsgThread(lbr_errReader, lsb_errorMessage);

		try {
		      lt_errorThread.start();
		} catch (IllegalStateException le_ex) {		
			le_ex.printStackTrace();
			LOGGER.info("SFTPClientHandler.execCommand.errorThread:Error message : " + le_ex);
		}
		
		String ls_outContents = "";
		try {
			ls_outContents = parseExecResult(lbr_inReader);			

		    if(lce_channelExec.isClosed()) {
		    	int li_exitCode = lce_channelExec.getExitStatus();
		    	LOGGER.info("SFTPClientHandler.execCommand.execChannel closed,exitCode = " + li_exitCode);
		    }

		    try {
		        // make sure that the error thread exits
		    	lt_errorThread.join();
		    } catch (InterruptedException le_ex) {		    	
		    	LOGGER.info("SFTPClientHandler.execCommand.wait thread excetpion = " + le_ex);
		    }
		    
		} catch (IOException le_ex) {
			le_ex.printStackTrace();
			throw new IOException(le_ex.toString());
		}finally {
			try {
				lbr_inReader.close();
			} catch (IOException le_ex) {
				LOGGER.info("SFTPClientHandler.execCommand.close read string excetpion = " + le_ex);
			}

		    try {
		    	lbr_errReader.close();
		    } catch (IOException le_ex) {
		    	LOGGER.info("SFTPClientHandler.execCommand.close error string excetpion = " + le_ex);
		    }
		    
		    if ((lsb_errorMessage.toString() != null) && (!lsb_errorMessage.toString().equals(""))){
				throw new Exception(lsb_errorMessage.toString());
			}

		    lce_channelExec.disconnect();
		}    
		return ls_outContents.trim();
	}
		    
	//Parse output string
	protected String parseExecResult(BufferedReader abr_lines) throws IOException {
		StringBuffer lsb_output = new StringBuffer();
		char[] lca_buf = new char[512];
		int li_readCount;
		
		while ( (li_readCount = abr_lines.read(lca_buf, 0, lca_buf.length)) > 0 ) {
			lsb_output.append(lca_buf, 0, li_readCount);
		}

		return lsb_output.toString();
	}

	
	public void quit() throws Exception{
		v_sFTPClient.quit();
    	v_channel.disconnect();
    	v_sshSession.disconnect();
	}

	
	public void login(String as_userName, String as_password, int ai_port) throws Exception{
   		v_sshSession = v_jsch.getSession(as_userName, vs_hostName, ai_port);
   		v_sshSession.setPassword(as_password);
    		
   		Properties lp_sshConfig = new Properties();
   		lp_sshConfig.setProperty("StrictHostKeyChecking", "no");
   		lp_sshConfig.setProperty("PreferredAuthentications", "password");
   		v_sshSession.setConfig(lp_sshConfig);
   		v_sshSession.setTimeout(30000);
   		v_sshSession.connect();
    		
   		v_channel = v_sshSession.openChannel("sftp");
   		v_channel.connect();
   		v_sFTPClient = (ChannelSftp)v_channel;
   		LOGGER.info("SFTPClientHandler login successfully, JSch version = " + v_sFTPClient.version());
    }
	
	public static class ExecCommErrMsgThread extends Thread {
		
		BufferedReader vbr_errReader = null;
		StringBuffer vsb_errorMessage = null;
		
		public ExecCommErrMsgThread(BufferedReader abr_errReader, StringBuffer asb_errorMessage){
			vbr_errReader = abr_errReader;
			vsb_errorMessage = asb_errorMessage;
		}
		
	    @Override
	    public void run() {
	        try {
	        	String ls_line = vbr_errReader.readLine();
	        	while((ls_line != null) && !isInterrupted()) {
	        		vsb_errorMessage.append(ls_line);
	        		ls_line = vbr_errReader.readLine();		        		
	        	}
	        } catch(IOException le_ex) {	
	        	le_ex.printStackTrace();
	        	LOGGER.info("SFTPClientHandler.execCommand.ExecCommErrMsgThread:Error reading the error stream : " + le_ex);
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
	    
	    public boolean isEnabled(int ai_level){
	      return true;
	    }
	    
	    public void log(int ai_level, String as_message){
	    	LOGGER.info("[SFTPClientHandler] " + (String)lh_para.get(new Integer(ai_level)) + as_message);
	    }
	    
	}


}
