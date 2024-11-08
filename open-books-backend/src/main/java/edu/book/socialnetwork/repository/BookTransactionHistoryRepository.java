package edu.book.socialnetwork.repository;

import edu.book.socialnetwork.entity.BookTransactionHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistoryEntity, Integer> {
    @Query("""
            SELECT history
            FROM BookTransactionHistoryEntity history
            WHERE history.user.id = :userId
            """)
    Page<BookTransactionHistoryEntity> findAllBorrowedBooks(Pageable pageable, @Param("userId") Integer userId);

    @Query("""
            SELECT history
            FROM BookTransactionHistoryEntity history
            WHERE history.book.owner.id = :userId
            """)
    Page<BookTransactionHistoryEntity> findAllReturnedBooks(Pageable pageable, @Param("userId") Integer userId);

    @Query("""
            SELECT (COUNT(*) > 0) AS isBorrowed
            FROM BookTransactionHistoryEntity history
            WHERE history.user.id = :userId
            AND history.book.id = :bookId
            AND history.returnApproved = false
            """)
    boolean isAlreadyBorrowedByUser(@Param("bookId") Integer bookId, @Param("userId") Integer userId);

    @Query("""
            SELECT transaction
            FROM BookTransactionHistoryEntity transaction
            WHERE transaction.book.id = :bookId
            AND transaction.user.id = :userId
            AND transaction.returned = false
            AND transaction.returnApproved = false
            """)
    Optional<BookTransactionHistoryEntity> findByBookIdAndUserId(
            @Param("bookId") Integer bookId,
            @Param("userId") Integer userId
    );

    @Query("""
            SELECT transaction
            FROM BookTransactionHistoryEntity transaction
            WHERE transaction.book.id = :bookId
            AND transaction.book.owner.id = :userId
            AND transaction.returned = true
            AND transaction.returnApproved = false
            """)
    Optional<BookTransactionHistoryEntity> findByBookIdAndOwnerId(
            @Param("bookId") Integer bookId,
            @Param("userId") Integer userId
    );
}