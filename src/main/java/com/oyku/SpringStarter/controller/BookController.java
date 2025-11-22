package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.BookRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.mapper.BookMapper;
import com.oyku.SpringStarter.model.Book;
import com.oyku.SpringStarter.response.ApiResponse;
import com.oyku.SpringStarter.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookResponseDTO>>> getAllBooks() {
        List<BookResponseDTO> books = bookService.getAllBooks().stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Books fetched successfully", books));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDTO>> getBookById(@PathVariable int id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Book fetched successfully", bookMapper.toDTO(book)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponseDTO>> createBook(@Valid @RequestBody BookRequestDTO dto) {
        Book book = bookService.createBook(dto);
        BookResponseDTO responseDTO = bookMapper.toDTO(book);

        // Location header
        URI location = URI.create("/api/v1/books/" + book.getId());
        return ResponseEntity.created(location)
                .body(new ApiResponse<>(true, "Book created successfully", responseDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDTO>> updateBook(@PathVariable int id,
                                                                   @Valid @RequestBody BookRequestDTO dto) {
        Book updated = bookService.updateBook(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Book updated successfully", bookMapper.toDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
