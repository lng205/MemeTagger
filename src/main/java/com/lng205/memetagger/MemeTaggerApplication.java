package com.lng205.memetagger;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public abstract class MemeTaggerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemeTaggerApplication.class, args);
    }

    @Value("${key.gemini}")
    private String key;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            System.out.println("Starting MemeTagger application...");

            System.out.println("Key: " + key);
            Client client = Client.builder().apiKey(key).build();
            GenerateContentResponse response = client.models.generateContent("gemini-2.0-flash",
                    "What is the meaning of life?", null);
            System.out.println(response.text());

            System.out.println("Finished MemeTagger application...");
        };
    }
}
