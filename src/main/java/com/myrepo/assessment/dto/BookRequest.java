package com.myrepo.assessment.dto;

import com.myrepo.assessment.validator.ValidBuddhistDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookRequest {

    @NotBlank(message = "title must not be empty")
    private String title;

    @NotBlank(message = "author must not be empty")
    private String author;

    @NotNull(message = "publishedDate must not be null")
    @ValidBuddhistDate
    private String publishedDate;

    public BookRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }
}
