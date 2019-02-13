package com.excelhk.openapi.demoservice.utils;

import com.excelhk.openapi.demoservice.ftp.SftpHandler.UploadGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.support.ErrorMessage;

import java.io.File;

/**
 *
 * @author anita
 */

@Configuration
public class InterfaceFileFtpUtils {

    @Value("${sftp.localOutpath}")
    private String localOutpath;
    @Value("${sftp.romteOut.file.conv:txt}")
    private String remoteOutFileConv;
    @Value("${sftp.remoteIn.file.conv:txt}")
    private String remoteInFileConv;
    private Exception exception;

    private static Logger logger = LoggerFactory.getLogger(InterfaceFileFtpUtils.class);

    @Autowired
    private UploadGateway uploadGateway;

    public void upload (String fileName) throws Exception {
        try {
            fileName = fileName + "." + remoteOutFileConv;
            String localOutpathString = null;
            CommonUtils.checkDirExist(localOutpath);
            if (!localOutpath.endsWith(File.separator)) {
                localOutpathString = localOutpath + File.separator + fileName;
            } else {
                localOutpathString = localOutpath + fileName;
            }
            logger.info("localOutpathString " + localOutpathString);
            uploadGateway.upload(new File(localOutpathString));
            if(null != exception){
                throw exception;
            }

        } catch (Exception e) {
            logger.error("upload file failed. error message: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @ServiceActivator(inputChannel="errorChannel")
    public void errorHandler(ErrorMessage errorMessage) {
        Exception exceptionMsg = (Exception)errorMessage.getPayload();
        if(null != exceptionMsg) {
            exceptionMsg = (Exception) exceptionMsg.getCause();
            Exception exceptionTmp = exceptionMsg;
            while (null != exceptionMsg) {
                if (null != exceptionMsg) {
                    exceptionTmp = exceptionMsg;
                }
                exceptionMsg = (Exception) exceptionMsg.getCause();
            }
            setException(exceptionTmp);
        }
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
