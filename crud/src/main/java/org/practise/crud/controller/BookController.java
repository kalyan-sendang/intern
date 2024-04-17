package org.practise.crud.controller;

import org.practise.crud.entity.Book;
import org.practise.crud.service.BookService;
import org.practise.crud.utils.ResponseWrapper;
import org.springframework.http.HttpStatus;
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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/query")
    public List<Book> getBooksByAuthorAndTitle(@RequestParam(value = "title") String title, @RequestParam(value = "author") String author) {
        return bookService.findByTitleAndAuthor(title, author);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{isbn}")
    public Book getBookByIsbn(@PathVariable String isbn) {
        return bookService.getByIsbn(isbn).orElseThrow(() -> new RuntimeException("The given isbn is invalid"));
    }

    @GetMapping(value = "/get-all")
    public ResponseEntity<ResponseWrapper<List<Book>>> getAll() {
        ResponseWrapper<List<Book>> response = new ResponseWrapper<>();
        try {
            response.setSuccess(true);
            response.setMessage("Books Retrieved Successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setResponse(bookService.getAll());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.NO_CONTENT.value());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.create(book);
    }
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "update/{id}")
    public Book updateBook(@PathVariable String id, @RequestBody Book book){
        return bookService.update(id, book);
    }


}
