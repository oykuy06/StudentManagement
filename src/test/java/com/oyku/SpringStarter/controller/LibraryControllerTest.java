package com.oyku.SpringStarter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oyku.SpringStarter.DTO.RequestDTO.LibraryRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.LibraryResponseDTO;
import com.oyku.SpringStarter.exception.GlobalExceptionHandler;
import com.oyku.SpringStarter.mapper.LibraryMapper;
import com.oyku.SpringStarter.model.Library;
import com.oyku.SpringStarter.service.LibraryService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class LibraryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LibraryService libraryService;

    @Mock
    private LibraryMapper libraryMapper;

    @InjectMocks
    private LibraryController libraryController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllLibraries_shouldReturnLibraries() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController).build();

        Library lib = new Library();
        lib.setId(1);
        lib.setName("Central");
        lib.setLocation("City");

        LibraryResponseDTO dto = new LibraryResponseDTO();
        dto.setId(1);
        dto.setName("Central");
        dto.setLocation("City");

        when(libraryService.getAllLibraries()).thenReturn(List.of(lib));
        when(libraryMapper.toDTO(lib)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/libraries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("Central"));

        verify(libraryService).getAllLibraries();
    }

    @Test
    void getLibraryById_shouldReturnLibrary() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController).build();

        Library lib = new Library();
        lib.setId(1);
        lib.setName("Central");
        lib.setLocation("City");

        LibraryResponseDTO dto = new LibraryResponseDTO();
        dto.setId(1);
        dto.setName("Central");
        dto.setLocation("City");

        when(libraryService.getLibraryById(1)).thenReturn(lib);
        when(libraryMapper.toDTO(lib)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/libraries/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Central"));

        verify(libraryService).getLibraryById(1);
    }

    @Test
    void getLibraryById_shouldReturn404_whenNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        when(libraryService.getLibraryById(99)).thenThrow(new EntityNotFoundException("Library not found"));

        mockMvc.perform(get("/api/v1/libraries/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Library not found"));
    }

    @Test
    void addLibrary_shouldReturnCreatedLibrary() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController).build();

        LibraryRequestDTO requestDTO = new LibraryRequestDTO();
        requestDTO.setName("Central");
        requestDTO.setLocation("City");

        Library lib = new Library();
        lib.setId(1);
        lib.setName("Central");
        lib.setLocation("City");

        LibraryResponseDTO responseDTO = new LibraryResponseDTO();
        responseDTO.setId(1);
        responseDTO.setName("Central");
        responseDTO.setLocation("City");

        when(libraryService.addNewLibrary(any(Library.class))).thenReturn(lib);
        when(libraryMapper.toDTO(lib)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/libraries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Central"))
                .andExpect(header().string("Location", "/api/v1/libraries/1"));

        verify(libraryService).addNewLibrary(any(Library.class));
    }

    @Test
    void addLibrary_shouldReturnBadRequest_whenNameNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        LibraryRequestDTO requestDTO = new LibraryRequestDTO();
        requestDTO.setName(null); // invalid
        requestDTO.setLocation("City");

        when(libraryService.addNewLibrary(any())).thenThrow(new IllegalArgumentException("Name cannot be null"));

        mockMvc.perform(post("/api/v1/libraries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Name cannot be null"));
    }

    @Test
    void updateLibrary_shouldReturnUpdatedLibrary() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController).build();

        LibraryRequestDTO requestDTO = new LibraryRequestDTO();
        requestDTO.setName("Main Library");
        requestDTO.setLocation("Downtown");

        Library lib = new Library();
        lib.setId(1);
        lib.setName("Main Library");
        lib.setLocation("Downtown");

        LibraryResponseDTO responseDTO = new LibraryResponseDTO();
        responseDTO.setId(1);
        responseDTO.setName("Main Library");
        responseDTO.setLocation("Downtown");

        when(libraryService.updateLibrary(eq(1), any(Library.class))).thenReturn(lib);
        when(libraryMapper.toDTO(lib)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/libraries/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Main Library"));

        verify(libraryService).updateLibrary(eq(1), any(Library.class));
    }

    @Test
    void updateLibrary_shouldReturnBadRequest_whenNameNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        LibraryRequestDTO requestDTO = new LibraryRequestDTO();
        requestDTO.setName(null); // invalid
        requestDTO.setLocation("Downtown");

        when(libraryService.updateLibrary(eq(1), any())).thenThrow(new IllegalArgumentException("Name cannot be null"));

        mockMvc.perform(put("/api/v1/libraries/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Name cannot be null"));
    }

    @Test
    void deleteLibrary_shouldReturnNoContent() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController).build();

        doNothing().when(libraryService).deleteLibrary(1);

        mockMvc.perform(delete("/api/v1/libraries/1"))
                .andExpect(status().isNoContent());

        verify(libraryService).deleteLibrary(1);
    }

    @Test
    void deleteLibrary_shouldReturn404_whenNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        doThrow(new EntityNotFoundException("Library not found"))
                .when(libraryService).deleteLibrary(99);

        mockMvc.perform(delete("/api/v1/libraries/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Library not found"));

        verify(libraryService).deleteLibrary(99);
    }
}