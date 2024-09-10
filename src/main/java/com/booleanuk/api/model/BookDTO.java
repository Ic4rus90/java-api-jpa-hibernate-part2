package com.booleanuk.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class BookDTO {

    public BookDTO(String title, String genre, int authorId, int publisherId){
        this.title = title;
        this.genre = genre;
        this.authorId = authorId;
        this.publisherId = publisherId;
    }

    private int id;
    private String title;
    private String genre;
    private int authorId;
    private int publisherId;
}
