package com.meme;

import com.meme.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public abstract class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
