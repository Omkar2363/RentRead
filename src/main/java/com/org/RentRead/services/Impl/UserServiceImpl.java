package com.org.RentRead.services.Impl;

import com.org.RentRead.dtos.LoginDto;
import com.org.RentRead.dtos.RegistrationDto;
import com.org.RentRead.dtos.RentalDto;
import com.org.RentRead.dtos.UserDto;
import com.org.RentRead.entities.Role;
import com.org.RentRead.entities.User;
import com.org.RentRead.exceptions.*;
import com.org.RentRead.repositories.UserRepository;
import com.org.RentRead.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public User registerUser(RegistrationDto registrationDto) {

        System.out.println("RegistrationDto : "+registrationDto);
        User user = modelMapper.map(registrationDto, User.class);

        System.out.println("User Object : "+user);
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailIdAlreadyUsedException("Email id already registered...");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole() != null ? user.getRole() : Role.USER);
        User registeredUser = userRepository.save(user);
        return registeredUser;
    }

    @Override
    public void authenticateUser(LoginDto userLoginDto) {
        String email = userLoginDto.getEmail();
        String password = userLoginDto.getPassword();

        User user = userRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(()-> new UserNotFoundException("Register first to Login..."));

        boolean isAuthenticated = passwordEncoder.matches(password, user.getPassword());

        if(!isAuthenticated){
            throw new PasswordMisMatchException("Enter the correct password.");
        }
    }

    @Override
    public User updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not Found"));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        User updatedUser = userRepository.save(user);
        return updatedUser;
    }



    //Endpoints accessible by the ADMIN only :
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User Not Found"));
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User Not Found"));
        userRepository.delete(user);

    }

}
