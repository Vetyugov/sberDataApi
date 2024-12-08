package ru.thebestsolution.dataapi.dataapiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.thebestsolution.dataapi.dataapiservice.config.AppConfig;

@SpringBootApplication
@EnableConfigurationProperties(value = AppConfig.class)
public class DataApiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataApiServiceApplication.class, args);
    }

}
