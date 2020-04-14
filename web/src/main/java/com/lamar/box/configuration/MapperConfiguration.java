package com.lamar.box.configuration;

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
