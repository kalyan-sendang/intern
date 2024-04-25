package org.practise.crud.service;

import org.practise.crud.entity.Book;
import org.practise.crud.entity.dto.BookRequestDto;
import org.practise.crud.entity.dto.BookResponseDto;


import java.util.List;

public interface BookService {
    BookResponseDto getByIsbn(String isbn);
    Book create(BookRequestDto book);
    List<Book> getAll();
    List<Book> findByTitleAndAuthor(String title, String author);
    Book update(String id, BookRequestDto book);
}
