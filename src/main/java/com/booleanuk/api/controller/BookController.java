package com.booleanuk.api.controller;

import com.booleanuk.api.model.Author;
import com.booleanuk.api.model.Book;
import com.booleanuk.api.model.BookDTO;
import com.booleanuk.api.model.Publisher;
import com.booleanuk.api.repository.AuthorRepository;
import com.booleanuk.api.repository.BookRepository;
import com.booleanuk.api.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    PublisherRepository publisherRepository;

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody BookDTO bookDTO) throws ResponseStatusException {
        try {
            Author author = this.authorRepository
                    .findById(bookDTO.getAuthorId())
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found. Author id: " + bookDTO.getAuthorId()));

            Publisher publisher = this.publisherRepository
                    .findById(bookDTO.getPublisherId())
                            .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publisher not found"));

            Book book = new Book(bookDTO.getTitle(), bookDTO.getGenre());

            book.setAuthor(author);
            book.setPublisher(publisher);

            return new ResponseEntity<>(this.bookRepository.save(book), HttpStatus.CREATED);

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create book: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(){
        return ResponseEntity.ok(this.bookRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable(name = "id") int id) throws ResponseStatusException {
        Book book = this.bookRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        return ResponseEntity.ok(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable (name = "id") int id, @RequestBody BookDTO bookDTO) throws ResponseStatusException {
        Book bookToUpdate = this.bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        try {
            bookToUpdate.setTitle(bookDTO.getTitle());
            bookToUpdate.setGenre(bookDTO.getGenre());

            if (bookDTO.getAuthorId() != bookToUpdate.getAuthor().getId()){
                Author author = authorRepository
                        .findById(bookDTO.getAuthorId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No author with the provided id."));
                bookToUpdate.setAuthor(author);
            }

            if (bookDTO.getPublisherId() != bookToUpdate.getPublisher().getId()){
                Publisher publisher = publisherRepository
                        .findById(bookDTO.getPublisherId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No publisher with the provided id."));
                bookToUpdate.setPublisher(publisher);
            }

            this.bookRepository.save(bookToUpdate);

            return new ResponseEntity<>(bookToUpdate, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An error occurred when attempting to update book: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable (name = "id") int id) throws ResponseStatusException {
        Book bookToDelete = this.bookRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        this.bookRepository.delete(bookToDelete);

        return ResponseEntity.ok(bookToDelete);
    }
}
