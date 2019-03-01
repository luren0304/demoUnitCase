package com.excelhk.openapi.demoservice.ftp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.*;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"sftp.port=10022","sftp.remoteInpath=/"})
public class SftpHandlerDownloadUnitTest extends SftpHandlerUnitTest {

    @Value("${sftp.localInpath}")
    private String localDirectoryDownload;
    // Test download
    // Test rename
    @Test
    public void testDownload() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        // Prepare phase
        //Path tempFile = Files.createTempFile(sftpFolder, "TEST_DOWNLOAD_", ".csv");
        File file = new File(sftpFolder + "/TEST_DOWNLOAD_2527022838211059829.csv");
        Path tempFile = file.toPath();
        System.out.println("tempFile: " + tempFile);

        File file1 = new File(sftpFolder + "/TEST_DOWNLOAD_2527022838211059829.tmp");
        Path pathFile1 = file1.toPath();

        // Run async task to wait for expected files to be downloaded to a file
        // system from a remote SFTP server
        Future<Boolean> future = Executors.newSingleThreadExecutor().submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Path expectedFile = Paths.get(localDirectoryDownload).resolve(pathFile1.getFileName());
                System.out.println("expectedFile: " + expectedFile);
                while (!Files.exists(expectedFile)) {
                    Thread.sleep(200);
                }
                return true;
            }
        });

        // Validation phase
        assertTrue(future.get(30, TimeUnit.SECONDS));
        assertTrue(Files.notExists(tempFile));
    }
//    @Before
//    @After
//    public void clean() throws IOException {
//        Files.walk(sftpFolder).filter(Files::isRegularFile).map(Path::toFile).forEach(File::delete);
//        Files.walk(Paths.get(localDirectoryDownload)).filter(Files::isRegularFile).map(Path::toFile)
//                .forEach(File::delete);
//
//    }
}