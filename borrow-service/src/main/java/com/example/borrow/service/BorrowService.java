package com.example.borrow.service;

import com.example.borrow.client.BookResponse;
import com.example.borrow.client.UserResponse;
import com.example.borrow.model.BorrowRecord;
import com.example.borrow.repository.BorrowRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class BorrowService {
    private static final String BOOKS = "http://book-service/books/";
    private static final String USERS = "http://user-service/users/";

    private final BorrowRepository repository;
    private final RestTemplate restTemplate;

    public BorrowService(BorrowRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public List<BorrowRecord> findAll() { return repository.findAll(); }

    public BorrowRecord findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Borrow record not found"));
    }

    @Transactional
    public BorrowRecord borrow(Long bookId, Long userId) {
        if (bookId == null || userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bookId and userId are required");
        }

        UserResponse user = get(USERS + userId, UserResponse.class, "User service rejected the request");
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        BookResponse book = get(BOOKS + bookId, BookResponse.class, "Book service rejected the request");
        if (book == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        if (!book.available() || repository.existsByBookIdAndStatus(bookId, BorrowRecord.Status.BORROWED)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book is not available");
        }

        setBookAvailability(bookId, false);
        try {
            BorrowRecord record = new BorrowRecord();
            record.setBookId(bookId);
            record.setUserId(userId);
            record.setBorrowDate(LocalDate.now());
            record.setStatus(BorrowRecord.Status.BORROWED);
            return repository.save(record);
        } catch (RuntimeException ex) {
            setBookAvailability(bookId, true);
            throw ex;
        }
    }

    @Transactional
    public BorrowRecord returnBook(Long id) {
        BorrowRecord record = findById(id);
        if (record.getStatus() == BorrowRecord.Status.RETURNED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book has already been returned");
        }
        setBookAvailability(record.getBookId(), true);
        record.setStatus(BorrowRecord.Status.RETURNED);
        record.setReturnDate(LocalDate.now());
        return repository.save(record);
    }

    private <T> T get(String url, Class<T> type, String message) {
        try {
            return restTemplate.getForObject(url, type);
        } catch (RestClientResponseException ex) {
            HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
            throw new ResponseStatusException(status == null ? HttpStatus.BAD_GATEWAY : status, message, ex);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, message, ex);
        }
    }

    private void setBookAvailability(Long bookId, boolean available) {
        try {
            restTemplate.put(BOOKS + bookId + "/availability", Map.of("available", available));
        } catch (RestClientResponseException ex) {
            HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
            throw new ResponseStatusException(status == null ? HttpStatus.BAD_GATEWAY : status,
                    "Could not update book availability", ex);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Book service is unavailable", ex);
        }
    }
}
