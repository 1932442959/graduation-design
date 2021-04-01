package com.scu.lcw.backsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan("com.scu.lcw.backsystem.mapper")
@EnableDiscoveryClient
@SpringBootApplication
public class BacksystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(BacksystemApplication.class, args);
    }
}
