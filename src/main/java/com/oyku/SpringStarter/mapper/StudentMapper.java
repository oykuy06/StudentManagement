package com.oyku.SpringStarter.mapper;

import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.DTO.SummaryDTO.BookSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.CourseSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.DepartmentSummaryDTO;
import com.oyku.SpringStarter.model.Book;
import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.model.StudentProfile;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentMapper {

    public StudentResponseDTO toDTO(Student student) {
        if (student == null) return null;

        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setName(student.getName() != null ? student.getName() : "");

        dto.setDepartment(mapDepartment(student.getDepartment()));
        dto.setProfile(mapProfile(student.getProfile()));
        dto.setBooks(mapBooks(student.getBooks()));
        dto.setCourses(mapCourses(student.getCourses()));

        return dto;
    }

    private DepartmentSummaryDTO mapDepartment(Department dept) {
        if (dept == null) return null;

        DepartmentSummaryDTO dto = new DepartmentSummaryDTO();
        dto.setId(dept.getId());
        dto.setName(dept.getName() != null ? dept.getName() : "");
        return dto;
    }

    private StudentProfileResponseDTO mapProfile(StudentProfile p) {
        if (p == null) return null;

        StudentProfileResponseDTO dto = new StudentProfileResponseDTO();
        dto.setId(p.getId());
        dto.setAddress(p.getAddress() != null ? p.getAddress() : "");
        dto.setPhone(p.getPhone() != null ? p.getPhone() : "");
        return dto;
    }

    private List<BookSummaryDTO> mapBooks(List<Book> books) {
        if (books == null) return List.of();

        return books.stream()
                .map(b -> {
                    BookSummaryDTO dto = new BookSummaryDTO();
                    dto.setId(b.getId());
                    dto.setTitle(b.getTitle() != null ? b.getTitle() : "");
                    return dto;
                })
                .collect(Collectors.toList());
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
