package com.lamar.primebox.web.service;

import com.lamar.primebox.web.dto.model.UserBasicDto;
import com.lamar.primebox.web.dto.model.UserCredentialsDto;
import com.lamar.primebox.web.dto.model.UserDto;
import com.lamar.primebox.web.dto.model.UserUpdateDto;

public interface UserService {

    UserDto getUser(String username) throws Exception;

    UserCredentialsDto getUserCredentials(String username) throws Exception;

    UserDto addUser(UserBasicDto userBasicDto) throws Exception;

    UserDto updateUser(UserUpdateDto userUpdateDto) throws Exception;

    UserCredentialsDto deactivateUser(String username) throws Exception;

}
