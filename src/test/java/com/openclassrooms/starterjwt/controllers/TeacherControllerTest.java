package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

   
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");


        teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");
    }

    @Test
    public void testFindById_Success() {
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        ResponseEntity<?> response = teacherController.findById("1");

        assertEquals(ResponseEntity.ok(teacherDto), response);
    }

    @Test
    public void testFindById_NotFound() {
        when(teacherService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = teacherController.findById("1");

        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    public void testFindById_BadRequest() {
        ResponseEntity<?> response = teacherController.findById("invalid-id");

        assertEquals(ResponseEntity.badRequest().build(), response);
    }

    @Test
    public void testFindAll_Success() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);

        List<TeacherDto> teacherDtos = new ArrayList<>();
        teacherDtos.add(teacherDto);

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        ResponseEntity<?> response = teacherController.findAll();

        assertEquals(ResponseEntity.ok(teacherDtos), response);
    }
}
