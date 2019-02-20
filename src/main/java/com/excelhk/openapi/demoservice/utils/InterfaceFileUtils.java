package com.excelhk.openapi.demoservice.utils;

import com.excelhk.openapi.demoservice.bean.Deposit;
import com.excelhk.openapi.demoservice.bean.Loan;
import com.excelhk.openapi.demoservice.bean.RateInfo;
import com.excelhk.openapi.demoservice.utils.constants.DemoConstants;
import com.jcraft.jsch.SftpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author anita
 */

@Component
public class InterfaceFileUtils {
    @Value("${sftp.waitTime}")
    private long waitTime;

    @Value("${sftp.retry}")
    private int loopCnt;

    private Hashtable exceptionMessage;

    @Autowired
    private InterfaceFileFtpUtils interfaceFileFtpUtils;


    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CommonUtils commonUtils;

    private static final Logger logger = LoggerFactory.getLogger(InterfaceFileUtils.class);
    /**
     * get details
     *
     * @param obj
     * @return
     */
    public List getDetails(Object obj) throws SftpException{
        List details = new ArrayList();
        String fileName=null;
        if(obj instanceof RateInfo) {
            fileName = commonUtils.getFileName("exch" + "." + ((RateInfo)obj).getCcyCde() + "." + ((RateInfo)obj).getRelvtCcyCde());
        }else if (obj instanceof Deposit) {
            fileName =  commonUtils.getFileName("prod" + "." + ((Deposit)obj).getProduct() + "." + ((Deposit)obj).getProdId());
        }else if (obj instanceof Loan) {
            fileName =  commonUtils.getFileName("prod" + "." + ((Loan)obj).getProduct() + "." + ((Loan)obj).getProdId());
        }

        boolean success = commonUtils.generateFile(fileName, obj );
        if(success) {
            //upload file
            try {
                interfaceFileFtpUtils.upload(fileName);

            } catch (Exception e) {
                throw commonUtils.handleErr(e);
            }

            logger.info("get file details");
            FtpFileEvent ftpFileEvent = new FtpFileEvent(this, fileName);
            ftpFileEvent.setEventType(DemoConstants.EVENT_TYPE_DETAILS);
            ftpFileEvent.setObject(obj);
            ftpFileEvent.setProdLst(details);
            //getFileLoop(ftpFileEvent);
            if(!getFileLoop(ftpFileEvent)){
                if(exceptionMessage != null){
                    logger.error("{} key failed {}",exceptionMessage);
                    Exception exception = (Exception)exceptionMessage.get(fileName);
                    if(exception != null){
                        logger.error("fileName: " + fileName + " exception:" + exception);
                        throw commonUtils.handleErr(exception);
                    }else {
                        exception = (Exception)exceptionMessage.get("Other");
                        logger.error("Other exception:" + exception);
                        if(exception != null){
                            throw commonUtils.handleErr(exception);
                        }
                    }
                }
            }
        }else {
            details.add(obj);
        }
        return details;
    }

    /**
     * Get the product id
     *
     * @param product
     * @param obj
     * @return
     */
    public List getProds(String product, Object obj) throws SftpException{
        String fileName = null;
        String content = product;
        List pordLst = new ArrayList();
        fileName = commonUtils.getFileName("prod"+ "." + product);
        boolean success = commonUtils.generateFile(fileName,  content);
        if(success) {
            //upload file
            try {
                interfaceFileFtpUtils.upload(fileName);
            } catch (Exception e) {
                throw commonUtils.handleErr(e);
            }
            logger.info("get file details");
            FtpFileEvent ftpFileEvent = new FtpFileEvent(this, fileName);
            ftpFileEvent.setEventType(DemoConstants.EVENT_TYPE_PROD);
            ftpFileEvent.setObject(obj);
            ftpFileEvent.setProdLst(pordLst);
            if(!getFileLoop(ftpFileEvent)){
                if(exceptionMessage != null){
                    logger.error("{} key failed {}",exceptionMessage);
                    Exception exception = (Exception)exceptionMessage.get(fileName);
                    if(exception != null){
                        logger.error("exception:" + exception);
                        throw commonUtils.handleErr(exception);
                    }else {
                        exception = (Exception)exceptionMessage.get("Other");
                        logger.error("exception:" + exception);
                        if(exception != null){
                            throw commonUtils.handleErr(exception);
                        }
                    }
                }
            }

        }
        return pordLst;
    }

    private boolean getFileLoop(FtpFileEvent ftpFileEvent) throws SftpException {
        boolean success = false;
        SftpException sftpException = null;
        for(int i = 0; i < loopCnt; i ++) {
            logger.info("loop i = " + i);
            try {
                applicationContext.publishEvent(ftpFileEvent);
                success = ftpFileEvent.isDownloadFlag();
                if (success) {
                    break;
                } else {
                    sftpException = ftpFileEvent.getSftpException();
                    if (sftpException != null) {
                        throw sftpException;
                    }
                }
                if (i != loopCnt - 1) {
                    Thread.sleep(waitTime * 1000 / loopCnt);
                }
            }catch(SftpException e){
                throw e;
            } catch (Exception e) {
                throw commonUtils.handleErr(e);
            }
        }
        return success;
    }

    @ServiceActivator(inputChannel="errorChannel")
    public void errorHandler(ErrorMessage errorMessage) {
        Exception exceptionMsg = (Exception)errorMessage.getPayload();
        Message<?> message = errorMessage.getOriginalMessage();
        if(null != exceptionMsg) {
            exceptionMsg = (Exception) exceptionMsg.getCause();
            Exception exceptionTmp = exceptionMsg;
            while (null != exceptionMsg) {
                logger.error("errorHandler exception: " + exceptionMsg.getMessage());
                if (null != exceptionMsg) {
                    exceptionTmp = exceptionMsg;
                }
                exceptionMsg = (Exception) exceptionMsg.getCause();
            }
            Hashtable exceptionTable = new Hashtable();
            String fileName = null;
            if(message == null){
                fileName = "Other";
            }else{
                fileName = (String)message.getHeaders().get("file_name");
            }

            exceptionTable.put(fileName.substring(0, fileName.lastIndexOf(".")), exceptionTmp);
            setExceptionMessage(exceptionTable);
        }
    }

    public void setExceptionMessage(Hashtable exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
