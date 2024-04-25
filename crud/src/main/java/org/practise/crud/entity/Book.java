package org.practise.crud.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "books")
public class Book {
    @Id
    private String id;

    private String title;

    private int publicationYear;

    private String authorName;

    private String isbn;



}
