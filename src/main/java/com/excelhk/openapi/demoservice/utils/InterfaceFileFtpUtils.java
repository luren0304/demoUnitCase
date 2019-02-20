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
    @Value("${sftp.romteOut.file.extension:txt}")
    private String remoteOutFileExtension;
    @Value("${sftp.remoteIn.file.extension:csv}")
    private String remoteInFileExtension;

    private static Logger logger = LoggerFactory.getLogger(InterfaceFileFtpUtils.class);

    @Autowired
    private UploadGateway uploadGateway;

    public void upload (String fileName) throws Exception {
        try {
            fileName = fileName + "." + remoteOutFileExtension;
            String localOutpathString = null;
            CommonUtils.checkDirExist(localOutpath);
            if (!localOutpath.endsWith(File.separator)) {
                localOutpathString = localOutpath + File.separator + fileName;
            } else {
                localOutpathString = localOutpath + fileName;
            }
            logger.info("localOutpathString " + localOutpathString);
            uploadGateway.upload(new File(localOutpathString));
        } catch (Exception e) {
            logger.error("upload file failed. error message: " + e.getMessage());
            Exception exceptionMsg = (Exception)e.getCause();
            while (null != exceptionMsg) {
                if(null != exceptionMsg){
                    e = exceptionMsg;
                }
                exceptionMsg = (Exception)exceptionMsg.getCause();
            }
            throw e;
        }
    }
}
