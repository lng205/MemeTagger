package com.meme;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public abstract class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            UploadObject.uploadObject("utility-ratio-448006-a2",
                    "memetagger",
                    "b.jpg",
                    "C:\\Users\\youbi\\Downloads\\b.jpg");
        };
    }
}
