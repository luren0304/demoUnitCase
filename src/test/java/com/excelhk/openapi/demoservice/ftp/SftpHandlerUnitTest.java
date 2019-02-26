package com.excelhk.openapi.demoservice.ftp;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"sftp.port=10022"})
public class SftpHandlerUnitTest {

    /*
    @Autowired
    private SftpHandler.UploadGateway uploadGateway;

    private static EmbeddedSftpServer sftpServer;
    private static Path sftpFolder;

    @BeforeClass
    public static void startServer() throws Exception{
        sftpServer = new EmbeddedSftpServer();
        sftpServer.setPort(10022);
        sftpFolder = Files.createTempDirectory("ftp-outbound");
        sftpServer.afterPropertiesSet();
        sftpServer.setHomeFolder(sftpFolder);
        if(!sftpServer.isRunning()){
            sftpServer.start();
        }
    }
    @Before
    @After
    public void clearSftpFolder() throws Exception{
        Files.walk(sftpFolder).filter(Files::isRegularFile).map(Path::toFile).forEach(File::delete);
    }

    //@Test

    @AfterClass
    public static void stopServer(){
        if(sftpServer.isRunning()){
            sftpServer.stop();
        }
    }*/
}