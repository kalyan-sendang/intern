package org.practise.crud.controller;

import jakarta.validation.Valid;
import org.practise.crud.Response.Response;
import org.practise.crud.Response.SuccessResponse;
import org.practise.crud.entity.Book;
import org.practise.crud.entity.dto.BookRequestDto;
import org.practise.crud.entity.dto.BookResponseDto;
import org.practise.crud.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    @GetMapping(value = "/query")
    public ResponseEntity<Response> getBooksByAuthorAndTitle(@Valid @RequestParam(value = "title") String title, @Valid @RequestParam(value = "author") String author) {
        List<Book> newBooks =  bookService.findByTitleAndAuthor(title, author);
        return ResponseEntity.ok().body(new SuccessResponse<>(newBooks));
    }

    @GetMapping(value = "/{isbn}")
    public ResponseEntity<Response> getBookByIsbn(@PathVariable String isbn) {
        BookResponseDto book = bookService.getByIsbn(isbn);
        return ResponseEntity.ok().body(new SuccessResponse<>(book));
    }

    @GetMapping(value = "/get-all")
    public ResponseEntity<Response> getAll() {
        List<Book> books = bookService.getAll();
        return ResponseEntity.ok().body(new SuccessResponse<>(books));
    }
    @PostMapping
    public ResponseEntity<Response> createBook(@Valid @RequestBody BookRequestDto book) {
        Book newBook = bookService.create(book);
        return ResponseEntity.ok().body(new SuccessResponse<>(newBook));
    }
    @PutMapping(value = "update/{id}")
    public ResponseEntity<Response> updateBook(@PathVariable String id, @Valid @RequestBody BookRequestDto book){
        Book updatedBook = bookService.update(id, book);
        return ResponseEntity.ok().body(new SuccessResponse<>(updatedBook));
    }


}
