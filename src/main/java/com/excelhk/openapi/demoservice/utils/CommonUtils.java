package com.excelhk.openapi.demoservice.utils;

import com.excelhk.openapi.demoservice.bean.Deposit;
import com.excelhk.openapi.demoservice.bean.FieldMapping;
import com.excelhk.openapi.demoservice.bean.Loan;
import com.excelhk.openapi.demoservice.bean.RateInfo;
import com.excelhk.openapi.demoservice.service.FieldMappingService;
import com.excelhk.openapi.demoservice.utils.constants.DemoConstants;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.RFC4180Parser;
import com.opencsv.RFC4180ParserBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author anita
 *
 */
@Configuration
public class CommonUtils {
	
	@Value("${sftp.localInpath}")
	private String localInpath;

	@Value("${sftp.localInpathBak}")
	private String localInpathBak;
	
	@Value("${sftp.localOutpath}")
	private String localOutpath;
	
	@Value("${sftp.file.delimiter:,}")
	private char fileDelimiter;
	
	@Value("${sftp.romteOut.file.conv:txt}")
	private String remoteOutFileConv;
	
	@Value("${sftp.remoteIn.file.conv:txt}")
	private String remoteInFileConv;
	
	private static Logger logger = LoggerFactory.getLogger(CommonUtils.class);

	@Autowired
	private FieldMappingService fieldMappingService;

	@Autowired
	private InterfaceFileUtils interfaceFileUtils;
	//private InterfaceFileProcess interfaceFileProcess;

	public String getFileName(String fileName) {
		IdWorker id = new IdWorker(1);
		return fileName + "." +id.nextId();
	}
	/**
	 *
	 * check if directory exists, if not, create it.
	 * @param dir
	 */
	public static void checkDirExist(String dir) {
		logger.info("dir " + dir);
		File fileDir = new File(dir);
		if(!fileDir.exists()) {
			fileDir.mkdirs();
		}
	}

	public static String getFullPath(String path){
		/*if(!path.endsWith(File.separator)) {
			return path + File.separator ;
		}else {
			return path;
		}*/
		logger.info("path " + path);
		String pathTmp = path;
		if(path.indexOf("/") >= 0 && !path.endsWith("/")  ) {
			logger.info("// " );
			pathTmp = path + "/";
		}else if(path.indexOf("\\") >= 0 && !path.endsWith("\\")  ) {
			logger.info("\\" );
			pathTmp = path + "\\";
		}else{
			//pathTmp = path + File.separator;
			pathTmp = path + "/";
		}
		logger.info("pathTmp " + pathTmp);
		return pathTmp;

	}

	public boolean checkFileExist(String fileName){

		return Files.exists(Paths.get(getFullPath(localInpath) + fileName + "." + remoteInFileConv));
	}
	/**
	 * Generate request file with content
	 * 
	 * @param fileName
	 * @param contentParam
	 * @return
	 */
	public boolean generateFile(String fileName, String contentParam) {
		logger.info("fileName " + fileName);
		logger.info("contentParam " + contentParam);
		if(fileName == null || contentParam == null) {
			logger.info("one of fileName and content is null" );
			return false;
		}
		String content="ReplyFileName=" + fileName;
		
		PrintWriter outContent = null;
		try{
			checkDirExist(localOutpath);
			fileName = fileName + "." + remoteOutFileConv;
			String localOutpathString = CommonUtils.getFullPath(localOutpath) + fileName;
			logger.info("localOutpathString " + localOutpathString);
			outContent = new PrintWriter(new BufferedWriter(new FileWriter(localOutpathString, false)), true);
			outContent.println(content + "." + remoteInFileConv);
			content = "Delimiter=" + fileDelimiter;
			outContent.println(content);
			content = contentParam;
			outContent.println(content);
			outContent.flush();
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}finally{
			
			if(outContent  !=null ) {
				outContent.close();
			}
		}
	}
	
	
	/**
	 * Generate request file by object
	 * 
	 * @param fileName
	 * @param obj
	 * @return
	 */
	public boolean generateFile(String fileName, Object obj) {
		
		logger.info("fileName " + fileName);
		logger.info("obj " + obj);
		if(fileName == null || obj == null) {
			logger.info("one of fileName and obj is null" );
			return false;
		}
		String content="ReplyFileName=" + fileName;
		PrintWriter outContent = null;
		try{
			checkDirExist(localOutpath);
			fileName = fileName + "." + remoteOutFileConv;
			String localOutpathString = CommonUtils.getFullPath(localOutpath) + fileName;
			logger.info("localOutpathString " + localOutpathString);
			outContent = new PrintWriter(new BufferedWriter(new FileWriter(localOutpathString, false)), true);
			outContent.println(content + "." + remoteInFileConv);
			content = "Delimiter=" + fileDelimiter;
			outContent.println(content);
			content ="Request data: ";
			outContent.println(content);
			if (obj instanceof RateInfo) {
				content = ((RateInfo)obj).getCcyCde()+ fileDelimiter+((RateInfo)obj).getRelvtCcyCde() + "\r\n";
				content +="Reply fields order: \r\n";
				content += getFieldDesc("EXCH_RATE");
			}else if (obj instanceof Loan) {
				content = ((Loan)obj).getProduct() + fileDelimiter + ((Loan)obj).getProdId() + "\r\n";
				content +="Reply fields order: \r\n";
				content += getFieldDesc(((Loan)obj).getProduct());

			}else if (obj instanceof Deposit) {
				content = ((Deposit)obj).getProduct() + fileDelimiter + ((Deposit)obj).getProdId() + "\r\n";
				content +="Reply fields order: \r\n";
				content += getFieldDesc( ((Deposit)obj).getProduct());
			}
			outContent.println(content);
			outContent.flush();
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}finally{
			
			if(outContent  !=null ) {
				outContent.close();
			}
		}
	}
	
	
	/**
	 * Decompose reply file for details
	 * 
	 * @param obj
	 * @param fileName
	 * @param detailsLst
	 */
	public void getDetailByFile(Object obj, String fileName, List detailsLst) {
		checkDirExist(localInpath);
		if(obj instanceof RateInfo   ) {
			getRateDetailByFile((RateInfo)obj, fileName, detailsLst);
		}else if(obj instanceof Loan) {
			getLoanDetailByFile((Loan)obj, fileName, detailsLst);
		}else if(obj instanceof Deposit) {
			getDepositDetailByFile((Deposit)obj, fileName, detailsLst);
		}

	}

	
	/**
	 * Decompose reply file for product ID
	 * 
	 * 
	 * @param prodLst
	 * @param fileName
	 * @param obj
	 */
	public void getProdsByFile(List prodLst, String fileName, Object obj) {
		CSVReader reader = null;
		String localInpathString = null;
		String localInpathBakFile = null;
		String[] nextLine;
		try{
			checkDirExist(localInpath);
			checkDirExist(localInpathBak);
			fileName = fileName + "." + remoteInFileConv;
			localInpathString = CommonUtils.getFullPath(localInpath) + fileName;
			localInpathBakFile = CommonUtils.getFullPath(localInpathBak) + fileName;
			logger.info("localInpathString " + localInpathString);
			
			//reader = new CSVReader(new FileReader(localInpathString));
			RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder().withSeparator(fileDelimiter).build();
			reader = new CSVReaderBuilder(new FileReader(localInpathString)).withCSVParser(rfc4180Parser).build();
			while((nextLine=reader.readNext()) != null) {
				logger.info("nextLine.length : " +nextLine.length);
				if(obj instanceof Loan) {
					((Loan) obj).setProdId(nextLine[0]);
					prodLst.add(obj);
					obj = new Loan();
				}else if (obj instanceof Deposit) {
					((Deposit) obj).setProdId(nextLine[0]);
					prodLst.add(obj);
					obj = new Deposit();
				}
			}

		}catch(FileNotFoundException ex) {
			logger.error("File " + fileName +" not found!" );
			return;
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//back up file
			backup(localInpathString, localInpathBakFile);
		}
	
	}

	/**
	 * Decompose reply file for Deposit details
	 * 
	 * @param info
	 * @param fileName
	 * @param detailsLst
	 */
	public void getDepositDetailByFile(Deposit info, String fileName, List detailsLst) {
		String localInpathString = null;
		String localInpathBakFile = null;
		CSVReader reader = null;
		String[] nextLine;
		try{
			checkDirExist(localInpath);
			checkDirExist(localInpathBak);
			fileName = fileName + "." + remoteInFileConv;
			localInpathString = CommonUtils.getFullPath(localInpath) + fileName;
			localInpathBakFile = CommonUtils.getFullPath(localInpathBak) + fileName;
			logger.info("localInpathString " + localInpathString);
			//reader = new CSVReader(new FileReader(localInpathString));
			RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder().withSeparator(fileDelimiter).build();
			reader = new CSVReaderBuilder(new FileReader(localInpathString)).withCSVParser(rfc4180Parser).build();
			while((nextLine=reader.readNext()) != null) {
				logger.info("nextLine.length : " +nextLine.length);
				//info
				if(info.getProdId() == null) {
					info.setProdId(nextLine[0]);
				}
				if (nextLine.length > 2) {
					info.setProduct(nextLine[1]);
				}
				if (nextLine.length > 2) {
					info.setType(nextLine[2]);
				}
				if (nextLine.length > 3) {
					info.setSubtype(nextLine[3]);
				}
				if (nextLine.length > 4) {
					info.setCurrency(nextLine[4]);
				}
				if (nextLine.length > 5) {
					info.setInterestRate(nextLine[5]);
				}
				if (nextLine.length > 6) {
					info.setMinamount(nextLine[6]);
				}
				if (nextLine.length > 7) {
					info.setFee(nextLine[7]);
				}
				if (nextLine.length > 8) {
					info.setRemark(removeLFChar(nextLine[8]));
				}
				detailsLst.add(info);
				info = new Deposit();
			}

		}catch(FileNotFoundException ex) {
			logger.error("File " + fileName +" not found!" );
			return;
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//back up file
			backup(localInpathString, localInpathBakFile);
		}
	}
	
	/**
	 * Decompose reply file for Loan details
	 * 
	 * @param info
	 * @param fileName
	 * @param detailsLst
	 */
	public void getLoanDetailByFile(Loan info, String fileName, List detailsLst) {
		String localInpathString = null;
		String localInpathBakFile = null;
		CSVReader reader = null;
		String[] nextLine;
		try{
			checkDirExist(localInpath);
			checkDirExist(localInpathBak);
			fileName = fileName + "." + remoteInFileConv;
			localInpathString = CommonUtils.getFullPath(localInpath) + fileName;
			localInpathBakFile = CommonUtils.getFullPath(localInpathBak) + fileName;
			logger.info("localInpathString " + localInpathString);
			//reader = new CSVReader(new FileReader(localInpathString));
			RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder().withSeparator(fileDelimiter).build();
			reader = new CSVReaderBuilder(new FileReader(localInpathString)).withCSVParser(rfc4180Parser).build();
			while((nextLine=reader.readNext()) != null) {
				logger.info("nextLine.length : " +nextLine.length);
				if(info.getProdId() == null) {
					info.setProdId(nextLine[0]);
				}
				if (nextLine.length > 1) {
					info.setProduct(nextLine[1]);
				}
				if (nextLine.length > 2) {
					info.setType(nextLine[2]);
				}
				if (nextLine.length > 3) {
					info.setSubtype(nextLine[3]);
				}
				if (nextLine.length > 4) {
					info.setInterestRate(nextLine[4]);
				}
				if (nextLine.length > 5) {
					info.setPrdinfo1(removeLFChar(nextLine[5]));
				}
				if (nextLine.length > 6) {
					info.setPrdinfo2(removeLFChar(nextLine[6]));
				}
				if (nextLine.length > 7) {
					info.setPrdinfo3(removeLFChar(nextLine[7]));
				}
				if (nextLine.length > 8) {
					info.setFee(nextLine[8]);
				}
				if (nextLine.length > 9) {
					info.setRemark(removeLFChar(nextLine[9]));
				}
				detailsLst.add(info);
				info = new Loan();
			}

		}catch(FileNotFoundException ex) {
			logger.error("File " + fileName +" not found!" );
			return;
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//back up file
			backup(localInpathString, localInpathBakFile);
		}
	}
	
	/**
	 * Decompose reply file for Rate details
	 * 
	 * @param info
	 * @param fileName
	 * @param detailsLst
	 */
	public void getRateDetailByFile(RateInfo info, String fileName, List detailsLst) {
		String localInpathString = null;
		String localInpathBakFile = null;
		CSVReader reader = null;
		String[] nextLine;
		try{
			checkDirExist(localInpath);
			checkDirExist(localInpathBak);
			fileName = fileName + "." + remoteInFileConv;
			localInpathString = CommonUtils.getFullPath(localInpath) + fileName;
			localInpathBakFile = CommonUtils.getFullPath(localInpathBak) + fileName;
			logger.info("localInpathString " + localInpathString);
			//reader = new CSVReader(new FileReader(localInpathString));
			RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder().withSeparator(fileDelimiter).build();
			reader = new CSVReaderBuilder(new FileReader(localInpathString)).withCSVParser(rfc4180Parser).build();
			String ccy = info.getCcyCde();
			String relvtCcy = info.getRelvtCcyCde();
			while((nextLine=reader.readNext()) != null) {
				logger.info("nextLine.length : " +nextLine.length);
				//info
				if(info.getCcyCde() == null) {
					info.setCcyCde(ccy);
					info.setRelvtCcyCde(relvtCcy);
				}
				if (nextLine.length > 1) {
					info.setBid(new BigDecimal(nextLine[1]));
				}
				if (nextLine.length > 2) {
					info.setMid(new BigDecimal(nextLine[2]));
				}
				if (nextLine.length > 3) {
					info.setAsk(new BigDecimal(nextLine[3]));
				}
				if (nextLine.length > 4) {
					info.setFeedSource(nextLine[4]);
				}
				if (nextLine.length > 5) {
					info.setLastDate(nextLine[5]);
				}
				detailsLst.add(info);
				info = new RateInfo();
			}

		}catch(FileNotFoundException ex) {
			logger.error("File " + fileName +" not found!" );
			return;
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//back up file
			backup(localInpathString, localInpathBakFile);
		}
	}
	
	public SftpException handleErr(Exception e) {
		int errCode = 500;
		String errMsg;
		String msg = e.getMessage();
		if (e instanceof JSchException) {
			logger.error("login or connect error. error message: " + msg);
			if(msg != null) {
				if (msg.contains(DemoConstants.AUTH_FAIL)) {
					errMsg = "ftp login failed";
				}else if (msg.contains(DemoConstants.CONN_TIMED_OUT) || msg.contains(DemoConstants.TIME_OUT)) {
					errMsg = "ftp Connection timed out";
				}else if (msg.contains(DemoConstants.CONN_REFUSED)) {
					errMsg = "ftp Connection refused";
				}else {
					errMsg = "ftp Connection Exception";
				}
			}else {
				errMsg = "ftp Connection Exception";
			}
		}else if (e instanceof SftpException) {
			logger.error("ChannelSftp " + msg);
			if(((SftpException) e).id == ChannelSftp.SSH_FX_NO_CONNECTION || ((SftpException) e).id == ChannelSftp.SSH_FX_CONNECTION_LOST ){
				msg = "Connection error";
			}else {
				msg = "Internal Server error";
			}
			errMsg = "ftp error: " + msg;
		}else {
			logger.error("other error message: " + msg);
			msg = "Internal Server error";
			errMsg = msg;
		}
		return new SftpException(errCode, errMsg);
	}

	public  Object responseFtpError(Object obj){
		return responseFtpError(null, obj);
	}
	public Object responseFtpError(String prod , Object obj){
		try {
			if(StringUtils.isEmpty(prod)) {
				//return interfaceFileProcess.getDetails(obj);
				return interfaceFileUtils.getDetails(obj);
			} else {
				//return interfaceFileProcess.getProds(prod,obj);
				return interfaceFileUtils.getProds(prod,obj);
			}
		} catch (SftpException e) {
			Map<String, String> map = new HashMap<String, String>(1);
			map.put("error", e.getMessage());
			logger.error("responseFtpError " +  e.getMessage());
			return new ResponseEntity<Object>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public String removeLFChar(String strParam) {
		String strLine = strParam;
		if(strParam.contains(DemoConstants.LINE_BREAK)) {
			strLine = strParam.replaceAll(" \r\n", " ");
			strLine = strLine.replaceAll("\r\n", " ");
		}else if(strParam.contains(DemoConstants.NEW_LINE)) {
			strLine = strParam.replaceAll(" \n", " ");
			strLine = strLine.replaceAll("\n", " ");
		}
		return strLine;
	}

	public List<String> getFieldDescList(String product){

		List<FieldMapping> listResult = fieldMappingService.findAllByProductAndShowOrderByOrder(product, true);

		List<String> fieldNameLst = new ArrayList<String>();
		for (FieldMapping fieldMapping: listResult
		) {
			fieldNameLst.add(fieldMapping.getFieldDesc());
		}

		return fieldNameLst;
	}

	public String getFieldDesc(String product){
		String content="";
		List<String>  fieldDescLst = getFieldDescList(product);
		for (int i = 0; i < fieldDescLst.size(); i++) {
			String fieldDesc = fieldDescLst.get(i);
			if(i != fieldDescLst.size() - 1) {
				content += fieldDesc + fileDelimiter;
			} else {
				content += fieldDesc;
			}
		}
		return content;
	}

	public void backup(String path, String bakPath){
		logger.info("backup path: " + path );
		logger.info("backup bakPath: " + bakPath );
		File fileIn = new File(path);
		File fileInBak = new File(bakPath);
		if(fileIn.renameTo(fileInBak)){
			logger.info("backup file successfully" );
		}else{
			logger.info("backup file failed" );
		}
	}
}
