package com.cy.framework.service.impl.ftp;

import com.cy.framework.model.UploadParam;
import com.cy.framework.model.ftp.FtpParam;
import com.cy.framework.service.dao.ftp.FtpService;
import com.cy.framework.service.impl.DataException;
import com.cy.framework.util.StringUtil;
import com.cy.framework.util.json.JSONUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class FtpServiceImpl implements FtpService, Callable<Map<String, Object>> {
    @Resource
    private FtpParam ftpParam;
    private FTPClient ftpClient = new FTPClient();
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 连接FTP服务器
     *
     * @return
     */
    private boolean connect() {
        boolean stat = false;
        try {
            if (ftpClient.isConnected()) {
                return true;
            }
            ftpClient.connect(ftpParam.getHost(), ftpParam.getPort());
            stat = true;
        } catch (SocketException e) {
            stat = false;
            e.printStackTrace();
        } catch (IOException e) {
            stat = false;
            e.printStackTrace();
        }
        return stat;
    }

    /**
     * 打开FTP服务器
     *
     * @return
     */
    public boolean open() {
        if (ftpClient == null) {
            ftpClient = new FTPClient();
        }
        if (!connect()) {
            return false;
        }
        boolean stat = false;
        try {
            stat = ftpClient.login(ftpParam.getUserName(), ftpParam.getPassword());
            // 检测连接是否成功
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                close();
                stat = false;
            }
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
            stat = false;
        }
        logger.warn("open is :" + stat);
        return stat;
    }


    /**
     * 关闭FTP服务器
     */
    public void close() {
        try {
            if (ftpClient != null) {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
                ftpClient = null;
            }
            logger.warn("close");
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }


    /**
     * 上传文件到FTP服务器
     *
     * @param filename
     * @param path
     * @param input
     * @return
     */
    public boolean upload(String filename, String path, InputStream input) {
        boolean stat = false;
        try {
            open();
            mkdirs(path);
            if (StringUtil.isNotEmpty(path)) {
                boolean ret = ftpClient.changeWorkingDirectory(path);
                logger.warn("changeWorkingDirectory " + path + " ret is：" + ret);
            }
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("GBK");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            stat = ftpClient.storeFile(filename, input);
            logger.warn("fileName:" + filename + ",ret:" + stat + ",input");
            input.close();  //关闭输入流
            ftpClient.changeToParentDirectory();
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
        logger.warn("upload result is :" + stat);
        return stat;
    }

    /**
     * 上传文件到FTP服务器
     *
     * @param filename
     * @param path
     * @param filepath
     * @return
     */
    public boolean upload(String filename, String path, String filepath) {
        boolean stat = false;
        try {
            open();
            mkdirs(path);
            cd(path);
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("GBK");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            FileInputStream input = new FileInputStream(new File(filepath));
            stat = ftpClient.storeFile(filename, input);
            input.close();  //关闭输入流
        } catch (IOException e) {

        }
        return stat;
    }

    /**
     * 上传文件
     *
     * @param filename
     * @param path
     * @param file
     * @return
     */
    public boolean upload(String filename, String path, File file) {
        boolean stat = false;
        try {
            open();
            mkdirs(path);
            cd(path);
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("GBK");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            FileInputStream input = new FileInputStream(file);
            stat = ftpClient.storeFile(filename, input);
            input.close();  //关闭输入流
        } catch (IOException e) {

        }
        return stat;
    }

    /**
     * 循环切换目录
     *
     * @param dir
     * @return
     */
    public boolean cd(String dir) {
        boolean stat = false;
        try {
            logger.warn("cd path:" + dir);
            String[] dirs = dir.split("/");
            if (dirs.length == 0) {
                return ftpClient.changeWorkingDirectory(dir);
            }

            stat = ftpClient.changeToParentDirectory();
            for (String dirss : dirs) {
                stat = stat && ftpClient.changeWorkingDirectory(dirss);
            }

            stat = true;
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
            stat = false;
        }
        logger.warn("cd reuslt is:" + stat);
        return stat;
    }

    /***
     * 创建目录
     * @param dir
     * @return
     */
    public boolean mkdir(String dir) {
        boolean stat = false;
        try {
            ftpClient.changeToParentDirectory();
            ftpClient.makeDirectory(dir);
            stat = true;
        } catch (IOException e) {
            stat = false;
        }
        return stat;
    }

    /***
     * 创建多个层级目录
     * @param dir dong/zzz/ddd/ewv
     * @return
     */
    public boolean mkdirs(String dir) {
        String[] dirs = dir.split("/");
        if (dirs.length == 0) {
            return false;
        }
        boolean stat = false;
        try {
            ftpClient.changeToParentDirectory();
            for (String dirss : dirs) {
                if (StringUtil.isEmpty(dirss)) {
                    continue;
                }
                ftpClient.makeDirectory(dirss);
                ftpClient.changeWorkingDirectory(dirss);
            }

            ftpClient.changeToParentDirectory();
            stat = true;
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
            stat = false;
        }
        return stat;
    }

    /**
     * 删除文件夹
     *
     * @param pathname
     * @return
     */
    public boolean rmdir(String pathname) {
        try {
            return ftpClient.removeDirectory(pathname);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param pathname
     * @return
     */
    public boolean remove(String pathname) {
        boolean stat = false;
        try {
            stat = ftpClient.deleteFile(pathname);
        } catch (Exception e) {
            stat = false;
        }
        return stat;
    }

    /**
     * 移动文件或文件夹
     *
     * @param pathname1
     * @param pathname2
     * @return
     */
    public boolean rename(String pathname1, String pathname2) {
        try {
            return ftpClient.rename(pathname1, pathname2);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Map<String, Object> upload(UploadParam param, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = null;
        try {
            if (param.getFiles() == null || param.getFiles().size() <= 0) {
                throw new DataException(1,"请至少上传一张图片");
            }
            StringBuilder builder = new StringBuilder();
            if (StringUtil.isNotEmpty(param.getType())) {
                if (StringUtil.isEmpty(param.getId())) {
                    param.setId("1");
                }
                builder.append("/").append(param.getType()).append("/").append(param.getId());
            }
            List<String> list = new ArrayList<>();
            for (MultipartFile file : param.getFiles()) {
                String name = UUID.randomUUID().toString() + "." + getType(file.getOriginalFilename());
                list.add(builder.toString() + "/" + name);
                Future<Boolean> future = taskExecutor.submit(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        logger.warn("path：" + file.getOriginalFilename() + ",name:" + name);
                        return upload(name, builder.toString(), file.getInputStream());
                    }
                });
                if (!future.get()) {
                    throw new DataException(1,"上传失败，请重试。");
                }
            }
            map = new HashMap<>();
            map.put("images", list);
            map.put("url", ftpParam.getUrl());
            map.put("result", true);
            map.put("remark", "上传成功");
        } catch (DataException e) {
            map = JSONUtils.getBasicJson(e.getMessage(), false);
        } catch (InterruptedException e) {
            e.printStackTrace();
            map = JSONUtils.getBasicJson(e.getMessage(), false);
        } catch (ExecutionException e) {
            e.printStackTrace();
            map = JSONUtils.getBasicJson(e.getMessage(), false);
        } finally {
            close();
        }
        return map;
    }

    private String getType(String name) {
        String[] string = name.split("\\.");
        return string[string.length - 1];
    }

    @Override
    public Map<String, Object> call() {

        return null;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService threadPoolTaskExecutor = Executors.newFixedThreadPool(20);
        Future<String> future = threadPoolTaskExecutor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(1000);
                return (1 + 1) + "";
            }
        });
        System.out.println(future.get());
    }
}
