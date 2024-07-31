package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private User user;
    private UserDto userDto;
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

      
        user = new User();
        user.setId(1L);
        user.setEmail("john.doe@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");

        
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("john.doe@example.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");


        userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("john.doe@example.com");
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testFindById_Success() {
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> response = userController.findById("1");

        assertEquals(ResponseEntity.ok(userDto), response);
    }

    @Test
    public void testFindById_NotFound() {
        when(userService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = userController.findById("1");

        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    public void testFindById_BadRequest() {
        ResponseEntity<?> response = userController.findById("invalid-id");

        assertEquals(ResponseEntity.badRequest().build(), response);
    }

    @Test
    public void testSave_Success() {
        when(userService.findById(1L)).thenReturn(user);

        ResponseEntity<?> response = userController.save("1");

        assertEquals(ResponseEntity.ok().build(), response);
        verify(userService, times(1)).delete(1L);
    }

    @Test
    public void testSave_NotFound() {
        when(userService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = userController.save("1");

        assertEquals(ResponseEntity.notFound().build(), response);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    public void testSave_Unauthorized() {
        when(userService.findById(1L)).thenReturn(user);

        // Change the username to simulate a different user
        when(userDetails.getUsername()).thenReturn("another.user@example.com");

        ResponseEntity<?> response = userController.save("1");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService, never()).delete(anyLong());
    }

    @Test
    public void testSave_BadRequest() {
        ResponseEntity<?> response = userController.save("invalid-id");

        assertEquals(ResponseEntity.badRequest().build(), response);
        verify(userService, never()).delete(anyLong());
    }
}
    

