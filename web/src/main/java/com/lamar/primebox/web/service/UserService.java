package com.lamar.primebox.web.service;

import com.lamar.primebox.web.dto.model.UserAndJwtDto;
import com.lamar.primebox.web.dto.model.UserBasicDto;
import com.lamar.primebox.web.dto.model.UserCredentialsDto;
import com.lamar.primebox.web.dto.model.UserDto;

public interface UserService {

    UserDto getUser(String username) throws Exception;

    UserDto addUser(UserBasicDto userBasicDto) throws Exception;

    UserAndJwtDto authenticateUser(UserCredentialsDto userCredentialsDto) throws Exception;

    UserAndJwtDto authenticateVerificationCodeUser(UserCredentialsDto userCredentialsDto) throws Exception;

    UserDto updateUser(UserBasicDto userBasicDto) throws Exception;

    UserDto deactivateUser(String username) throws Exception;

}
