package com.hyundaiuni.nxtims.support.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.hyundaiuni.nxtims.exception.FtpClientException;

public class FtpClientTemplate {
    private static final Log log = LogFactory.getLog(FtpClientTemplate.class);

    private static final int RETRY_CNT = 2;
    private static final int THIRTYSECONDS = 30 * 1000;

    private String server;
    private String username;
    private String password;
    private String mode;

    public void setServer(String server) {
        this.server = server;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    private FTPClient login() throws FtpClientException {
        int connectFailCnt = 0;
        int reply;

        FTPClient ftpClient = new FTPClient();

        ftpClient.setConnectTimeout(THIRTYSECONDS);
        ftpClient.setDefaultTimeout(THIRTYSECONDS);

        while(connectFailCnt < RETRY_CNT) {
            try {
                if(connectFailCnt == 0) {
                    log.info("Connecting FTP Server [" + this.server + "].");
                }
                else {
                    log.info("Reconnecting FTP Server [" + this.server + "].");
                }

                ftpClient.connect(this.server);
                ftpClient.setSoTimeout(THIRTYSECONDS);

                log.info("Waiting a reply of the FTP Server [" + this.server + "].");

                reply = ftpClient.getReplyCode();

                log.info("The reply of the FTP Server is [" + reply + "].");

                if(!FTPReply.isPositiveCompletion(reply)) {
                    log.info("Cannot connect the FTP Server [" + this.server + "]. Reply code is [" + reply + "].");

                    ftpClient.disconnect();

                    throw new FtpClientException(
                        "Cannot connect the FTP Server [" + this.server + "]. Reply code is [" + reply + "].");
                }

                log.info("FTP Sever [" + this.server + "] is connected.");

                break;
            }
            catch(IOException e) {
                connectFailCnt++;

                if(connectFailCnt == RETRY_CNT) {
                    throw new FtpClientException("Cannot connect the FTP Server [" + this.server + "]", e);
                }
            }
        }

        log.info("Logging on the FTP Server [" + this.server + "].");

        try {
            if(!ftpClient.login(this.username, this.password)) {
                log.info("Cannot login the FTP Server [" + this.server + "] with id [" + this.username
                         + "] and password [" + this.password + "].");

                ftpClient.logout();

                throw new FtpClientException("Cannot login the FTP Server [" + this.server + "] with id ["
                                             + this.username + "] and password [" + this.password + "].");
            }
        }
        catch(IOException e) {
            throw new FtpClientException("Cannot login the FTP Server [" + this.server + "] with id [" + this.username
                                         + "] and password [" + this.password + "].",
                e);
        }

        log.info("Logged on the FTP Server [" + this.server + "].");
        log.info("Requesting a passive mode to the FTP Server [" + this.server + "].");

        if("P".equals(this.mode)) {
            ftpClient.enterLocalPassiveMode();
            log.info("Entering a passive mode to the FTP Server [" + this.server + "].");
        }
        else {
            ftpClient.enterLocalActiveMode();
            log.info("Entering a active mode to the FTP Server [" + this.server + "].");
        }

        ftpClient.setControlKeepAliveTimeout(THIRTYSECONDS);

        return ftpClient;
    }

    public void sendFile(String remoteDir, File file) throws FtpClientException {
        FTPClient ftpClient = null;
        InputStream input = null;

        try {
            ftpClient = login();
            input = new FileInputStream(file);

            if(!ftpClient.changeWorkingDirectory(remoteDir)) {
                log.info(
                    "Cannot change a remote directory [" + remoteDir + "] of the FTP Server [" + this.server + "].");

                throw new FtpClientException(
                    "Cannot change a remote directory [" + remoteDir + "] of the FTP Server [" + this.server + "].");
            }

            ftpClient.storeFile(file.getName(), input);

            log.info("File [" + file.getName() + "] was stored.");
        }
        catch(FileNotFoundException e) {
            throw new FtpClientException("Cannot find file to send.", e);
        }
        catch(IOException e) {
            throw new FtpClientException("Cannot send file.", e);
        }
        finally {
            IOUtils.closeQuietly(input);

            if(ftpClient != null) {
                try {
                    ftpClient.setSoTimeout(THIRTYSECONDS);
                    ftpClient.logout();
                }
                catch(IOException e) {
                    log.info(e.getMessage());
                }
            }

            if(ftpClient != null) {
                try {
                    ftpClient.disconnect();
                }
                catch(IOException e) {
                    log.info(e.getMessage());
                }
            }
        }
    }

    public void receiveFile(String remoteDir, String remotefileName, File file)
        throws FtpClientException {

        FTPClient ftpClient = null;
        OutputStream outputStream = null;

        try {
            ftpClient = login();

            if(!ftpClient.changeWorkingDirectory(remoteDir)) {
                log.info(
                    "Cannot change a remote directory [" + remoteDir + "] of the FTP Server [" + this.server + "].");

                throw new FtpClientException(
                    "Cannot change a remote directory [" + remoteDir + "] of the FTP Server [" + this.server + "].");
            }

            outputStream = new FileOutputStream(file);

            if(ftpClient.retrieveFile(remotefileName, outputStream)) {
                log.info("File [" + remotefileName + "] was received successfully.");
            }
            else {
                log.info("Cannot receive file [" + remotefileName + "] of the FTP Server [" + this.server + "].");

                throw new FtpClientException(
                    "Cannot receive file [" + remotefileName + "] of the FTP Server [" + this.server + "].");
            }
        }
        catch(IOException e) {
            throw new FtpClientException("Cannot retrieve file.", e);
        }
        finally {
            IOUtils.closeQuietly(outputStream);

            if(ftpClient != null) {
                try {
                    ftpClient.logout();
                }
                catch(IOException e) {
                    log.info(e.getMessage());
                }
            }

            if(ftpClient != null) {
                try {
                    ftpClient.disconnect();
                }
                catch(IOException e) {
                    log.info(e.getMessage());
                }
            }
        }
    }

    public void deleteFile(String remoteDir, String remotefileName)
        throws FtpClientException {

        FTPClient ftpClient = null;

        try {
            ftpClient = login();

            if(!ftpClient.changeWorkingDirectory(remoteDir)) {
                log.info(
                    "Cannot change a remote directory [" + remoteDir + "] of the FTP Server [" + this.server + "].");

                throw new FtpClientException(
                    "Cannot change a remote directory [" + remoteDir + "] of the FTP Server [" + this.server + "].");
            }

            if(ftpClient.deleteFile(remotefileName)) {
                log.info("File [" + remotefileName + "] was deleted successfully.");
            }
            else {
                log.info("Cannot delete file [" + remotefileName + "] of the FTP Server [" + this.server + "].");

                throw new FtpClientException(
                    "Cannot delete file [" + remotefileName + "] of the FTP Server [" + this.server + "].");
            }
        }
        catch(IOException e) {
            throw new FtpClientException("Cannot delete file.", e);
        }
        finally {
            if(ftpClient != null) {
                try {
                    ftpClient.logout();
                }
                catch(IOException e) {
                    log.info(e.getMessage());
                }
            }

            if(ftpClient != null) {
                try {
                    ftpClient.disconnect();
                }
                catch(IOException e) {
                    log.info(e.getMessage());
                }
            }
        }
    }
}
