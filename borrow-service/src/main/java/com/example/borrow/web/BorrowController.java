package com.example.borrow.web;

import com.example.borrow.model.BorrowRecord;
import com.example.borrow.service.BorrowService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrows")
public class BorrowController {
    private final BorrowService service;
    public BorrowController(BorrowService service) { this.service = service; }

    @GetMapping public List<BorrowRecord> all() { return service.findAll(); }
    @GetMapping("/{id}") public BorrowRecord one(@PathVariable Long id) { return service.findById(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public BorrowRecord borrow(@RequestBody BorrowRequest request) { return service.borrow(request.bookId(), request.userId()); }
    @PostMapping("/{id}/return") public BorrowRecord returnBook(@PathVariable Long id) { return service.returnBook(id); }

    public record BorrowRequest(Long bookId, Long userId) {}
}
