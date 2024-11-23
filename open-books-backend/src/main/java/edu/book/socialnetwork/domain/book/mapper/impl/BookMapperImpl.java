package edu.book.socialnetwork.domain.book.mapper.impl;

import edu.book.socialnetwork.domain.storage.util.FileUtils;
import edu.book.socialnetwork.domain.book.dto.request.BookRequest;
import edu.book.socialnetwork.domain.book.dto.response.BookResponse;
import edu.book.socialnetwork.domain.book.dto.response.BorrowedBookResponse;
import edu.book.socialnetwork.domain.book.entity.BookEntity;
import edu.book.socialnetwork.domain.book.mapper.BookMapper;
import edu.book.socialnetwork.domain.transaction.entity.BookTransactionHistoryEntity;
import org.springframework.stereotype.Service;

@Service
public class BookMapperImpl implements BookMapper {
    @Override
    public BookEntity toBookEntity(BookRequest bookRequest) {
        return BookEntity.builder()
                .id(bookRequest.id())
                .title(bookRequest.title())
                .authorName(bookRequest.authorName())
                .isbn(bookRequest.isbn())
                .synopsis(bookRequest.synopsis())
                .archived(false)
                .shareable(bookRequest.shareable())
                .build();
    }

    @Override
    public BookResponse toBookResponse(BookEntity bookEntity) {
        return BookResponse.builder()
                .id(bookEntity.getId())
                .title(bookEntity.getTitle())
                .authorName(bookEntity.getAuthorName())
                .isbn(bookEntity.getIsbn())
                .synopsis(bookEntity.getSynopsis())
                .rate(bookEntity.getRate())
                .archived(bookEntity.isArchived())
                .shareable(bookEntity.isShareable())
                .owner(bookEntity.getOwner().fullName())
                .cover(FileUtils.readFileFromLocation(bookEntity.getBookCover()))
                .build();

    }

    @Override
    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistoryEntity history) {
        return BorrowedBookResponse.builder()
                .id(history.getBook().getId())
                .title(history.getBook().getTitle())
                .authorName(history.getBook().getAuthorName())
                .isbn(history.getBook().getIsbn())
                .rate(history.getBook().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}
