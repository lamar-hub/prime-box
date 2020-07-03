package com.lamar.primebox.web.config;

import com.lamar.primebox.web.dto.model.SharedFileDto;
import com.lamar.primebox.web.dto.model.UserBasicDto;
import com.lamar.primebox.web.dto.model.UserCredentialsDto;
import com.lamar.primebox.web.dto.request.UserLogInRequest;
import com.lamar.primebox.web.dto.request.UserSignUpRequest;
import com.lamar.primebox.web.dto.response.SharedFileShareResponse;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfiguration {

    @Bean
    public ModelMapper defaultMapper() {
        final ModelMapper mapper = new ModelMapper();
        mapper.typeMap(UserSignUpRequest.class, UserBasicDto.class).addMapping(UserSignUpRequest::getEmail, UserBasicDto::setUsername);
        mapper.typeMap(UserLogInRequest.class, UserCredentialsDto.class).addMapping(UserLogInRequest::getEmail, UserCredentialsDto::setUsername);
        mapper.typeMap(UserLogInRequest.class, UserCredentialsDto.class).addMapping(UserLogInRequest::getEmail, UserCredentialsDto::setUsername);
        mapper.typeMap(SharedFileDto.class, SharedFileShareResponse.class).addMapping(SharedFileDto::getSharedFileFileId, SharedFileShareResponse::setFileId);
        mapper.typeMap(SharedFileDto.class, SharedFileShareResponse.class).addMapping(SharedFileDto::getSharedFileFilename, SharedFileShareResponse::setFilename);
        mapper.typeMap(SharedFileDto.class, SharedFileShareResponse.class).addMapping(SharedFileDto::getSharedFileSize, SharedFileShareResponse::setSize);
        mapper.typeMap(SharedFileDto.class, SharedFileShareResponse.class).addMapping(SharedFileDto::getSharedFileLastModified, SharedFileShareResponse::setLastModified);
        mapper.typeMap(SharedFileDto.class, SharedFileShareResponse.class).addMapping(SharedFileDto::getSharedFileUserEmail, SharedFileShareResponse::setSharedUserUsername);
        mapper.typeMap(SharedFileDto.class, SharedFileShareResponse.class).addMapping(SharedFileDto::getSharedFileUserName, SharedFileShareResponse::setSharedUserName);
        mapper.typeMap(SharedFileDto.class, SharedFileShareResponse.class).addMapping(SharedFileDto::getSharedFileUserSurname, SharedFileShareResponse::setSharedUserSurname);
        return mapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
