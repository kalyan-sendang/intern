package org.practise.crud.service.Implementation;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import org.practise.crud.entity.Book;
import org.practise.crud.entity.dto.BookRequestDto;
import org.practise.crud.entity.dto.BookResponseDto;
import org.practise.crud.mapper.BookMapper;
import org.practise.crud.repository.BookRepository;
import org.practise.crud.service.BookService;
import org.practise.crud.utils.ElementAlreadyPresentException;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.util.*;

import static co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders.match;

@Service
public class BookServiceImpl implements BookService {
    private final ElasticsearchOperations elasticsearchOperations;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    public BookServiceImpl(ElasticsearchOperations elasticsearchOperations, BookRepository bookRepository, BookMapper bookMapper) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }
    @Override
    public BookResponseDto getByIsbn(String isbn) {
        Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);
        if (optionalBook.isEmpty()) {
            throw new NoSuchElementException("Book not found");
        }
        return bookMapper.bookToResponseDto(optionalBook.get());
    }

    @Override
    public Book create(BookRequestDto book) {
        Optional<Book> optionalBook = bookRepository.findByIsbn(book.getIsbn());
        if (optionalBook.isEmpty()) {
//            Book books = Book.builder()
//                    .authorName(book.getAuthorName())
//                    .title(book.getTitle())
//                    .isbn(book.getIsbn())
//                    .publicationYear(book.getPublicationYear())
//                    .build();
//            bookRepository.save(books);
//            return bookMapper.createBookResponseDto(books);
            Book book1= bookMapper.bookRequestDTOToBook(book);
            System.out.println(book1);
            return bookRepository.save(book1);

        } else {
            throw new ElementAlreadyPresentException("Book with given Isbn is already present");
        }
    }
    @Override
    public List<Book> getAll() {
        List<Book> books = new ArrayList<>();
        bookRepository.findAll()
                .forEach(books::add);
        if (books.isEmpty()) {
            return Collections.emptyList();
        }
        return books;
    }
    @Override
    public List<Book> findByTitleAndAuthor(String title, String author) {
        var criteria = QueryBuilders.bool(builder -> builder.must(
                match(queryAuthor -> queryAuthor.field("authorName").query(author)),
                match(queryTitle -> queryTitle.field("title").query(title))
        ));

        List<Book> bookList = elasticsearchOperations.search(NativeQuery.builder().withQuery(criteria).build(), Book.class)
                .stream().map(SearchHit::getContent).toList();
        if (bookList.isEmpty()) {
            throw new NoSuchElementException("No such books with author and title found");
        }
        return bookList;
    }
    @Override
    public Book update(String id, BookRequestDto book) {
        Optional<Book> oldBook = bookRepository.findById(id);
        if (oldBook.isEmpty()){
            throw new NoSuchElementException("Book with given Id not Found");
        }
        oldBook.get().setIsbn(book.getIsbn());
        oldBook.get().setAuthorName(book.getAuthorName());
        oldBook.get().setPublicationYear(book.getPublicationYear());
        oldBook.get().setTitle(book.getTitle());
        return bookRepository.save(oldBook.get());
    }
}
