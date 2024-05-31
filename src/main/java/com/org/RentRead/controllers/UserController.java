package com.org.RentRead.controllers;

import com.org.RentRead.dtos.LoginDto;
import com.org.RentRead.dtos.RegistrationDto;
import com.org.RentRead.dtos.UserDto;
import com.org.RentRead.entities.User;
import com.org.RentRead.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegistrationDto registrationDto) {
        return new ResponseEntity<>(userService.registerUser(registrationDto), HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<String> authenticateUserLogin(@RequestBody LoginDto userLoginDto){
        userService.authenticateUser(userLoginDto);
        return ResponseEntity.ok().body("Logged in successfully...");
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto){
        return new ResponseEntity<>(userService.updateUser(userId, userDto), HttpStatus.OK);
    }




    //EndPoints Accessible by the ADMIN only :
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>("User with userId "+userId+" has been deleted successfully...!!!", HttpStatus.OK);
    }
}

