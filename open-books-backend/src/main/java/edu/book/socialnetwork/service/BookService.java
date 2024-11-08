package edu.book.socialnetwork.service;

import edu.book.socialnetwork.dto.request.BookRequest;
import edu.book.socialnetwork.dto.response.BookResponse;
import edu.book.socialnetwork.dto.response.BorrowedBookResponse;
import edu.book.socialnetwork.dto.response.PageResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {
    Integer save(BookRequest bookRequest, Authentication connectedUser);
    BookResponse findById(Integer bookId);

    PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser);

    PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser);

    PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser);

    PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser);

    Integer updateShareableStatus(Integer bookId, Authentication connectedUser);

    Integer updateArchivedStatus(Integer bookId, Authentication connectedUser);

    Integer borrowBook(Integer bookId, Authentication connectedUser);

    Integer returnBorrowedBook(Integer bookId, Authentication connectedUser);

    Integer approveReturnBorrowedBook(Integer bookId, Authentication connectedUser);

    void uploadBookCover(MultipartFile file, Authentication connectedUser, Integer bookId);
}
