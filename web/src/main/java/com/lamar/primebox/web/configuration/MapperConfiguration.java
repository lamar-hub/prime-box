package com.lamar.primebox.web.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    public ModelMapper defaultMapper() {
        ModelMapper mapper = new ModelMapper();
        return mapper;
    }

}
