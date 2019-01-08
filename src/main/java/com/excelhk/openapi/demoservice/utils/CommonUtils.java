package com.excelhk.openapi.demoservice.utils;

import com.excelhk.openapi.demoservice.bean.Deposit;
import com.excelhk.openapi.demoservice.bean.FieldMapping;
import com.excelhk.openapi.demoservice.bean.Loan;
import com.excelhk.openapi.demoservice.bean.RateInfo;
import com.excelhk.openapi.demoservice.service.FieldMappingService;
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

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class CommonUtils {
	
	@Value("${sftp.localInpath}")
	private String localInpath;
	
	@Value("${sftp.localOutpath}")
	private String localOutpath;
	
	@Value("${sftp.file.delimiter:,}")
	private char fileDelimiter;
	
	@Value("${sftp.romteOut.file.conv:txt}")
	private String remoteOutFileConv;
	
	@Value("${sftp.remoteIn.file.conv:txt}")
	private String remoteInFileConv;
	
	private static Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

	@Autowired
	private FieldMappingService fieldMappingService;

	public String getFileName(String as_Filename) {
		IdWorker id = new IdWorker(1);
		return as_Filename + "." +id.nextId();
	}
	
	
	/**
	 * check if directory exists, if not, create it.
	 */
	public void checkDirExist(String as_Dir) {
		LOGGER.info("as_Dir " + as_Dir);
		File filedir = new File(as_Dir);
		if(!filedir.exists()) {
			filedir.mkdirs();
		}
	}
	
	/**
	 * Generate request file with content
	 * 
	 * @param as_FileName
	 * @param as_content
	 * @return
	 */
	public boolean generateFile(String as_FileName, String as_content) {
		LOGGER.info("as_FileName " + as_FileName);
		LOGGER.info("as_content " + as_content);
		if(as_FileName == null || as_content == null) {
			LOGGER.info("one of as_FileName and as_content is null" );
			return false;
		}
		String ls_context="ReplyFileName=" + as_FileName;
		
		PrintWriter lPw_Out = null;
		try{
			String ls_localOutpath = null;
			checkDirExist(localOutpath);
			if(!localOutpath.endsWith(File.separator)) {
				ls_localOutpath = localOutpath + File.separator + as_FileName + "." + remoteOutFileConv;
			}else {
				ls_localOutpath = localOutpath + as_FileName + "." + remoteOutFileConv;;
			}
			LOGGER.info("ls_localOutpath " + ls_localOutpath);
			lPw_Out = new PrintWriter(new BufferedWriter(new FileWriter(ls_localOutpath, false)), true);
			lPw_Out.println(ls_context + "." + remoteInFileConv);
			ls_context = "Delimiter=" + fileDelimiter;
			lPw_Out.println(ls_context);
			ls_context = as_content;
			lPw_Out.println(ls_context);
			lPw_Out.flush();
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}finally{
			
			if(lPw_Out  !=null )
				lPw_Out.close(); 
		}
	}
	
	
	/**
	 * Generate request file by object
	 * 
	 * @param as_FileName
	 * @param obj
	 * @return
	 */
	public boolean generateFile(String as_FileName, Object obj) {
		
		LOGGER.info("as_FileName " + as_FileName);
		LOGGER.info("obj " + obj);
		if(as_FileName == null || obj == null) {
			LOGGER.info("one of as_FileName and obj is null" );
			return false;
		}
		String ls_context="ReplyFileName=" + as_FileName;
		PrintWriter lPw_Out = null;
		try{
			String ls_localOutpath = null;
			checkDirExist(localOutpath);
			if(!localOutpath.endsWith(File.separator)) {
				ls_localOutpath = localOutpath + File.separator + as_FileName + "." + remoteOutFileConv;
			}else {
				ls_localOutpath = localOutpath + as_FileName + "." + remoteOutFileConv;
			}
			LOGGER.info("ls_localOutpath " + ls_localOutpath);
			lPw_Out = new PrintWriter(new BufferedWriter(new FileWriter(ls_localOutpath, false)), true);
			lPw_Out.println(ls_context + "." + remoteInFileConv);
			ls_context = "Delimiter=" + fileDelimiter;
			lPw_Out.println(ls_context);
			ls_context ="Request data: ";
			lPw_Out.println(ls_context);
			if (obj instanceof RateInfo) {
				ls_context = ((RateInfo)obj).getCcy_Cde()+ fileDelimiter+((RateInfo)obj).getRelvt_Ccy_Cde() + "\r\n";
				ls_context +="Reply fields order: \r\n";
				ls_context += getFieldDesc("EXCH_RATE");
			}else if (obj instanceof Loan) {
				ls_context = ((Loan)obj).getProduct() + fileDelimiter + ((Loan)obj).getProdId() + "\r\n";
				ls_context +="Reply fields order: \r\n";
				ls_context += getFieldDesc(((Loan)obj).getProduct());

			}else if (obj instanceof Deposit) {
				ls_context = ((Deposit)obj).getProduct() + fileDelimiter + ((Deposit)obj).getProdId() + "\r\n";
				ls_context +="Reply fields order: \r\n";
				ls_context += getFieldDesc( ((Deposit)obj).getProduct());
			}
			lPw_Out.println(ls_context);
			lPw_Out.flush();
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}finally{
			
			if(lPw_Out  !=null )
				lPw_Out.close(); 
		}
	}
	
	
	/**
	 * Decompose reply file for details
	 * 
	 * @param obj
	 * @param as_Filename
	 * @param a_Lst
	 */
	public void getDetailByFile(Object obj, String as_Filename, List a_Lst) {
		checkDirExist(localInpath);
		if(obj instanceof RateInfo   ) {
			getRateDetailByFile((RateInfo)obj, as_Filename, a_Lst);
		}else if(obj instanceof Loan) {
			getLoanDetailByFile((Loan)obj, as_Filename, a_Lst);
		}else if(obj instanceof Deposit) {
			getDepositDetailByFile((Deposit)obj, as_Filename, a_Lst);
		}

	}

	
	/**
	 * Decompose reply file for product ID
	 * 
	 * 
	 * @param a_ProdLst
	 * @param as_Filename
	 * @param obj
	 */
	public void getProdsByFile(List a_ProdLst, String as_Filename, Object obj) {
		CSVReader reader = null;
		String[] ls_NextLine;
		try{
			String ls_localInpath = null;
			checkDirExist(localInpath);
			if(!localInpath.endsWith(File.separator)) {
				ls_localInpath = localInpath + File.separator + as_Filename + "." + remoteInFileConv;
			}else {
				ls_localInpath = localInpath + as_Filename + "." + remoteInFileConv;;
			}
			LOGGER.info("ls_localInpath " + ls_localInpath);
			
			//reader = new CSVReader(new FileReader(ls_localInpath));
			RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder().withSeparator(fileDelimiter).build();
			reader = new CSVReaderBuilder(new FileReader(ls_localInpath)).withCSVParser(rfc4180Parser).build();
			while((ls_NextLine=reader.readNext()) != null) {
				LOGGER.info("ls_NextLine.length : " +ls_NextLine.length);
				if(obj instanceof Loan) {
					((Loan) obj).setProdId(ls_NextLine[0]);
					a_ProdLst.add(obj);
					obj = new Loan();
				}else if (obj instanceof Deposit) {
					((Deposit) obj).setProdId(ls_NextLine[0]);
					a_ProdLst.add(obj);
					obj = new Deposit();
				}
			}
		}catch(FileNotFoundException ex) {
			LOGGER.error("File " + as_Filename +" not found!" );
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
		}
	
	}

	/**
	 * Decompose reply file for Deposit details
	 * 
	 * @param info
	 * @param as_Filename
	 * @param a_Lst
	 */
	public void getDepositDetailByFile(Deposit info, String as_Filename, List a_Lst) {
		
		CSVReader reader = null;
		String[] ls_NextLine;
		try{
			String ls_localInpath = null;
			if(!localInpath.endsWith(File.separator)) {
				ls_localInpath = localInpath + File.separator + as_Filename + "." + remoteInFileConv;;
			}else {
				ls_localInpath = localInpath + as_Filename + "." + remoteInFileConv;;
			}
			LOGGER.info("ls_localInpath " + ls_localInpath);
			//reader = new CSVReader(new FileReader(ls_localInpath));
			RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder().withSeparator(fileDelimiter).build();
			reader = new CSVReaderBuilder(new FileReader(ls_localInpath)).withCSVParser(rfc4180Parser).build();
			while((ls_NextLine=reader.readNext()) != null) {
				LOGGER.info("ls_NextLine.length : " +ls_NextLine.length);
				//info
				if(info.getProdId() == null)
						info.setProdId(ls_NextLine[0]);
				if (ls_NextLine.length > 2)
					info.setProduct(ls_NextLine[1]);
				if (ls_NextLine.length > 2)
					info.setType(ls_NextLine[2]);
				if (ls_NextLine.length > 3)
					info.setSubtype(ls_NextLine[3]);
				if (ls_NextLine.length > 4)
					info.setCurrency(ls_NextLine[4]);
				if (ls_NextLine.length > 5)
					info.setInterestRate(ls_NextLine[5]);
				if (ls_NextLine.length > 6)
					info.setMinamount(ls_NextLine[6]);
				if (ls_NextLine.length > 7)
					info.setFee(ls_NextLine[7]);
				if (ls_NextLine.length > 8)
					info.setRemark(removeLFChar(ls_NextLine[8]));
				a_Lst.add(info);
				info = new Deposit();
			}
		}catch(FileNotFoundException ex) {
			LOGGER.error("File " + as_Filename +" not found!" );
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
		}
	}
	
	/**
	 * Decompose reply file for Loan details
	 * 
	 * @param info
	 * @param as_Filename
	 * @param a_Lst
	 */
	public void getLoanDetailByFile(Loan info, String as_Filename, List a_Lst) {
		
		CSVReader reader = null;
		String[] ls_NextLine;
		try{
			String ls_localInpath = null;
			if(!localInpath.endsWith(File.separator)) {
				ls_localInpath = localInpath + File.separator + as_Filename + "." + remoteInFileConv;;
			}else {
				ls_localInpath = localInpath + as_Filename + "." + remoteInFileConv;;
			}
			LOGGER.info("ls_localInpath " + ls_localInpath);
			//reader = new CSVReader(new FileReader(ls_localInpath));
			RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder().withSeparator(fileDelimiter).build();
			reader = new CSVReaderBuilder(new FileReader(ls_localInpath)).withCSVParser(rfc4180Parser).build();
			while((ls_NextLine=reader.readNext()) != null) {
				LOGGER.info("ls_NextLine.length : " +ls_NextLine.length);
				if(info.getProdId() == null)
					info.setProdId(ls_NextLine[0]);
				if (ls_NextLine.length > 1)
					info.setProduct(ls_NextLine[1]);
				if (ls_NextLine.length > 2)	
					info.setType(ls_NextLine[2]);
				if (ls_NextLine.length > 3)					
					info.setSubtype(ls_NextLine[3]);
				if (ls_NextLine.length > 4)
					info.setInterestRate(ls_NextLine[4]);
				if (ls_NextLine.length > 5)
					info.setPrdinfo1(removeLFChar(ls_NextLine[5]));
				if (ls_NextLine.length > 6)	
					info.setPrdinfo2(removeLFChar(ls_NextLine[6]));
				if (ls_NextLine.length > 7)
					info.setPrdinfo3(removeLFChar(ls_NextLine[7]));
				if (ls_NextLine.length > 8)
					info.setFee(ls_NextLine[8]);
				if (ls_NextLine.length > 9)
					info.setRemark(removeLFChar(ls_NextLine[9]));
				a_Lst.add(info);
				info = new Loan();
			}
		}catch(FileNotFoundException ex) {
			LOGGER.error("File " + as_Filename +" not found!" );
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
		}
	}
	
	/**
	 * Decompose reply file for Rate details
	 * 
	 * @param info
	 * @param as_Filename
	 * @param a_Lst
	 */
	public void getRateDetailByFile(RateInfo info, String as_Filename, List a_Lst) {
		
		CSVReader reader = null;
		String[] ls_NextLine;
		try{
			String ls_localInpath = null;
			if(!localInpath.endsWith(File.separator)) {
				ls_localInpath = localInpath + File.separator + as_Filename + "." + remoteInFileConv;;
			}else {
				ls_localInpath = localInpath + as_Filename + "." + remoteInFileConv;;
			}
			LOGGER.info("ls_localInpath " + ls_localInpath);
			//reader = new CSVReader(new FileReader(ls_localInpath));
			RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder().withSeparator(fileDelimiter).build();
			reader = new CSVReaderBuilder(new FileReader(ls_localInpath)).withCSVParser(rfc4180Parser).build();
			String ls_Ccy = info.getCcy_Cde();
			String ls_RelvtCcy = info.getRelvt_Ccy_Cde();
			while((ls_NextLine=reader.readNext()) != null) {
				LOGGER.info("ls_NextLine.length : " +ls_NextLine.length);
				//info
				if(info.getCcy_Cde() == null) {
					info.setCcy_Cde(ls_Ccy);
					info.setRelvt_Ccy_Cde(ls_RelvtCcy);
				}
				if (ls_NextLine.length > 1)
					info.setBid(new BigDecimal(ls_NextLine[1]));
				if (ls_NextLine.length > 2)
					info.setMid(new BigDecimal(ls_NextLine[2]));
				if (ls_NextLine.length > 3)
					info.setAsk(new BigDecimal(ls_NextLine[3]));
				if (ls_NextLine.length > 4)
					info.setFeed_Source(ls_NextLine[4]);
				if (ls_NextLine.length > 5)
					info.setLastDate(ls_NextLine[5]);
				a_Lst.add(info);
				info = new RateInfo();
			}
		}catch(FileNotFoundException ex) {
			LOGGER.error("File " + as_Filename +" not found!" );
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
		}
	}
	
	public SftpException handleErr(Exception e) {
		int errCode = 500;
		String errMsg;
		String msg = e.getMessage();
		if (e instanceof JSchException) {
			LOGGER.error("login or connect error. error message: " + msg);
			if(msg != null) {
				if (msg.contains("Auth fail")) {
					errMsg = "ftp login failed";
				}else if (msg.contains("Connection timed out") || msg.contains("timeout:")) {
					errMsg = "ftp Connection timed out";
				}else if (msg.contains("Connection refused")) {
					errMsg = "ftp Connection refused";
				}else {
					errMsg = "ftp Connection Exception";
				}
			}else {
				errMsg = "ftp Connection Exception";
			}
		}else if (e instanceof SftpException) {
			LOGGER.error("SftpException error message: " + msg);
			errMsg = "ftp error: " + msg;
		}else {
			LOGGER.error("other error message: " + msg);
			errMsg = msg;
		}
		return new SftpException(errCode, errMsg);
	}
	
	public String removeLFChar(String as_Str) {
		String ls_Str = as_Str;
		if(as_Str.contains("\r\n")) {
			ls_Str = as_Str.replaceAll(" \r\n", " ");
			ls_Str = ls_Str.replaceAll("\r\n", " ");
		}else if(as_Str.contains("\n")) {
			ls_Str = as_Str.replaceAll(" \n", " ");
			ls_Str = ls_Str.replaceAll("\n", " ");
		}
		return ls_Str;
	}

/*	public String getFieldMapping(String as_Product){

		String ls_Content ="";
		List<FieldMapping> listResult = fieldMappingService.findAllByProductAndShowOrderByOrder(as_Product, true);

		for (FieldMapping fieldMapping: listResult
			 ) {
			ls_Content += fieldMapping.getProduct() + fileDelimiter;
		}

		return ls_Content;
	}*/
	public List<String> getFieldDescList(String as_Product){

		List<FieldMapping> listResult = fieldMappingService.findAllByProductAndShowOrderByOrder(as_Product, true);

		List<String> fieldNameLst = new ArrayList<String>();
		for (FieldMapping fieldMapping: listResult
		) {
			//fieldNameLst.add(fieldMapping.getField());
			fieldNameLst.add(fieldMapping.getFieldDesc());
		}

		return fieldNameLst;
	}

	public String getFieldDesc(String as_Product){
		String ls_Content="";
		List<String>  fieldDescLst = getFieldDescList(as_Product);
		for (int i = 0; i < fieldDescLst.size(); i++) {
			String ls_fieldDesc = fieldDescLst.get(i);
			if(i != fieldDescLst.size() - 1)
				ls_Content += ls_fieldDesc + fileDelimiter;
			else
				ls_Content += ls_fieldDesc;
		}
		return ls_Content;
	}
}
