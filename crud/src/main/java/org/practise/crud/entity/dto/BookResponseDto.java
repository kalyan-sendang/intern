package org.practise.crud.entity.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BookResponseDto{
    private String title;
    private String authorName;
};
