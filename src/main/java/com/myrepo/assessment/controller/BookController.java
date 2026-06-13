package com.myrepo.assessment.controller;

import com.myrepo.assessment.dto.BookRequest;
import com.myrepo.assessment.dto.BookResponse;
import com.myrepo.assessment.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(@Valid @RequestBody BookRequest request) {
        return bookService.createBook(request);
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookResponse>> getBooksByAuthor(@RequestParam String author) {
        if (bookService.getBooksByAuthor(author).size() == 0) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(bookService.getBooksByAuthor(author));
        }
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookResponse> getBooksByById(@PathVariable Long id) {
        Optional<BookResponse> bookResponse  = bookService.getBooksById(id);
        if (bookResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(bookResponse.get());
        }
    }
}
