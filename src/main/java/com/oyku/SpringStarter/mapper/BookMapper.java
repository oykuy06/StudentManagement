package com.oyku.SpringStarter.mapper;

import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.DTO.SummaryDTO.*;
import com.oyku.SpringStarter.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookResponseDTO toDTO(Book book) {
        if (book == null) return null;

        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle() != null ? book.getTitle() : "");

        // Student summary
        if (book.getStudent() != null) {
            StudentSummaryDTO s = new StudentSummaryDTO();
            s.setId(book.getStudent().getId());
            s.setName(book.getStudent().getName() != null ? book.getStudent().getName() : "");

            if (book.getStudent().getDepartment() != null) {
                DepartmentSummaryDTO d = new DepartmentSummaryDTO();
                d.setId(book.getStudent().getDepartment().getId());
                d.setName(book.getStudent().getDepartment().getName() != null
                        ? book.getStudent().getDepartment().getName()
                        : "");
                s.setDepartment(d);
            }

            dto.setStudent(s);
        }

        // Library summary
        if (book.getLibrary() != null) {
            LibrarySummaryDTO lib = new LibrarySummaryDTO();
            lib.setId(book.getLibrary().getId());
            lib.setName(book.getLibrary().getName() != null ? book.getLibrary().getName() : "");
            lib.setLocation(book.getLibrary().getLocation() != null ? book.getLibrary().getLocation() : "");
            dto.setLibrary(lib);
        }

        return dto;
    }
}
