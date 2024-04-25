package org.practise.crud.mapper;

import javax.annotation.processing.Generated;
import org.practise.crud.entity.Book;
import org.practise.crud.entity.dto.BookRequestDto;
import org.practise.crud.entity.dto.BookResponseDto;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-25T17:30:32+0545",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Private Build)"
)
@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public Book bookRequestDTOToBook(BookRequestDto bookRequestDto) {
        if ( bookRequestDto == null ) {
            return null;
        }

        Book.BookBuilder book = Book.builder();

        book.title( bookRequestDto.getTitle() );
        book.publicationYear( bookRequestDto.getPublicationYear() );
        book.authorName( bookRequestDto.getAuthorName() );
        book.isbn( bookRequestDto.getIsbn() );

        return book.build();
    }

    @Override
    public BookRequestDto bookToBookDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookRequestDto bookRequestDto = new BookRequestDto();

        bookRequestDto.setTitle( book.getTitle() );
        bookRequestDto.setPublicationYear( book.getPublicationYear() );
        bookRequestDto.setAuthorName( book.getAuthorName() );
        bookRequestDto.setIsbn( book.getIsbn() );

        return bookRequestDto;
    }

    @Override
    public BookResponseDto bookToResponseDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookResponseDto bookResponseDto = new BookResponseDto();

        bookResponseDto.setTitle( book.getTitle() );
        bookResponseDto.setAuthorName( book.getAuthorName() );

        return bookResponseDto;
    }
}
