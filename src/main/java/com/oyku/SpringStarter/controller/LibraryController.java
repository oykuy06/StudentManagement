package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.LibraryRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.mapper.LibraryMapper;
import com.oyku.SpringStarter.model.Library;
import com.oyku.SpringStarter.response.ApiResponse;
import com.oyku.SpringStarter.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/libraries")
public class LibraryController {

    private final LibraryService libraryService;
    private final LibraryMapper libraryMapper;

    public LibraryController(LibraryService libraryService, LibraryMapper libraryMapper) {
        this.libraryService = libraryService;
        this.libraryMapper = libraryMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LibraryResponseDTO>>> getAllLibraries() {
        List<LibraryResponseDTO> libraries = libraryService.getAllLibraries().stream()
                .map(libraryMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Libraries fetched successfully", libraries));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LibraryResponseDTO>> getLibraryById(@PathVariable int id) {
        Library library = libraryService.getLibraryById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Library fetched successfully", libraryMapper.toDTO(library)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LibraryResponseDTO>> addLibrary(@Valid @RequestBody LibraryRequestDTO dto) {
        Library library = new Library();
        library.setName(dto.getName());
        library.setLocation(dto.getLocation());
        Library saved = libraryService.addNewLibrary(library);

        LibraryResponseDTO responseDTO = libraryMapper.toDTO(saved);
        URI location = URI.create("/api/v1/libraries/" + saved.getId());
        return ResponseEntity.created(location)
                .body(new ApiResponse<>(true, "Library created successfully", responseDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LibraryResponseDTO>> updateLibrary(@PathVariable int id,
                                                                         @Valid @RequestBody LibraryRequestDTO dto) {
        Library updatedData = new Library();
        updatedData.setName(dto.getName());
        updatedData.setLocation(dto.getLocation());

        Library updated = libraryService.updateLibrary(id, updatedData);
        return ResponseEntity.ok(new ApiResponse<>(true, "Library updated successfully", libraryMapper.toDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibrary(@PathVariable int id) {
        libraryService.deleteLibrary(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
