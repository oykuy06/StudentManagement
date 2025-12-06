package com.oyku.SpringStarter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oyku.SpringStarter.DTO.RequestDTO.BookRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.BookResponseDTO;
import com.oyku.SpringStarter.exception.GlobalExceptionHandler;
import com.oyku.SpringStarter.model.Book;
import com.oyku.SpringStarter.service.BookService;
import com.oyku.SpringStarter.mapper.BookMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock private BookService bookService;
    @Mock private BookMapper bookMapper;

    @InjectMocks
    private BookController bookController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllBooks_shouldReturnBooks() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

        Book book = new Book();
        book.setId(1);
        book.setTitle("Test Book");

        BookResponseDTO res = new BookResponseDTO();
        res.setId(1);
        res.setTitle("Test Book");

        when(bookService.getAllBooks()).thenReturn(List.of(book));
        when(bookMapper.toDTO(any())).thenReturn(res);

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].title").value("Test Book"))
                .andExpect(jsonPath("$.data[0].id").value(1));
    }

    @Test
    void getBookById_shouldReturnBook() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

        Book book = new Book();
        book.setId(1);
        book.setTitle("Test Book");

        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(1);
        dto.setTitle("Test Book");

        when(bookService.getBookById(1)).thenReturn(book);
        when(bookMapper.toDTO(book)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Test Book"));
    }

    @Test
    void getBookById_shouldReturn404_whenNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).setControllerAdvice(new GlobalExceptionHandler()).build();

        when(bookService.getBookById(99)).thenThrow(new EntityNotFoundException("Book not found"));

        mockMvc.perform(get("/api/v1/books/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Book not found"));
    }

    @Test
    void createBook_shouldReturnCreated() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

        BookRequestDTO dto = new BookRequestDTO();
        dto.setTitle("New Book");
        dto.setStudentId(1);
        dto.setLibraryId(1);

        Book b = new Book();
        b.setId(1);
        b.setTitle("New Book");

        BookResponseDTO res = new BookResponseDTO();
        res.setId(1);
        res.setTitle("New Book");

        when(bookService.createBook(any())).thenReturn(b);
        when(bookMapper.toDTO(b)).thenReturn(res);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value("New Book"))
                .andExpect(header().string("Location", "/api/v1/books/1"));
    }

    @Test
    void createBook_shouldReturnBadRequest_whenTitleNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        BookRequestDTO dto = new BookRequestDTO();
        dto.setTitle(null);
        dto.setStudentId(1);
        dto.setLibraryId(1);

        when(bookService.createBook(any())).thenThrow(new IllegalArgumentException("Title cannot be null"));

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Title cannot be null"));
    }

    @Test
    void updateBook_shouldReturnUpdated() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

        BookRequestDTO dto = new BookRequestDTO();
        dto.setTitle("Updated Book");
        dto.setStudentId(1);
        dto.setLibraryId(1);

        Book b = new Book();
        b.setId(1);
        b.setTitle("Updated Book");

        BookResponseDTO res = new BookResponseDTO();
        res.setId(1);
        res.setTitle("Updated Book");

        when(bookService.updateBook(eq(1), any())).thenReturn(b);
        when(bookMapper.toDTO(b)).thenReturn(res);

        mockMvc.perform(put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Updated Book"));
    }

    @Test
    void updateBook_shouldReturnBadRequest_whenTitleNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        BookRequestDTO dto = new BookRequestDTO();
        dto.setTitle(null);

        when(bookService.updateBook(eq(1), any())).thenThrow(new IllegalArgumentException("Title cannot be null"));

        mockMvc.perform(put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Title cannot be null"));
    }

    @Test
    void updateBook_shouldReturn404_whenNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        BookRequestDTO dto = new BookRequestDTO();
        dto.setTitle("Updated Book");
        dto.setStudentId(1);
        dto.setLibraryId(1);

        when(bookService.updateBook(eq(99), any()))
                .thenThrow(new EntityNotFoundException("Book not found"));

        mockMvc.perform(put("/api/v1/books/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Book not found"));

        verify(bookService).updateBook(eq(99), any());
    }

    @Test
    void deleteBook_shouldReturnNoContent() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

        doNothing().when(bookService).deleteBook(1);

        mockMvc.perform(delete("/api/v1/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBook_shouldReturn404_whenNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        doThrow(new EntityNotFoundException("Book not found"))
                .when(bookService).deleteBook(99);

        mockMvc.perform(delete("/api/v1/books/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found"));
    }
}
