package com.myrepo.assessment.controller;

import com.myrepo.assessment.dto.BookRequest;
import com.myrepo.assessment.dto.BookResponse;
import com.myrepo.assessment.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<BookResponse> getBooksByAuthor(@RequestParam String author) {
        return bookService.getBooksByAuthor(author);
    }
}
