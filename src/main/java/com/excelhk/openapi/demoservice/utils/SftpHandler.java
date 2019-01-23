package com.excelhk.openapi.demoservice.utils;

import com.jcraft.jsch.ChannelSftp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.expression.Expression;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.annotation.*;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.remote.gateway.AbstractRemoteFileOutboundGateway;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.filters.SftpSimplePatternFileListFilter;
import org.springframework.integration.sftp.gateway.SftpOutboundGateway;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizer;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizingMessageSource;
import org.springframework.integration.sftp.outbound.SftpMessageHandler;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *  @author anita
 */
@Configuration
public class SftpHandler {
    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.port}")
    private int port;

    @Value("${sftp.user}")
    private String user;

    @Value("${sftp.password}")
    private String password;

    @Value("${sftp.remoteInpath}")
    private String remoteInpath;

    @Value("${sftp.remoteInpathBak}")
    private String remoteInpathBak;

    @Value("${sftp.localInpath}")
    private String localInpath;

    @Value("${sftp.localInpathBak}")
    private String localInpathBak;

    @Value("${sftp.remoteOutpath}")
    private String remoteOutpath;

    @Value("${sftp.localOutpath}")
    private String localOutpath;

    @Value("${sftp.romteOut.file.conv:txt}")
    private String remoteOutFileConv;

    @Value("${sftp.remoteIn.file.conv:csv}")
    private String remoteInFileConv;


    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    @Bean
    public SessionFactory<ChannelSftp.LsEntry> sftpSessionFactory(){
        System.out.println("sftpSessionFactory() Start");
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
        factory.setHost(host);
        factory.setPort(port);
        factory.setUser(user);
        factory.setPassword(password);
        factory.setAllowUnknownKeys(true);
        System.out.println("sftpSessionFactory() End");
        return new CachingSessionFactory<ChannelSftp.LsEntry>(factory);
    }

    @Bean
    public SftpInboundFileSynchronizer sftpInboundFileSynchronizer() {
        System.out.println("sftpInboundFileSynchronizer() Start");
        SftpInboundFileSynchronizer synchronizer = new SftpInboundFileSynchronizer(sftpSessionFactory());
        synchronizer.setDeleteRemoteFiles(false);
        synchronizer.setRemoteDirectory(remoteInpath);
        synchronizer.setFilter(new SftpSimplePatternFileListFilter("*." + remoteInFileConv));
        System.out.println("sftpInboundFileSynchronizer() End");
        return synchronizer;
    }
    @Bean(name="downloadChannel")
    public MessageChannel downloadChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean

    @InboundChannelAdapter(channel="downloadChannel", poller=@Poller(cron = "${sftp.cron}"))
    public MessageSource<File> sftpMessageSource(){
        System.out.println("sftpMessageSource() Start");
        SftpInboundFileSynchronizingMessageSource source = new SftpInboundFileSynchronizingMessageSource(sftpInboundFileSynchronizer());
        Map<String, Expression> headerExpressions = new HashMap(2);
        headerExpressions.put("remoteInpath", PARSER.parseExpression("'" + CommonUtils.getFullPath(remoteInpath) +"'") );
        headerExpressions.put("remoteInpathBak", PARSER.parseExpression("'" + CommonUtils.getFullPath(remoteInpathBak) + "'") );
        source.setHeaderExpressions(headerExpressions);
        source.setAutoCreateLocalDirectory(true);
        source.setLocalDirectory(new File(localInpath));
        source.setLocalFilter(new AcceptOnceFileListFilter<File>());
        System.out.println("sftpMessageSource() End");
        return source;
    }

    @Bean
    @ServiceActivator(inputChannel="downloadChannel")
    public MessageHandler downloadFiles() {
        System.out.println("downloadFiles() Start");
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println("handleMessage() Start");
                System.out.println(message.getHeaders());
                MessageHeaders messageHeaders = message.getHeaders();
                messageHeaders.put("remoteInpath", remoteInpath);
                messageHeaders.put("remoteInpathbak", remoteInpath + "/bak/");
                System.out.println(message.getPayload());
                System.out.println("handleMessage() End");
            }
        };
    }


    @Bean
    @ServiceActivator(inputChannel="downloadChannel")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public MessageHandler renameFiles() {
        System.out.println("renameFiles() Start");
        SftpOutboundGateway sftpOutboundGateway = new  SftpOutboundGateway(sftpSessionFactory(), AbstractRemoteFileOutboundGateway.Command.MV.getCommand(), PARSER.parseExpression("headers.remoteInpath + payload.getName()").getExpressionString());
        sftpOutboundGateway.setRenameExpressionString(PARSER.parseExpression("headers.remoteInpathBak + payload.getName()").getExpressionString());
        sftpOutboundGateway.setRequiresReply(false);
        sftpOutboundGateway.setOutputChannelName("nullChannel");
        sftpOutboundGateway.setOrder(Ordered.LOWEST_PRECEDENCE);
        sftpOutboundGateway.setAsync(true);
        System.out.println("renameFiles() End");
        return sftpOutboundGateway;
    }


    @Bean
    @ServiceActivator(inputChannel = "toSftpChannel")
    public MessageHandler handler() {
        SftpMessageHandler handler = new SftpMessageHandler(sftpSessionFactory());
        handler.setRemoteDirectoryExpression(new LiteralExpression(remoteOutpath));
        handler.setFileNameGenerator(new FileNameGenerator() {
            @Override
            public String generateFileName(Message<?> message) {
                System.out.println(message.getHeaders());
                System.out.println(message.getPayload());
                if (message.getPayload() instanceof File) {
                    return ((File) message.getPayload()).getName();
                } else {
                    throw new IllegalArgumentException("File expected as payload.");
                }
            }

        });
        return handler;
    }

    /**
     * upload file gateway
     */
    @MessagingGateway
    public interface UploadGateway {

        /**
         * upload file to ftp
         * @param file
         */
        @Gateway(requestChannel = "toSftpChannel")
        void upload(File file);

    }

}
