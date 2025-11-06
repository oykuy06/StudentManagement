package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.DTO.RequestDTO.DepartmentRequestDTO;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(int id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + id));
    }

    public Department createDepartment(DepartmentRequestDTO dto) {
        Department department = new Department();
        department.setName(dto.getName());
        return departmentRepository.save(department);
    }

    public Department updateDepartment(int id, DepartmentRequestDTO dto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + id));

        department.setName(dto.getName());
        return departmentRepository.save(department);
    }

    public void deleteDepartment(int id) {
        if (!departmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Department not found with ID: " + id);
        }
        departmentRepository.deleteById(id);
    }

}
