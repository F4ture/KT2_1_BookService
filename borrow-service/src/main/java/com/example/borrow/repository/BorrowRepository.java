package com.example.borrow.repository;

import com.example.borrow.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowRepository extends JpaRepository<BorrowRecord, Long> {
    boolean existsByBookIdAndStatus(Long bookId, BorrowRecord.Status status);
}
