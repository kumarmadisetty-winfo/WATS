package com.winfo.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import okhttp3.OkHttpClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .build();
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .build();
    }
}
