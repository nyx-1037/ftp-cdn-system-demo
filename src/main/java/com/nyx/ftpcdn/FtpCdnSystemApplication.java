package com.nyx.ftpcdn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * FTP-CDN系统主启动类
 * 
 * @author nyx
 */
@SpringBootApplication
public class FtpCdnSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FtpCdnSystemApplication.class, args);
        System.out.println("FTP-CDN系统启动成功！");
    }

}