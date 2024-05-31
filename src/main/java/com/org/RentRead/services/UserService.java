package com.org.RentRead.services;

import com.org.RentRead.dtos.LoginDto;
import com.org.RentRead.dtos.RegistrationDto;
import com.org.RentRead.dtos.UserDto;
import com.org.RentRead.entities.User;

import java.util.List;

public interface UserService {

    User registerUser(RegistrationDto registrationDto);

    void authenticateUser(LoginDto userLoginDto);

    User updateUser(Long userId, UserDto userDto);

    List<User> getAllUsers();
    User getUserById(Long userId);
    void deleteUser(Long userId);
}
