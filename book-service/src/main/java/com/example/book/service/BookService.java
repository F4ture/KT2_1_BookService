package com.example.book.service;

import com.example.book.model.Book;
import com.example.book.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookService {
    // BookService is the only component allowed to change book state.
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) { this.bookRepository = bookRepository; }

    public List<Book> getAllBooks() { return bookRepository.findAll(); }

    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }

    public Book saveBook(Book book) { return bookRepository.save(book); }

    public Book updateBook(Long id, Book input) {
        Book book = getBook(id);
        book.setTitle(input.getTitle());
        book.setAuthor(input.getAuthor());
        book.setAvailable(input.isAvailable());
        return bookRepository.save(book);
    }

    public Book changeAvailability(Long id, boolean available) {
        Book book = getBook(id);
        book.setAvailable(available);
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        bookRepository.deleteById(id);
    }
}
