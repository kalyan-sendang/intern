package org.practise.crud.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BookRequestDto {
    @NotBlank(message = "Title is required")
    private String title;
    @NotNull(message = "Cannot be empty")
    private int publicationYear;
    @NotBlank(message = "Author name is required")
    private String authorName;
    @Pattern(regexp = "^\\d{9}[\\dXx]$", message = "ISBN must be of 10 digits")
    private String isbn;
}
