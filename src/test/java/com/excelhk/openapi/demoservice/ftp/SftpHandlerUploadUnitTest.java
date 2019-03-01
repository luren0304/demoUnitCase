package com.excelhk.openapi.demoservice.ftp;

import com.excelhk.openapi.demoservice.utils.EmbeddedSftpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"sftp.port=10022","sftp.local.directory.download= ${java.io.tmpdir}/localDownload"})
public class SftpHandlerUploadUnitTest extends SftpHandlerUnitTest{
//    private static EmbeddedSftpServer server;
//
//    private static Path sftpFolder;

    @Autowired
    private SftpHandler.UploadGateway uploadGateway;

    @Value("${sftp.local.directory.download}")
    private String localDirectoryDownload;


    @Test
    public void testUpload() throws IOException {
        // Prepare phase
        Path tempFile = Files.createTempFile("UPLOAD_TEST", ".csv");
        System.out.println("tempFile: " + tempFile);
        // Prerequisites
        assertEquals(0, Files.list(sftpFolder).count());

        // test phase
        uploadGateway.upload(tempFile.toFile());

        // Validation phase
        List<Path> paths = Files.list(sftpFolder).collect(Collectors.toList());
        assertEquals(1, paths.size());
        assertEquals(tempFile.getFileName(), paths.get(0).getFileName());
    }








}