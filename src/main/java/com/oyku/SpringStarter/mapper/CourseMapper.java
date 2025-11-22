package com.oyku.SpringStarter.mapper;

import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.DTO.SummaryDTO.*;
import com.oyku.SpringStarter.model.Course;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    public CourseResponseDTO toDTO(Course course) {
        if (course == null) return null;

        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName() != null ? course.getName() : "");

        // Teacher summary
        if (course.getTeacher() != null) {
            TeacherSummaryDTO t = new TeacherSummaryDTO();
            t.setId(course.getTeacher().getId());
            t.setName(course.getTeacher().getName() != null ? course.getTeacher().getName() : "");
            t.setTitle(course.getTeacher().getTitle() != null ? course.getTeacher().getTitle() : "");

            // department summary
            if (course.getTeacher().getDepartment() != null) {
                DepartmentSummaryDTO d = new DepartmentSummaryDTO();
                d.setId(course.getTeacher().getDepartment().getId());
                d.setName(course.getTeacher().getDepartment().getName());
                t.setDepartment(d);
            }

            dto.setTeacher(t);
        }

        // Students summary
        if (course.getStudents() != null) {
            dto.setStudents(course.getStudents().stream().map(s -> {
                StudentSummaryDTO sDto = new StudentSummaryDTO();
                sDto.setId(s.getId());
                sDto.setName(s.getName());
                if (s.getDepartment() != null) {
                    DepartmentSummaryDTO d = new DepartmentSummaryDTO();
                    d.setId(s.getDepartment().getId());
                    d.setName(s.getDepartment().getName());
                    sDto.setDepartment(d);
                }
                return sDto;
            }).collect(Collectors.toList()));
        }

        // Grades summary
        if (course.getGrades() != null) {
            dto.setGrades(course.getGrades().stream().map(g -> {
                GradeSummaryDTO gDto = new GradeSummaryDTO();
                gDto.setId(g.getId());
                gDto.setScore(g.getScore());
                return gDto;
            }).collect(Collectors.toList()));
        }

        return dto;
    }
}
