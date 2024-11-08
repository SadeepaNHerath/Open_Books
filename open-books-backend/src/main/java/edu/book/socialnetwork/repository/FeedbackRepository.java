package edu.book.socialnetwork.repository;

import edu.book.socialnetwork.entity.FeedbackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Integer> {

    @Query("""
            SELECT feedback
            FROM FeedbackEntity feedback
            WHERE feedback.book.id = :bookId
            """)
    Page<FeedbackEntity> findAllByBookId(@Param("bookId") Integer bookId, Pageable pageable);
}
