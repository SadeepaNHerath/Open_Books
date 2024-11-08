package edu.book.socialnetwork.mapper;

import edu.book.socialnetwork.dto.request.BookRequest;
import edu.book.socialnetwork.dto.response.BookResponse;
import edu.book.socialnetwork.dto.response.BorrowedBookResponse;
import edu.book.socialnetwork.entity.BookEntity;
import edu.book.socialnetwork.entity.BookTransactionHistoryEntity;

public interface BookMapper {
    BookEntity toBookEntity(BookRequest bookRequest);

    BookResponse toBookResponse(BookEntity bookEntity);

    BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistoryEntity bookTransactionHistoryEntity);
}
