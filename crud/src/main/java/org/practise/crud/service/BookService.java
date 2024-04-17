package org.practise.crud.service;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import org.practise.crud.entity.Book;
import org.practise.crud.repository.BookRepository;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders.match;

@Service
public class BookService {
    private final ElasticsearchOperations elasticsearchOperations;
    private final BookRepository bookRepository;

    public BookService(ElasticsearchOperations elasticsearchOperations, BookRepository bookRepository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.bookRepository = bookRepository;
    }
    public Optional<Book> getByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public Book create(Book book) {
        if (getByIsbn(book.getIsbn()).isEmpty()) {
            return bookRepository.save(book);
        }else {
            return null;
        }
    }
    public List<Book> getAll() {
        List<Book> books = new ArrayList<>();
        bookRepository.findAll()
                .forEach(books::add);

        System.out.println("List of books "+books);
        return books;
    }

    public List<Book> findByTitleAndAuthor(String title, String author) {
        var criteria = QueryBuilders.bool(builder -> builder.must(
                match(queryAuthor -> queryAuthor.field("authorName").query(author)),
                match(queryTitle -> queryTitle.field("title").query(title))
        ));

        return elasticsearchOperations.search(NativeQuery.builder().withQuery(criteria).build(), Book.class)
                .stream().map(SearchHit::getContent).toList();
    }
    public Book update(String id, Book book){
        Book oldBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("There is not book associated with the given id"));
        oldBook.setIsbn(book.getIsbn());
        oldBook.setAuthorName(book.getAuthorName());
        oldBook.setPublicationYear(book.getPublicationYear());
        oldBook.setTitle(book.getTitle());
        return bookRepository.save(oldBook);
    }
}
