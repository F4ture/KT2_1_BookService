package com.example.book.web;

import com.example.book.model.Book;
import com.example.book.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) { this.bookService = bookService; }

    @GetMapping public List<Book> getAllBooks() { return bookService.getAllBooks(); }
    @GetMapping("/{id}") public Book getBook(@PathVariable Long id) { return bookService.getBook(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Book createBook(@RequestBody Book book) { return bookService.saveBook(book); }
    @PutMapping("/{id}") public Book updateBook(@PathVariable Long id, @RequestBody Book book) { return bookService.updateBook(id, book); }
    @PatchMapping("/{id}/availability") public Book changeAvailability(@PathVariable Long id, @RequestBody AvailabilityRequest request) {
        return bookService.changeAvailability(id, request.available());
    }
    @PutMapping("/{id}/availability") public Book setAvailability(@PathVariable Long id, @RequestBody AvailabilityRequest request) {
        return bookService.changeAvailability(id, request.available());
    }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void deleteBook(@PathVariable Long id) { bookService.deleteBook(id); }

    public record AvailabilityRequest(boolean available) {}
}
