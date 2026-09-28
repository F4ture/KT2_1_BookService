package com.example.user.service;

import com.example.user.model.LibraryUser;
import com.example.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;
    public UserService(UserRepository repository) { this.repository = repository; }

    public List<LibraryUser> findAll() { return repository.findAll(); }
    public LibraryUser findById(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")); }
    public LibraryUser create(LibraryUser user) { user.setId(null); return repository.save(user); }
    public LibraryUser update(Long id, LibraryUser input) {
        LibraryUser user = findById(id);
        user.setName(input.getName()); user.setEmail(input.getEmail());
        return repository.save(user);
    }
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        repository.deleteById(id);
    }
}
