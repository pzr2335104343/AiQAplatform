package com.pzr.rongdada.config;

import com.zhipu.oapi.ClientV4;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ai")
@Data
public class AIConfig {
    /**
     * apiKey
     */
    private String apiKey;

    @Bean
    public ClientV4 clientV4(){
        return new ClientV4.Builder(apiKey).build();
    }
}