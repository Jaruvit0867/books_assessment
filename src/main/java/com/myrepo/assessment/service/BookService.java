package com.myrepo.assessment.service;

import com.myrepo.assessment.dto.BookRequest;
import com.myrepo.assessment.dto.BookResponse;
import com.myrepo.assessment.entity.Book;
import com.myrepo.assessment.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookResponse createBook(BookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublishedDate(convertBuddhistToGregorian(request.getPublishedDate()));

        Book saved = bookRepository.save(book);
        return toResponse(saved);
    }

    public List<BookResponse> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private LocalDate convertBuddhistToGregorian(String buddhistDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                .withChronology(ThaiBuddhistChronology.INSTANCE);
        return LocalDate.from(formatter.parse(buddhistDate));
    }
    
    private BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublishedDate()
        );
    }
}
