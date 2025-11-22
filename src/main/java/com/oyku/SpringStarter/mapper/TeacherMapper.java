package com.oyku.SpringStarter.mapper;

import com.oyku.SpringStarter.DTO.ResponseDTO.TeacherResponseDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.CourseSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.DepartmentSummaryDTO;
import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.model.Teacher;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeacherMapper {

    public TeacherResponseDTO toDTO(Teacher teacher) {
        if (teacher == null) return null;

        TeacherResponseDTO dto = new TeacherResponseDTO();
        dto.setId(teacher.getId());
        dto.setName(teacher.getName() != null ? teacher.getName() : "");
        dto.setTitle(teacher.getTitle() != null ? teacher.getTitle() : "");

        dto.setDepartment(mapDepartment(teacher.getDepartment()));
        dto.setCourses(mapCourses(teacher.getCourses()));

        return dto;
    }

    private DepartmentSummaryDTO mapDepartment(Department dep) {
        if (dep == null) return null;

        DepartmentSummaryDTO dto = new DepartmentSummaryDTO();
        dto.setId(dep.getId());
        dto.setName(dep.getName() != null ? dep.getName() : "");
        return dto;
    }

    private List<CourseSummaryDTO> mapCourses(List<Course> courses) {
        if (courses == null) return List.of();

        return courses.stream()
                .map(c -> {
                    CourseSummaryDTO dto = new CourseSummaryDTO();
                    dto.setId(c.getId());
                    dto.setName(c.getName() != null ? c.getName() : "");
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
