package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.LibraryRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.model.Library;
import com.oyku.SpringStarter.response.ApiResponse;
import com.oyku.SpringStarter.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/libraries")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LibraryResponseDTO>>> getAllLibraries() {
        List<LibraryResponseDTO> libraries = libraryService.getAllLibraries().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Libraries fetched successfully", libraries));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LibraryResponseDTO>> getLibraryById(@PathVariable int id) {
        Library library = libraryService.getLibraryById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Library fetched successfully", convertToResponseDTO(library)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LibraryResponseDTO>> addLibrary(@Valid @RequestBody LibraryRequestDTO dto) {
        Library library = new Library();
        library.setName(dto.getName());
        library.setLocation(dto.getLocation());

        Library saved = libraryService.addNewLibrary(library);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Library created successfully", convertToResponseDTO(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LibraryResponseDTO>> updateLibrary(@PathVariable int id,
                                                                         @Valid @RequestBody LibraryRequestDTO dto) {
        Library updatedData = new Library();
        updatedData.setName(dto.getName());
        updatedData.setLocation(dto.getLocation());

        Library updated = libraryService.updateLibrary(id, updatedData);
        return ResponseEntity.ok(new ApiResponse<>(true, "Library updated successfully", convertToResponseDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLibrary(@PathVariable int id) {
        libraryService.deleteLibrary(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Library deleted successfully", null));
    }

    private LibraryResponseDTO convertToResponseDTO(Library library) {
        LibraryResponseDTO dto = new LibraryResponseDTO();
        dto.setId(library.getId());
        dto.setName(library.getName() != null ? library.getName() : "");
        dto.setLocation(library.getLocation() != null ? library.getLocation() : "");

        dto.setBooks(library.getBooks() != null ? library.getBooks().stream().map(book -> {
            BookResponseDTO bDto = new BookResponseDTO();
            bDto.setId(book.getId());
            bDto.setTitle(book.getTitle() != null ? book.getTitle() : "");

            // Student reference
            if (book.getStudent() != null) {
                StudentResponseDTO sDto = new StudentResponseDTO();
                sDto.setId(book.getStudent().getId());
                sDto.setName(book.getStudent().getName() != null ? book.getStudent().getName() : "");

                if (book.getStudent().getDepartment() != null) {
                    DepartmentResponseDTO dDto = new DepartmentResponseDTO();
                    dDto.setId(book.getStudent().getDepartment().getId());
                    dDto.setName(book.getStudent().getDepartment().getName() != null ? book.getStudent().getDepartment().getName() : "");
                    dDto.setStudents(List.of());
                    dDto.setTeachers(List.of());
                    sDto.setDepartment(dDto);
                }

                if (book.getStudent().getProfile() != null) {
                    StudentProfileResponseDTO pDto = new StudentProfileResponseDTO();
                    pDto.setId(book.getStudent().getProfile().getId());
                    pDto.setAddress(book.getStudent().getProfile().getAddress() != null ? book.getStudent().getProfile().getAddress() : "");
                    pDto.setPhone(book.getStudent().getProfile().getPhone() != null ? book.getStudent().getProfile().getPhone() : "");
                    sDto.setProfile(pDto);
                }

                sDto.setBooks(List.of());
                sDto.setCourses(List.of());
                bDto.setStudent(sDto);
            } else {
                bDto.setStudent(null);
            }

            // Library reference
            LibraryResponseDTO libDto = new LibraryResponseDTO();
            libDto.setId(library.getId());
            libDto.setName(library.getName() != null ? library.getName() : "");
            libDto.setLocation(library.getLocation() != null ? library.getLocation() : "");
            libDto.setBooks(List.of());
            bDto.setLibrary(libDto);

            return bDto;
        }).collect(Collectors.toList()) : List.of());

        return dto;
    }
}
