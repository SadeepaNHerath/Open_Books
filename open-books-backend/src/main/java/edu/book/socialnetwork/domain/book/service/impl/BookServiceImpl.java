package edu.book.socialnetwork.domain.book.service.impl;

import edu.book.socialnetwork.core.exception.OperationNotPermittedException;
import edu.book.socialnetwork.domain.book.dto.request.BookRequest;
import edu.book.socialnetwork.domain.book.dto.response.BookResponse;
import edu.book.socialnetwork.domain.book.dto.response.BorrowedBookResponse;
import edu.book.socialnetwork.domain.book.entity.BookEntity;
import edu.book.socialnetwork.domain.book.mapper.BookMapper;
import edu.book.socialnetwork.domain.book.repository.BookRepository;
import edu.book.socialnetwork.domain.book.service.BookService;
import edu.book.socialnetwork.domain.book.specification.BookSpecification;
import edu.book.socialnetwork.domain.common.dto.PageResponse;
import edu.book.socialnetwork.domain.storage.service.FileStorageService;
import edu.book.socialnetwork.domain.transaction.entity.BookTransactionHistoryEntity;
import edu.book.socialnetwork.domain.transaction.repository.BookTransactionHistoryRepository;
import edu.book.socialnetwork.domain.user.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final BookMapper bookMapperService;
    private final FileStorageService fileStorageService;

    @Override
    public Integer save(BookRequest bookRequest, Authentication connectedUser) {
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        BookEntity book = bookMapperService.toBookEntity(bookRequest);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    @Override
    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapperService::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: " + bookId));
    }

    @Override
    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookEntity> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapperService::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    @Override
    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookEntity> books = bookRepository.findAll(BookSpecification.withOwnerID(user.getId()), pageable);

        List<BookResponse> bookResponses = books.stream()
                .map(bookMapperService::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    @Override
    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistoryEntity> allBBorrowedBooks = bookTransactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());
        List<BorrowedBookResponse> bookResponses = allBBorrowedBooks.stream()
                .map(bookMapperService::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                allBBorrowedBooks.getNumber(),
                allBBorrowedBooks.getSize(),
                allBBorrowedBooks.getTotalElements(),
                allBBorrowedBooks.getTotalPages(),
                allBBorrowedBooks.isFirst(),
                allBBorrowedBooks.isLast()
        );
    }

    @Override
    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistoryEntity> allBBorrowedBooks = bookTransactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());
        List<BorrowedBookResponse> bookResponses = allBBorrowedBooks.stream()
                .map(bookMapperService::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                allBBorrowedBooks.getNumber(),
                allBBorrowedBooks.getSize(),
                allBBorrowedBooks.getTotalElements(),
                allBBorrowedBooks.getTotalPages(),
                allBBorrowedBooks.isFirst(),
                allBBorrowedBooks.isLast()
        );
    }

    @Override
    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: " + bookId));
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot update this books shareable status because you are not the owner.");
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    @Override
    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: " + bookId));
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot update this books archived status because you are not the owner.");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    @Override
    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: " + bookId));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("This book is not available for borrowing since it is archived or not shareable.");
        }
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot borrow your own book.");
        }
        final boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());
        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("The requested book is already borrowed.");
        }
        BookTransactionHistoryEntity bookTransactionHistory = BookTransactionHistoryEntity.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    @Override
    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: " + bookId));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("This book is not available for borrowing since it is archived or not shareable.");
        }
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot return your own book.");
        }
        BookTransactionHistoryEntity bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book or the book with ID:: " + bookId));
        bookTransactionHistory.setReturned(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    @Override
    public Integer approveReturnBorrowedBook(Integer bookId, Authentication connectedUser) {
        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: " + bookId));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("This book is not available for borrowing since it is archived or not shareable.");
        }
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot return a book that do not belongs to you.");
        }
        BookTransactionHistoryEntity bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("The book is not approved yet. So you cannot approve the return."));
        bookTransactionHistory.setReturnApproved(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    @Override
    public void uploadBookCover(MultipartFile file, Authentication connectedUser, Integer bookId) {
        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: " + bookId));
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        String bookCover = fileStorageService.saveFile(file, user.getId());
        book.setBookCover(bookCover);
        bookRepository.save(book);
    }

}
