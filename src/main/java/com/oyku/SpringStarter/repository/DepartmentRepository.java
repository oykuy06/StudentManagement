package com.oyku.SpringStarter.repository;

import com.oyku.SpringStarter.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
