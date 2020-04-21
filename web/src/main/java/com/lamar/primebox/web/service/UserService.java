package com.lamar.primebox.web.service;

import com.lamar.primebox.web.dto.model.UserDto;
import com.lamar.primebox.web.dto.model.UserCredentialsDto;
import com.lamar.primebox.web.dto.model.UserBasicDto;
import com.lamar.primebox.web.dto.model.UserJwtDto;

public interface UserService {

    UserDto getUser(String username) throws Exception;

    UserDto addUser(UserBasicDto userBasicDto) throws Exception;

    UserJwtDto authenticateUser(UserCredentialsDto userCredentialsDto) throws Exception;

    UserDto updateUser(UserBasicDto userBasicDto) throws Exception;

    UserDto deactivateUser(String username) throws Exception;

}
