package org.practise.crud.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
@Data
@Document(indexName = "books")
public class Book {
    @Id
    private String id;

    private String title;

    private int publicationYear;

    private String authorName;

    private String isbn;

}
