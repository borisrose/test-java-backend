package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import java.util.Optional;

public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAuthenticateUser_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("\"test@example.com\"");
        loginRequest.setPassword("\"password123\"");
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwtToken");
        when(userDetails.getId()).thenReturn(1L);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(userDetails.getFirstName()).thenReturn("John");
        when(userDetails.getLastName()).thenReturn("Doe");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new User()));

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);
        JwtResponse jwtResponse = (JwtResponse) response.getBody();

        assertEquals("jwtToken", jwtResponse.getToken());
        assertEquals("test@example.com", jwtResponse.getUsername());
    }

    @Test
    public void testRegisterUser_EmailExists() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("Error: Email is already taken!", messageResponse.getMessage());
    }

    @Test
    public void testRegisterUser_Success() {
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setEmail("newuser@example.com");
    signupRequest.setFirstName("Jane");
    signupRequest.setLastName("Doe");
    signupRequest.setPassword("password123");

 
    when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
    

    when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

    User user = new User(signupRequest.getEmail(), signupRequest.getLastName(), signupRequest.getFirstName(), "encodedPassword", false);
    when(userRepository.save(any(User.class))).thenReturn(user);

    ResponseEntity<?> response = authController.registerUser(signupRequest);

    MessageResponse messageResponse = (MessageResponse) response.getBody();
    assertEquals("User registered successfully!", messageResponse.getMessage());
    }
}

