package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.TeacherRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.CourseResponseDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.DepartmentResponseDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.StudentResponseDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.TeacherResponseDTO;
import com.oyku.SpringStarter.model.Teacher;
import com.oyku.SpringStarter.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService){
        this.teacherService = teacherService;
    }

    @GetMapping
    public List<TeacherResponseDTO> getAllTeachers() {
        return teacherService.getAllTeachers().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> getTeacherById(@PathVariable int id){
        Optional<Teacher> teacher = teacherService.getTeacherById(id);
        return teacher.map(t -> ResponseEntity.ok(convertToResponseDTO(t)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TeacherResponseDTO> addTeacher(@RequestBody TeacherRequestDTO dto){
        Teacher teacher = teacherService.addNewTeacher(dto);
        return ResponseEntity.status(201).body(convertToResponseDTO(teacher));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> updateTeacher(@PathVariable int id,
                                                            @RequestBody TeacherRequestDTO dto){
        Optional<Teacher> updated = teacherService.updateTeacher(id, dto);
        return updated.map(t -> ResponseEntity.ok(convertToResponseDTO(t)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable int id){
        return teacherService.deleteTeacherById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private TeacherResponseDTO convertToResponseDTO(Teacher teacher) {
        TeacherResponseDTO dto = new TeacherResponseDTO();
        dto.setId(teacher.getId());
        dto.setName(teacher.getName());
        dto.setTitle(teacher.getTitle());

        // Department
        if (teacher.getDepartment() != null) {
            DepartmentResponseDTO depDto = new DepartmentResponseDTO();
            depDto.setId(teacher.getDepartment().getId());
            depDto.setName(teacher.getDepartment().getName());

            // Department'taki Students
            depDto.setStudents(teacher.getDepartment().getStudents().stream().map(s -> {
                StudentResponseDTO sDto = new StudentResponseDTO();
                sDto.setId(s.getId());
                sDto.setName(s.getName());

                // Student'ın departmentını set et (sonsuz döngü olmaması için sadece id ve name)
                DepartmentResponseDTO sDep = new DepartmentResponseDTO();
                sDep.setId(teacher.getDepartment().getId());
                sDep.setName(teacher.getDepartment().getName());
                sDep.setStudents(List.of());  // Sonsuz döngüyü kır
                sDep.setTeachers(List.of());  // Sonsuz döngüyü kır
                sDto.setDepartment(sDep);

                // ÖNEMLİ: Profile bilgisini set et
                if (s.getProfile() != null) {
                    com.oyku.SpringStarter.DTO.ResponseDTO.StudentProfileResponseDTO pDto =
                            new com.oyku.SpringStarter.DTO.ResponseDTO.StudentProfileResponseDTO();
                    pDto.setId(s.getProfile().getId());
                    pDto.setAddress(s.getProfile().getAddress() != null ? s.getProfile().getAddress() : "");
                    pDto.setPhone(s.getProfile().getPhone() != null ? s.getProfile().getPhone() : "");
                    sDto.setProfile(pDto);
                }

                sDto.setBooks(List.of());
                sDto.setCourses(List.of());
                return sDto;
            }).collect(Collectors.toList()));

            // Department'taki Teachers
            depDto.setTeachers(teacher.getDepartment().getTeachers().stream().map(t -> {
                TeacherResponseDTO tDto = new TeacherResponseDTO();
                tDto.setId(t.getId());
                tDto.setName(t.getName());
                tDto.setTitle(t.getTitle());

                // Teacher'ın departmentını set et (sonsuz döngü olmaması için sadece id ve name)
                DepartmentResponseDTO tDep = new DepartmentResponseDTO();
                tDep.setId(teacher.getDepartment().getId());
                tDep.setName(teacher.getDepartment().getName());
                tDep.setStudents(List.of());  // Sonsuz döngüyü kır
                tDep.setTeachers(List.of());  // Sonsuz döngüyü kır
                tDto.setDepartment(tDep);

                tDto.setCourses(List.of());
                return tDto;
            }).collect(Collectors.toList()));

            dto.setDepartment(depDto);
        }

        // Courses
        dto.setCourses(teacher.getCourses().stream().map(c -> {
            CourseResponseDTO cDto = new CourseResponseDTO();
            cDto.setId(c.getId());
            cDto.setName(c.getName());

            // Course'un teacher bilgisini set et (sonsuz döngü olmaması için sadece temel bilgiler)
            TeacherResponseDTO cTeacher = new TeacherResponseDTO();
            cTeacher.setId(teacher.getId());
            cTeacher.setName(teacher.getName());
            cTeacher.setTitle(teacher.getTitle());

            // ÖNEMLİ: Course'un teacher'ının department'ını da set et
            if (teacher.getDepartment() != null) {
                DepartmentResponseDTO cTeacherDep = new DepartmentResponseDTO();
                cTeacherDep.setId(teacher.getDepartment().getId());
                cTeacherDep.setName(teacher.getDepartment().getName());
                cTeacherDep.setStudents(List.of());  // Sonsuz döngüyü kır
                cTeacherDep.setTeachers(List.of());  // Sonsuz döngüyü kır
                cTeacher.setDepartment(cTeacherDep);
            }

            cTeacher.setCourses(List.of());  // Sonsuz döngüyü kır
            cDto.setTeacher(cTeacher);

            cDto.setStudents(List.of());
            cDto.setGrades(List.of());
            return cDto;
        }).collect(Collectors.toList()));

        return dto;
    }

}
