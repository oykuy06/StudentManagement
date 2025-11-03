package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.DTO.RequestDTO.DepartmentRequestDTO;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.repository.DepartmentRepository;
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

    public Optional<Department> getDepartmentById(int id) {
        return departmentRepository.findById(id);
    }

    public Department createDepartment(DepartmentRequestDTO dto) {
        Department department = new Department();
        department.setName(dto.getName());
        return departmentRepository.save(department);
    }

    public Optional<Department> updateDepartment(int id, DepartmentRequestDTO dto) {
        Optional<Department> optional = departmentRepository.findById(id);
        if (optional.isEmpty()) return Optional.empty();

        Department department = optional.get();
        department.setName(dto.getName());
        departmentRepository.save(department);
        return Optional.of(department);
    }

    public boolean deleteDepartment(int id) {
        if (!departmentRepository.existsById(id)) return false;
        departmentRepository.deleteById(id);
        return true;
    }
}
