package com.lamar.primebox.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lamar.primebox")
public class PrimeBoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrimeBoxApplication.class, args);
    }

}
