package com.example.user.web;

import com.example.user.model.LibraryUser;
import com.example.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;
    public UserController(UserService service) { this.service = service; }

    @GetMapping public List<LibraryUser> all() { return service.findAll(); }
    @GetMapping("/{id}") public LibraryUser one(@PathVariable Long id) { return service.findById(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public LibraryUser create(@RequestBody LibraryUser user) { return service.create(user); }
    @PutMapping("/{id}") public LibraryUser update(@PathVariable Long id, @RequestBody LibraryUser user) { return service.update(id, user); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.delete(id); }
}
