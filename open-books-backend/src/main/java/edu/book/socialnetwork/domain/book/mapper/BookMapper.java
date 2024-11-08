package edu.book.socialnetwork.domain.book.mapper;

import edu.book.socialnetwork.domain.book.dto.request.BookRequest;
import edu.book.socialnetwork.domain.book.dto.response.BookResponse;
import edu.book.socialnetwork.domain.book.dto.response.BorrowedBookResponse;
import edu.book.socialnetwork.domain.book.entity.BookEntity;
import edu.book.socialnetwork.domain.transaction.entity.BookTransactionHistoryEntity;

public interface BookMapper {
    BookEntity toBookEntity(BookRequest bookRequest);

    BookResponse toBookResponse(BookEntity bookEntity);

    BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistoryEntity bookTransactionHistoryEntity);
}
