package com.oyku.SpringStarter.mapper;

import com.oyku.SpringStarter.DTO.ResponseDTO.DepartmentResponseDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.StudentSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.TeacherSummaryDTO;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.model.Teacher;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DepartmentMapper {

    public DepartmentResponseDTO toDTO(Department dep) {
        if (dep == null) return null;

        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setId(dep.getId());
        dto.setName(dep.getName() != null ? dep.getName() : "");

        dto.setStudents(mapStudents(dep.getStudents()));
        dto.setTeachers(mapTeachers(dep.getTeachers()));

        return dto;
    }

    private List<StudentSummaryDTO> mapStudents(List<Student> students) {
        if (students == null) return List.of();

        return students.stream().map(s -> {
            StudentSummaryDTO dto = new StudentSummaryDTO();
            dto.setId(s.getId());
            dto.setName(s.getName() != null ? s.getName() : "");
            return dto;
        }).collect(Collectors.toList());
    }

    private List<TeacherSummaryDTO> mapTeachers(List<Teacher> teachers) {
        if (teachers == null) return List.of();

        return teachers.stream().map(t -> {
            TeacherSummaryDTO dto = new TeacherSummaryDTO();
            dto.setId(t.getId());
            dto.setName(t.getName() != null ? t.getName() : "");
            dto.setTitle(t.getTitle() != null ? t.getTitle() : "");
            return dto;
        }).collect(Collectors.toList());
    }
}
