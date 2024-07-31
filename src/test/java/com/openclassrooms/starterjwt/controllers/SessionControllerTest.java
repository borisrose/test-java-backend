package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    private Session session;
    private SessionDto sessionDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

       
        session = new Session();
        session.setId(1L);
        session.setName("Test Session");
        session.setDate(new Date());
        session.setDescription("This is a test session");

        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Test Session");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("This is a test session DTO");
    }

    @Test
    public void testFindById_Success() {
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.findById("1");

        assertEquals(ResponseEntity.ok(sessionDto), response);
    }

    @Test
    public void testFindById_NotFound() {
        when(sessionService.getById(1L)).thenReturn(null);

        ResponseEntity<?> response = sessionController.findById("1");

        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    public void testFindAll_Success() {
        List<Session> sessions = new ArrayList<>();
        sessions.add(session);

        List<SessionDto> sessionDtos = new ArrayList<>();
        sessionDtos.add(sessionDto);

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        ResponseEntity<?> response = sessionController.findAll();

        assertEquals(ResponseEntity.ok(sessionDtos), response);
    }

    @Test
    public void testCreate_Success() {
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.create(sessionDto);

        assertEquals(ResponseEntity.ok(sessionDto), response);
    }

    @Test
    public void testUpdate_Success() {
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(1L, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.update("1", sessionDto);

        assertEquals(ResponseEntity.ok(sessionDto), response);
    }

    @Test
    public void testDelete_Success() {
        when(sessionService.getById(1L)).thenReturn(session);

        ResponseEntity<?> response = sessionController.save("1");

        verify(sessionService, times(1)).delete(1L);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    public void testParticipate_Success() {
        ResponseEntity<?> response = sessionController.participate("1", "1");

        verify(sessionService, times(1)).participate(1L, 1L);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    public void testNoLongerParticipate_Success() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "1");

        verify(sessionService, times(1)).noLongerParticipate(1L, 1L);
        assertEquals(ResponseEntity.ok().build(), response);
    }
}
