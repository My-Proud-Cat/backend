package com.study.proudcat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ProudCatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProudCatApplication.class, args);
    }

}
