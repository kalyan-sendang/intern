package org.practise.crud.mapper;

import org.mapstruct.Mapper;
import org.practise.crud.entity.Book;
import org.practise.crud.entity.dto.BookRequestDto;
import org.practise.crud.entity.dto.BookResponseDto;

@Mapper(componentModel = "spring")
public interface BookMapper {
    //    BookResponseDto bookToBookDTO(Book book);
    Book bookRequestDTOToBook(BookRequestDto bookRequestDto);

    BookRequestDto bookToBookDto(Book book);


    BookResponseDto bookToResponseDto(Book book);
}
