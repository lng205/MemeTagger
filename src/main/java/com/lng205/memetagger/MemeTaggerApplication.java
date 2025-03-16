package com.lng205.memetagger;

import org.apache.ibatis.annotations.Insert;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public abstract class MemeTaggerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemeTaggerApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> System.out.println("Hello World");
    }
}
