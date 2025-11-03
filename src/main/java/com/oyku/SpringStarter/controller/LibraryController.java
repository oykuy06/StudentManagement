package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.LibraryRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.LibraryResponseDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.BookResponseDTO;
import com.oyku.SpringStarter.model.Library;
import com.oyku.SpringStarter.model.Book;
import com.oyku.SpringStarter.service.LibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/libraries")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public List<LibraryResponseDTO> getAllLibraries() {
        return libraryService.getAllLibraries().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibraryResponseDTO> getLibraryById(@PathVariable int id) {
        Optional<Library> libraryOpt = libraryService.getLibraryById(id);
        return libraryOpt.map(library -> ResponseEntity.ok(convertToResponseDTO(library)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LibraryResponseDTO> addLibrary(@RequestBody LibraryRequestDTO dto) {
        Library library = new Library();
        library.setName(dto.getName());
        library.setLocation(dto.getLocation());
        Library saved = libraryService.addNewLibrary(library);
        return ResponseEntity.status(201).body(convertToResponseDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibraryResponseDTO> updateLibrary(@PathVariable int id, @RequestBody LibraryRequestDTO dto) {
        Optional<Library> optional = libraryService.getLibraryById(id);
        if(optional.isEmpty()) return ResponseEntity.notFound().build();

        Library library = optional.get();
        library.setName(dto.getName());
        library.setLocation(dto.getLocation());
        libraryService.updateLibrary(library);
        return ResponseEntity.ok(convertToResponseDTO(library));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibrary(@PathVariable int id) {
        return libraryService.deleteLibrary(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private LibraryResponseDTO convertToResponseDTO(Library library) {
        LibraryResponseDTO dto = new LibraryResponseDTO();
        dto.setId(library.getId());
        dto.setName(library.getName() != null ? library.getName() : "");
        dto.setLocation(library.getLocation() != null ? library.getLocation() : "");

        if (library.getBooks() != null && !library.getBooks().isEmpty()) {
            List<BookResponseDTO> books = library.getBooks().stream().map(book -> {
                BookResponseDTO bDto = new BookResponseDTO();
                bDto.setId(book.getId());
                bDto.setTitle(book.getTitle() != null ? book.getTitle() : "");

                // Student reference
                if (book.getStudent() != null) {
                    com.oyku.SpringStarter.DTO.ResponseDTO.StudentResponseDTO sDto =
                            new com.oyku.SpringStarter.DTO.ResponseDTO.StudentResponseDTO();
                    sDto.setId(book.getStudent().getId());
                    sDto.setName(book.getStudent().getName() != null ? book.getStudent().getName() : "");

                    // ÖNEMLİ: Department'ı da ekle
                    if (book.getStudent().getDepartment() != null) {
                        com.oyku.SpringStarter.DTO.ResponseDTO.DepartmentResponseDTO dDto =
                                new com.oyku.SpringStarter.DTO.ResponseDTO.DepartmentResponseDTO();
                        dDto.setId(book.getStudent().getDepartment().getId());
                        dDto.setName(book.getStudent().getDepartment().getName() != null ? book.getStudent().getDepartment().getName() : "");
                        dDto.setStudents(List.of());
                        dDto.setTeachers(List.of());
                        sDto.setDepartment(dDto);
                    }

                    // ÖNEMLİ: Profile'ı da ekle
                    if (book.getStudent().getProfile() != null) {
                        com.oyku.SpringStarter.DTO.ResponseDTO.StudentProfileResponseDTO pDto =
                                new com.oyku.SpringStarter.DTO.ResponseDTO.StudentProfileResponseDTO();
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

                // ÖNEMLİ: Library reference (sonsuz döngüyü kırmak için minimal)
                LibraryResponseDTO libDto = new LibraryResponseDTO();
                libDto.setId(library.getId());
                libDto.setName(library.getName() != null ? library.getName() : "");
                libDto.setLocation(library.getLocation() != null ? library.getLocation() : "");
                libDto.setBooks(List.of());  // Sonsuz döngüyü kır
                bDto.setLibrary(libDto);

                return bDto;
            }).collect(Collectors.toList());
            dto.setBooks(books);
        } else {
            dto.setBooks(List.of());
        }

        return dto;
    }

}
