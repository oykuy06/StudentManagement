package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.BookRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.model.Book;
import com.oyku.SpringStarter.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookResponseDTO> getAllBooks() {
        return bookService.getAllBooks().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable int id) {
        return bookService.getBookById(id)
                .map(book -> ResponseEntity.ok(convertToResponseDTO(book)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public BookResponseDTO createBook(@RequestBody BookRequestDTO dto) {
        Book book = bookService.createBook(dto);
        return convertToResponseDTO(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable int id, @RequestBody BookRequestDTO dto) {
        Optional<Book> updated = bookService.updateBook(id, dto);
        return updated.map(value -> ResponseEntity.ok(convertToResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable int id) {
        return bookService.deleteBook(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private BookResponseDTO convertToResponseDTO(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());

        // STUDENT
        if (book.getStudent() != null) {
            StudentResponseDTO s = new StudentResponseDTO();
            s.setId(book.getStudent().getId());
            s.setName(book.getStudent().getName() != null ? book.getStudent().getName() : "");

            // Department
            if (book.getStudent().getDepartment() != null) {
                DepartmentResponseDTO d = new DepartmentResponseDTO();
                d.setId(book.getStudent().getDepartment().getId());
                d.setName(book.getStudent().getDepartment().getName() != null ? book.getStudent().getDepartment().getName() : "");

                // ÖNEMLİ: Students ve Teachers listelerini boş liste olarak set et (null değil!)
                d.setStudents(new ArrayList<>());
                d.setTeachers(new ArrayList<>());

                s.setDepartment(d);
            }

            // Profile
            if (book.getStudent().getProfile() != null) {
                StudentProfileResponseDTO p = new StudentProfileResponseDTO();
                p.setId(book.getStudent().getProfile().getId());
                p.setAddress(book.getStudent().getProfile().getAddress() != null ? book.getStudent().getProfile().getAddress() : "");
                p.setPhone(book.getStudent().getProfile().getPhone() != null ? book.getStudent().getProfile().getPhone() : "");
                s.setProfile(p);
            }

            // Books list (boşsa [])
            if (book.getStudent().getBooks() != null && !book.getStudent().getBooks().isEmpty()) {
                s.setBooks(book.getStudent().getBooks().stream()
                        .map(b -> {
                            BookResponseDTO br = new BookResponseDTO();
                            br.setId(b.getId());
                            br.setTitle(b.getTitle());

                            // ÖNEMLİ: Student bilgisini dolu set et
                            StudentResponseDTO bStudent = new StudentResponseDTO();
                            bStudent.setId(book.getStudent().getId());
                            bStudent.setName(book.getStudent().getName() != null ? book.getStudent().getName() : "");

                            // Department'ı da ekle
                            if (book.getStudent().getDepartment() != null) {
                                DepartmentResponseDTO bDep = new DepartmentResponseDTO();
                                bDep.setId(book.getStudent().getDepartment().getId());
                                bDep.setName(book.getStudent().getDepartment().getName() != null ? book.getStudent().getDepartment().getName() : "");
                                bDep.setStudents(new ArrayList<>());
                                bDep.setTeachers(new ArrayList<>());
                                bStudent.setDepartment(bDep);
                            }

                            // Profile'ı da ekle
                            if (book.getStudent().getProfile() != null) {
                                StudentProfileResponseDTO bProfile = new StudentProfileResponseDTO();
                                bProfile.setId(book.getStudent().getProfile().getId());
                                bProfile.setAddress(book.getStudent().getProfile().getAddress() != null ? book.getStudent().getProfile().getAddress() : "");
                                bProfile.setPhone(book.getStudent().getProfile().getPhone() != null ? book.getStudent().getProfile().getPhone() : "");
                                bStudent.setProfile(bProfile);
                            }

                            bStudent.setBooks(new ArrayList<>());   // Sonsuz döngüyü kır
                            bStudent.setCourses(new ArrayList<>()); // Sonsuz döngüyü kır
                            br.setStudent(bStudent);

                            // ÖNEMLİ: Library bilgisini set et
                            if (b.getLibrary() != null) {
                                LibraryResponseDTO bLib = new LibraryResponseDTO();
                                bLib.setId(b.getLibrary().getId());
                                bLib.setName(b.getLibrary().getName() != null ? b.getLibrary().getName() : "");
                                bLib.setLocation(b.getLibrary().getLocation() != null ? b.getLibrary().getLocation() : "");
                                bLib.setBooks(new ArrayList<>());  // Sonsuz döngüyü kır
                                br.setLibrary(bLib);
                            }

                            return br;
                        }).collect(Collectors.toList()));
            } else {
                s.setBooks(new ArrayList<>());
            }

            // Courses list (boşsa [])
            if (book.getStudent().getCourses() != null && !book.getStudent().getCourses().isEmpty()) {
                s.setCourses(book.getStudent().getCourses().stream()
                        .map(c -> {
                            CourseResponseDTO cr = new CourseResponseDTO();
                            cr.setId(c.getId());
                            cr.setName(c.getName() != null ? c.getName() : "");

                            // ÖNEMLİ: Teacher bilgisini set et
                            if (c.getTeacher() != null) {
                                TeacherResponseDTO tr = new TeacherResponseDTO();
                                tr.setId(c.getTeacher().getId());
                                tr.setName(c.getTeacher().getName() != null ? c.getTeacher().getName() : "");
                                tr.setTitle(c.getTeacher().getTitle() != null ? c.getTeacher().getTitle() : "");

                                // Teacher'ın department'ını da set et
                                if (c.getTeacher().getDepartment() != null) {
                                    DepartmentResponseDTO td = new DepartmentResponseDTO();
                                    td.setId(c.getTeacher().getDepartment().getId());
                                    td.setName(c.getTeacher().getDepartment().getName() != null ? c.getTeacher().getDepartment().getName() : "");
                                    td.setStudents(new ArrayList<>());  // Sonsuz döngüyü kır
                                    td.setTeachers(new ArrayList<>());  // Sonsuz döngüyü kır
                                    tr.setDepartment(td);
                                }

                                tr.setCourses(new ArrayList<>());  // Sonsuz döngüyü kır
                                cr.setTeacher(tr);
                            }

                            // recursive olarak students ve grades boş bırakıyoruz
                            cr.setStudents(new ArrayList<>());
                            cr.setGrades(new ArrayList<>());
                            return cr;
                        }).collect(Collectors.toList()));
            } else {
                s.setCourses(new ArrayList<>());
            }

            dto.setStudent(s);
        }

        // LIBRARY
        if (book.getLibrary() != null) {
            LibraryResponseDTO lib = new LibraryResponseDTO();
            lib.setId(book.getLibrary().getId());
            lib.setName(book.getLibrary().getName() != null ? book.getLibrary().getName() : "");
            lib.setLocation(book.getLibrary().getLocation() != null ? book.getLibrary().getLocation() : "");

            // kitap listesini boş veya minimal döndürelim
            if (book.getLibrary().getBooks() != null && !book.getLibrary().getBooks().isEmpty()) {
                lib.setBooks(book.getLibrary().getBooks().stream()
                        .map(b -> {
                            BookResponseDTO br = new BookResponseDTO();
                            br.setId(b.getId());
                            br.setTitle(b.getTitle());

                            // ÖNEMLİ: Student bilgisini DOLU set et (null değil!)
                            if (b.getStudent() != null) {
                                StudentResponseDTO bStudent = new StudentResponseDTO();
                                bStudent.setId(b.getStudent().getId());
                                bStudent.setName(b.getStudent().getName() != null ? b.getStudent().getName() : "");

                                // Department'ı da ekle
                                if (b.getStudent().getDepartment() != null) {
                                    DepartmentResponseDTO bDep = new DepartmentResponseDTO();
                                    bDep.setId(b.getStudent().getDepartment().getId());
                                    bDep.setName(b.getStudent().getDepartment().getName() != null ? b.getStudent().getDepartment().getName() : "");
                                    bDep.setStudents(new ArrayList<>());
                                    bDep.setTeachers(new ArrayList<>());
                                    bStudent.setDepartment(bDep);
                                }

                                // Profile'ı da ekle
                                if (b.getStudent().getProfile() != null) {
                                    StudentProfileResponseDTO bProfile = new StudentProfileResponseDTO();
                                    bProfile.setId(b.getStudent().getProfile().getId());
                                    bProfile.setAddress(b.getStudent().getProfile().getAddress() != null ? b.getStudent().getProfile().getAddress() : "");
                                    bProfile.setPhone(b.getStudent().getProfile().getPhone() != null ? b.getStudent().getProfile().getPhone() : "");
                                    bStudent.setProfile(bProfile);
                                }

                                bStudent.setBooks(new ArrayList<>());
                                bStudent.setCourses(new ArrayList<>());
                                br.setStudent(bStudent);
                            }

                            // ÖNEMLİ: Library bilgisini set et
                            LibraryResponseDTO bLib = new LibraryResponseDTO();
                            bLib.setId(book.getLibrary().getId());
                            bLib.setName(book.getLibrary().getName() != null ? book.getLibrary().getName() : "");
                            bLib.setLocation(book.getLibrary().getLocation() != null ? book.getLibrary().getLocation() : "");
                            bLib.setBooks(new ArrayList<>());  // Sonsuz döngüyü kır
                            br.setLibrary(bLib);

                            return br;
                        }).collect(Collectors.toList()));
            } else {
                lib.setBooks(new ArrayList<>());
            }
            dto.setLibrary(lib);
        }

        return dto;
    }

}
