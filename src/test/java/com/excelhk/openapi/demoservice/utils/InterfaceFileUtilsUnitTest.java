package com.excelhk.openapi.demoservice.utils;

import com.excelhk.openapi.demoservice.bean.Deposit;
import com.excelhk.openapi.demoservice.utils.constants.DemoConstants;
import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"sftp.port=10022","sftp.privateKey=classpath:keys/sftp_rsa",
        "sftp.privateKeyPassphrase=passphrase","sftp.remoteInpath=/","sftp.remoteOutpath=/"})
public class InterfaceFileUtilsUnitTest {

    @Autowired
    private CommonUtils commonUtils;

    @Value("${sftp.localInpath}")
    private String path;

    @Autowired
    private InterfaceFileFtpUtils interfaceFileFtpUtils;

    private static EmbeddedSftpServer sftpServer;
    private static Path sftpFolder;

    // Prepare the detail file(test.tmp) to the sftp.localInpath folder before testing

    @BeforeClass
    public static void startServer() throws Exception{
        sftpServer = new EmbeddedSftpServer();
        sftpServer.setPort(10022);
        String pathname = "D:/Data/001";
        new File(pathname).mkdirs();
        sftpFolder = Paths.get(pathname);
        System.out.println("sftpFolder: " + sftpFolder);
        sftpServer.afterPropertiesSet();
        sftpServer.setHomeFolder(sftpFolder);
        System.out.println("sftpFolder : " + sftpFolder);
        if(!sftpServer.isRunning()){
            sftpServer.start();
        }
    }
    @Before
    @After
    public void clearSftpFolder() throws Exception{
        Files.walk(sftpFolder).filter(Files::isRegularFile).map(Path::toFile).forEach(File::delete);
    }

    @Test
    public  void whenGetDetails_thenReturnList() throws Exception {
        List details = new ArrayList();
        Deposit obj = new Deposit();
        obj.setProdId("D1");
        obj.setProduct(DemoConstants.PROD_TYPE_DEPOSIT);
        String fileName = null;
        fileName = commonUtils.getFileName("prod" + "." + obj.getProduct() + "." + obj.getProdId());
        System.out.println("fileName: " + fileName);

        commonUtils.generateFile(fileName, obj);

        // upload
        interfaceFileFtpUtils.upload(fileName);

        rename("test.tmp",fileName, path);

        commonUtils.getDetailByFile(obj,fileName,details);
        Assert.assertEquals(1,details.size());
        Assert.assertThat(((Deposit)details.get(0)).getProdId(), Matchers.is("D1"));

        Assert.assertThat(((Deposit)details.get(0)).getProduct(), Matchers.is(DemoConstants.PROD_TYPE_DEPOSIT));
        Assert.assertThat(((Deposit)details.get(0)).getMinamount(), Matchers.is("5000"));
    }


    @AfterClass
    public static void stopServer(){
        if(sftpServer.isRunning()){
            sftpServer.stop();
        }
    }

    public void rename(String originalFileName, String newFileName, String path){

        File file = new File(path+"/"+originalFileName);

        File newFile = new File(file.getPath().replace(originalFileName.substring(0,originalFileName.lastIndexOf(".")),newFileName));

        if(file.renameTo(newFile)){
            System.out.println(file.getPath());
            System.out.println("rename");
            System.out.println(newFile.getPath());
            System.out.println("successfully.");
        }
    }

}