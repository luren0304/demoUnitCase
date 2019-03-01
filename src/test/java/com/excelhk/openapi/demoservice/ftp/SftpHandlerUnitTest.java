package com.excelhk.openapi.demoservice.ftp;

import com.excelhk.openapi.demoservice.utils.EmbeddedSftpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"sftp.port=10022","sftp.remoteInpath=/"})
public class SftpHandlerUnitTest {
    public static EmbeddedSftpServer server;

    public static Path sftpFolder;

//    @Value("${sftp.remoteOutpath}")
//    private static String pathName;
//
//    @Value("${sftp.port}")
//    private static int port;

    @BeforeClass
    public static void startServer() throws Exception {

        server = new EmbeddedSftpServer();
        server.setPort(10022);
        //sftpFolder = Files.createTempDirectory("SFTP_DOWNLOAD_TEST");
        String pathName = "D:/Data/001";
        new File(pathName).mkdirs();
        sftpFolder = Paths.get(pathName);
        server.afterPropertiesSet();
        server.setHomeFolder(sftpFolder);
        // Starting SFTP
        if (!server.isRunning()) {
            server.start();
        }
    }

//    @Before
//    @After
//    public void clean() throws IOException {
//        Files.walk(Paths.get(localDirectoryDownload)).filter(Files::isRegularFile).map(Path::toFile)
//                .forEach(File::delete);
//
//    }
    // Test upload
    // Test download
    // Test rename

    @AfterClass
    public static void stopServer() {
        if (server.isRunning()) {
            server.stop();
        }
    }






}