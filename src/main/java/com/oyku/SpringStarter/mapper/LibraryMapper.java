package com.oyku.SpringStarter.mapper;

import com.oyku.SpringStarter.DTO.ResponseDTO.LibraryResponseDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.BookSummaryDTO;
import com.oyku.SpringStarter.model.Book;
import com.oyku.SpringStarter.model.Library;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LibraryMapper {

    public LibraryResponseDTO toDTO(Library library) {
        if (library == null) return null;

        LibraryResponseDTO dto = new LibraryResponseDTO();
        dto.setId(library.getId());
        dto.setName(library.getName() != null ? library.getName() : "");
        dto.setLocation(library.getLocation() != null ? library.getLocation() : "");
        dto.setBooks(mapBooks(library.getBooks()));
        return dto;
    }

    private List<BookSummaryDTO> mapBooks(List<Book> books) {
        if (books == null) return List.of();

        return books.stream()
                .map(this::mapBook)
                .collect(Collectors.toList());
    }

    private BookSummaryDTO mapBook(Book book) {
        BookSummaryDTO dto = new BookSummaryDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle() != null ? book.getTitle() : "");
        return dto;
    }
}
