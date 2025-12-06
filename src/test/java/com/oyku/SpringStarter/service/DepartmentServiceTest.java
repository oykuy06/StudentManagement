package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.DTO.RequestDTO.DepartmentRequestDTO;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department dept;

    @BeforeEach
    void setup() {
        dept = new Department();
        dept.setId(1);
        dept.setName("Engineering");
    }

    // GET
    @Test
    void getAllDepartments_shouldReturnList() {
        when(departmentRepository.findAll()).thenReturn(List.of(dept));

        var result = departmentService.getAllDepartments();
        assertEquals(1, result.size());
        verify(departmentRepository).findAll();
    }

    @Test
    void getDepartmentById_shouldReturn_whenExists() {
        when(departmentRepository.findById(1)).thenReturn(Optional.of(dept));

        var result = departmentService.getDepartmentById(1);
        assertEquals("Engineering", result.getName());
    }

    @Test
    void getDepartmentById_shouldThrow_whenMissing() {
        when(departmentRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> departmentService.getDepartmentById(999));
    }

    // CREATE
    @Test
    void createDepartment_shouldSave_whenValid() {
        DepartmentRequestDTO dto = new DepartmentRequestDTO();
        dto.setName("Software");

        when(departmentRepository.save(any())).thenReturn(dept);

        var result = departmentService.createDepartment(dto);
        assertNotNull(result);
        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    void createDepartment_shouldThrow_whenNameNull() {
        DepartmentRequestDTO dto = new DepartmentRequestDTO();
        dto.setName(null);

        assertThrows(IllegalArgumentException.class,
                () -> departmentService.createDepartment(dto));
    }

    // UPDATE
    @Test
    void updateDepartment_shouldUpdate_whenValid() {
        DepartmentRequestDTO dto = new DepartmentRequestDTO();
        dto.setName("Updated Dept");

        when(departmentRepository.findById(1)).thenReturn(Optional.of(dept));
        when(departmentRepository.save(any())).thenReturn(dept);

        var result = departmentService.updateDepartment(1, dto);
        assertEquals("Updated Dept", result.getName());
    }

    @Test
    void updateDepartment_shouldThrow_whenNotFound() {
        DepartmentRequestDTO dto = new DepartmentRequestDTO();
        dto.setName("Updated Dept");

        when(departmentRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> departmentService.updateDepartment(99, dto));
    }

    @Test
    void updateDepartment_shouldThrow_whenNameNull() {
        DepartmentRequestDTO dto = new DepartmentRequestDTO();
        dto.setName(null);

        when(departmentRepository.findById(1)).thenReturn(Optional.of(dept));

        assertThrows(IllegalArgumentException.class,
                () -> departmentService.updateDepartment(1, dto));
    }

    // DELETE
    @Test
    void deleteDepartment_shouldDelete_whenExists() {
        when(departmentRepository.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() -> departmentService.deleteDepartment(1));
        verify(departmentRepository).deleteById(1);
    }

    @Test
    void deleteDepartment_shouldThrow_whenNotExists() {
        when(departmentRepository.existsById(999)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> departmentService.deleteDepartment(999));
    }
}
