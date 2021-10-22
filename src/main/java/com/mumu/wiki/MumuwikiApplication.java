package com.mumu.wiki;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mumu.wiki.mapper")
public class MumuwikiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MumuwikiApplication.class, args);
    }

}