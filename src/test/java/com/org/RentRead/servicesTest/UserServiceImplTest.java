package com.org.RentRead.servicesTest;

import com.org.RentRead.dtos.LoginDto;
import com.org.RentRead.dtos.RegistrationDto;
import com.org.RentRead.dtos.UserDto;
import com.org.RentRead.entities.Role;
import com.org.RentRead.entities.User;
import com.org.RentRead.exceptions.PasswordMisMatchException;
import com.org.RentRead.exceptions.UserNotFoundException;
import com.org.RentRead.repositories.UserRepository;
import com.org.RentRead.services.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class  UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user1, user2;
    private RegistrationDto registrationDto;
    private LoginDto loginDto;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = new User(1L, "Rashi@example.com", "password", "Rashi", "Yadav", Role.USER);
        user2 = new User(2L, "Rashi@example.com", "password", "Rashi", "Yadav", Role.USER);

        registrationDto = new RegistrationDto("Rashi@example.com", "password", "Rashi", "Yadav", "USER");
        loginDto = new LoginDto("Rashi@example.com", "password");
        userDto = new UserDto("Rashi", "Yadav");
    }

    @Test
    void testRegisterUser() {
        when(modelMapper.map(registrationDto, User.class)).thenReturn(user1);
        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user1.getPassword())).thenReturn(user1.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(user1);

        User registeredUser = userService.registerUser(registrationDto);
        assertNotNull(registeredUser);
        assertEquals(user1, registeredUser);
    }



    @Test
    void testAuthenticateUser() {
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user1));
        when(passwordEncoder.matches(loginDto.getPassword(), user1.getPassword())).thenReturn(true);

        assertDoesNotThrow(() -> userService.authenticateUser(loginDto));
    }

    @Test
    void testAuthenticateUserUserNotFound() {
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.authenticateUser(loginDto));
    }

    @Test
    void testAuthenticateUserPasswordMismatch() {
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user1));
        when(passwordEncoder.matches(loginDto.getPassword(), user1.getPassword())).thenReturn(false);

        assertThrows(PasswordMisMatchException.class, () -> userService.authenticateUser(loginDto));
    }

    @Test
    void testUpdateUser() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenReturn(user1);

        User updatedUser = userService.updateUser(user1.getId(), userDto);
        assertNotNull(updatedUser);
        assertEquals(user1.getId(), updatedUser.getId());
        assertEquals(userDto.getFirstName(), updatedUser.getFirstName());
        assertEquals(userDto.getLastName(), updatedUser.getLastName());
    }

    @Test
    void testUpdateUserUserNotFound() {
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(3L, userDto));
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        List<User> allUsers = userService.getAllUsers();
        assertNotNull(allUsers);
        assertEquals(users, allUsers);
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));

        User foundUser = userService.getUserById(user1.getId());
        assertNotNull(foundUser);
        assertEquals(user1, foundUser);
    }

    @Test
    void testGetUserByIdUserNotFound() {
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(3L));
    }

    @Test
    void testDeleteUser() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));

        assertDoesNotThrow(() -> userService.deleteUser(user1.getId()));
        verify(userRepository, times(1)).delete(user1);
    }

    @Test
    void testDeleteUserUserNotFound() {
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(3L));
    }
}