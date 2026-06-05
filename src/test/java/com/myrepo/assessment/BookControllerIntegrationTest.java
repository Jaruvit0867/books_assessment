package com.myrepo.assessment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myrepo.assessment.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void cleanUp() {
        bookRepository.deleteAll();
    }

    @Test
    void shouldCreateBookSuccessfully() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "title", "Harry Potter",
                "author", "JK Rowling",
                "publishedDate", "26-06-2540"
        ));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Harry Potter"))
                .andExpect(jsonPath("$.author").value("JK Rowling"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void shouldReturnBadRequestWhenTitleIsEmpty() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "title", "",
                "author", "JK Rowling",
                "publishedDate", "26-06-2540"
        ));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenAuthorIsEmpty() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "title", "Harry Potter",
                "author", "",
                "publishedDate", "26-06-2540"
        ));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPublishedDateIsInvalid() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "title", "Harry Potter",
                "author", "JK Rowling",
                "publishedDate", "invalid-date"
        ));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPublishedDateIsInFuture() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "title", "Harry Potter",
                "author", "JK Rowling",
                "publishedDate", "01-01-2570"
        ));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetBooksByAuthor() throws Exception {
        // Create 2 books by same author
        String book1 = objectMapper.writeValueAsString(Map.of(
                "title", "Harry Potter",
                "author", "JK Rowling",
                "publishedDate", "26-06-2540"
        ));
        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(book1));

        String book2 = objectMapper.writeValueAsString(Map.of(
                "title", "Fantastic Beasts",
                "author", "JK Rowling",
                "publishedDate", "15-11-2544"
        ));
        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(book2));

        // Get by author
        mockMvc.perform(get("/books").param("author", "JK Rowling"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldReturnEmptyListWhenAuthorNotFound() throws Exception {
        mockMvc.perform(get("/books").param("author", "Unknown Author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
