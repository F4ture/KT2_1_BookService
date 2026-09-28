package com.example.borrow.client;

public record BookResponse(Long id, String title, String author, boolean available) {
}
