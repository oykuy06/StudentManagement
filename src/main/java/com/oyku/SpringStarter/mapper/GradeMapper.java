package com.oyku.SpringStarter.mapper;

import com.oyku.SpringStarter.DTO.ResponseDTO.GradeResponseDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.CourseSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.StudentSummaryDTO;
import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Grade;
import com.oyku.SpringStarter.model.Student;
import org.springframework.stereotype.Component;

@Component
public class GradeMapper {

    public GradeResponseDTO toDTO(Grade grade) {
        if (grade == null) return null;

        GradeResponseDTO dto = new GradeResponseDTO();
        dto.setId(grade.getId());
        dto.setScore(grade.getScore());
        dto.setStudent(mapStudent(grade.getStudent()));
        dto.setCourse(mapCourse(grade.getCourse()));
        return dto;
    }

    private StudentSummaryDTO mapStudent(Student student) {
        if (student == null) return null;

        StudentSummaryDTO s = new StudentSummaryDTO();
        s.setId(student.getId());
        s.setName(student.getName() != null ? student.getName() : "");
        return s;
    }

    private CourseSummaryDTO mapCourse(Course course) {
        if (course == null) return null;

        CourseSummaryDTO c = new CourseSummaryDTO();
        c.setId(course.getId());
        c.setName(course.getName() != null ? course.getName() : "");
        return c;
    }
}
