package com.example.book;

import com.example.book.model.Book;
import com.example.book.repository.BookRepository;
import com.example.book.service.BookService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class BookServiceTest {
    // Business rule: a book can be marked unavailable after a successful loan.
    @Test
    void changesAvailability() {
        BookRepository repository = mock(BookRepository.class);
        Book book = new Book(); book.setId(1L); book.setAvailable(true);
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(repository.save(book)).thenReturn(book);

        Book result = new BookService(repository).changeAvailability(1L, false);

        assertFalse(result.isAvailable());
        verify(repository).save(book);
    }
}
