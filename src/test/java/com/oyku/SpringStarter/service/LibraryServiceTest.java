package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.model.Library;
import com.oyku.SpringStarter.repository.LibraryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceTest {

    @Mock
    private LibraryRepository libraryRepository;

    @InjectMocks
    private LibraryService libraryService;

    private Library library;

    @BeforeEach
    void setup() {
        library = new Library();
        library.setId(1);
        library.setName("Central Library");
        library.setLocation("Main Street");
    }

    // -------------------- GET --------------------

    @Test
    void getAllLibraries_shouldReturnList() {
        when(libraryRepository.findAll()).thenReturn(List.of(library));

        List<Library> result = libraryService.getAllLibraries();

        assertEquals(1, result.size());
        verify(libraryRepository).findAll();
    }

    @Test
    void getLibraryById_shouldReturnLibrary_whenExists() {
        when(libraryRepository.findById(1)).thenReturn(Optional.of(library));

        Library result = libraryService.getLibraryById(1);

        assertEquals("Central Library", result.getName());
        verify(libraryRepository).findById(1);
    }

    @Test
    void getLibraryById_shouldThrowException_whenNotFound() {
        when(libraryRepository.findById(2)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> libraryService.getLibraryById(2));

        assertTrue(ex.getMessage().contains("Library not found"));
    }

    // -------------------- CREATE --------------------

    @Test
    void addNewLibrary_shouldSaveAndReturn() {
        when(libraryRepository.save(any(Library.class))).thenReturn(library);

        Library result = libraryService.addNewLibrary(library);

        assertNotNull(result);
        verify(libraryRepository).save(any(Library.class));
    }

    // -------------------- UPDATE --------------------

    @Test
    void updateLibrary_shouldUpdate_whenExists() {
        when(libraryRepository.findById(1)).thenReturn(Optional.of(library));
        when(libraryRepository.save(any(Library.class))).thenReturn(library);

        Library updatedData = new Library();
        updatedData.setName("Updated Library");
        updatedData.setLocation("Updated Location");

        Library result = libraryService.updateLibrary(1, updatedData);

        assertEquals("Updated Library", result.getName());
        assertEquals("Updated Location", result.getLocation());
        verify(libraryRepository).save(any(Library.class));
    }

    @Test
    void updateLibrary_shouldThrow_whenNotFound() {
        when(libraryRepository.findById(99)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> libraryService.updateLibrary(99, library));

        assertTrue(ex.getMessage().contains("Library not found"));
    }

    // -------------------- DELETE --------------------

    @Test
    void deleteLibrary_shouldDelete_whenExists() {
        when(libraryRepository.existsById(1)).thenReturn(true);
        doNothing().when(libraryRepository).deleteById(1);

        assertDoesNotThrow(() -> libraryService.deleteLibrary(1));
        verify(libraryRepository).deleteById(1);
    }

    @Test
    void deleteLibrary_shouldThrowException_whenNotFound() {
        when(libraryRepository.existsById(99)).thenReturn(false);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> libraryService.deleteLibrary(99));

        assertTrue(ex.getMessage().contains("Library not found"));
    }
}
