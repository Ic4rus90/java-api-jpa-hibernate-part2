package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "book")
public class Book {

    public Book (String title, String genre) {
        this.title = title;
        this.genre = genre;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "genre", nullable = false)
    private String genre;

    // TODO: Research fetch
    @ManyToOne
    @JsonIncludeProperties(value = {"id", "first_name", "last_name", "email", "alive"})
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne
    @JsonIncludeProperties(value = {"id", "name", "location"})
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;
}
