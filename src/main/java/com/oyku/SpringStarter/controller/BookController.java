package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.BookRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.model.Book;
import com.oyku.SpringStarter.response.ApiResponse;
import com.oyku.SpringStarter.service.BookService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApiResponse<List<BookResponseDTO>>> getAllBooks() {
        List<BookResponseDTO> books = bookService.getAllBooks().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Books fetched successfully", books));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDTO>> getBookById(@PathVariable int id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Book fetched successfully", convertToResponseDTO(book)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponseDTO>> createBook(@Valid @RequestBody BookRequestDTO dto) {
        Book book = bookService.createBook(dto);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Book created successfully", convertToResponseDTO(book)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDTO>> updateBook(@PathVariable int id,
                                                                   @Valid @RequestBody BookRequestDTO dto) {
        Book updated = bookService.updateBook(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Book updated successfully", convertToResponseDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Book deleted successfully", null));
    }


    private BookResponseDTO convertToResponseDTO(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle() != null ? book.getTitle() : "");

        // Student
        if (book.getStudent() != null) {
            StudentResponseDTO s = new StudentResponseDTO();
            s.setId(book.getStudent().getId());
            s.setName(book.getStudent().getName() != null ? book.getStudent().getName() : "");

            // Department
            if (book.getStudent().getDepartment() != null) {
                DepartmentResponseDTO d = new DepartmentResponseDTO();
                d.setId(book.getStudent().getDepartment().getId());
                d.setName(book.getStudent().getDepartment().getName() != null ? book.getStudent().getDepartment().getName() : "");
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

            s.setBooks(new ArrayList<>());
            s.setCourses(new ArrayList<>());
            dto.setStudent(s);
        }

        // Library
        if (book.getLibrary() != null) {
            LibraryResponseDTO lib = new LibraryResponseDTO();
            lib.setId(book.getLibrary().getId());
            lib.setName(book.getLibrary().getName() != null ? book.getLibrary().getName() : "");
            lib.setLocation(book.getLibrary().getLocation() != null ? book.getLibrary().getLocation() : "");
            lib.setBooks(new ArrayList<>());
            dto.setLibrary(lib);
        }

        return dto;
    }
}
