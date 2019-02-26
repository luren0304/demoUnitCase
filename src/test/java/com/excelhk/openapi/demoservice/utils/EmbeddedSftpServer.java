package com.excelhk.openapi.demoservice.utils;

import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.SocketUtils;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Collections;

public class EmbeddedSftpServer implements InitializingBean, SmartLifecycle {

    public static final int PORT = SocketUtils.findAvailableTcpPort();
    private final SshServer sshServer =  SshServer.setUpDefaultServer();
    private volatile int port;
    private volatile boolean running;

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        final PublicKey allowedKey = decodePublicKey();
        this.sshServer.setPublickeyAuthenticator(new PublickeyAuthenticator() {
            @Override
            public boolean authenticate(String username, PublicKey key, ServerSession session) throws AsyncAuthException {
                return key.equals(allowedKey);
            }
        });
        this.sshServer.setPort(this.port);
        this.sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Files.createTempFile("host_file",".ser")));
        this.sshServer.setSubsystemFactories(Collections.<NamedFactory<Command>>singletonList(new SftpSubsystemFactory()));
        sshServer.setFileSystemFactory(new VirtualFileSystemFactory(Files.createTempDirectory("SFTP_TEMP")));
        sshServer.setCommandFactory(new ScpCommandFactory());
    }

    public void setHomeFolder(Path path){
        sshServer.setFileSystemFactory(new VirtualFileSystemFactory(path));
    }


    @Override
    public void start() {

        try{
            sshServer.start();
            this.running = true;
        }catch (IOException e){
            throw  new IllegalStateException(e);
        }
    }

    @Override
    public void stop() {

        if(this.running){
            try {
                sshServer.stop();
            }catch (IOException e){
                throw new IllegalStateException(e);
            }finally {
                this.running = false;
            }
        }

    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public boolean isAutoStartup() {
        return PORT == this.port;
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    public SshServer getSshServer(){
        return sshServer;
    }
    private PublicKey decodePublicKey() throws Exception{
        InputStream stream = new ClassPathResource("/keys/sftp_rsa.pub").getInputStream();
        byte[] decodeBuffer = Base64.decodeBase64(StreamUtils.copyToByteArray(stream));
        ByteBuffer bb = ByteBuffer.wrap(decodeBuffer);
        int len = bb.getInt();
        byte[] type = new byte[len];
        bb.get(type);
        if("ssh-rsa".equals(new String(type))){
            BigInteger e = decodeBigInt(bb);
            BigInteger m = decodeBigInt(bb);
            RSAPublicKeySpec spec = new RSAPublicKeySpec(m, e);
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        }else{
            throw new IllegalArgumentException("only supports RSA");
        }
    }

    private BigInteger decodeBigInt(ByteBuffer byteBuffer){
        int len = byteBuffer.getInt();
        byte[] bytes = new byte[len];
        byteBuffer.get(bytes);
        return new BigInteger(bytes);
    }
}
