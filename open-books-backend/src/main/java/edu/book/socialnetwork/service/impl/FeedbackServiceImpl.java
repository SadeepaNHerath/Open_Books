package edu.book.socialnetwork.service.impl;

import edu.book.socialnetwork.dto.request.FeedbackRequest;
import edu.book.socialnetwork.dto.response.FeedbackResponse;
import edu.book.socialnetwork.dto.response.PageResponse;
import edu.book.socialnetwork.entity.BookEntity;
import edu.book.socialnetwork.entity.FeedbackEntity;
import edu.book.socialnetwork.entity.UserEntity;
import edu.book.socialnetwork.exception.OperationNotPermittedException;
import edu.book.socialnetwork.mapper.FeedbackMapper;
import edu.book.socialnetwork.repository.BookRepository;
import edu.book.socialnetwork.repository.FeedbackRepository;
import edu.book.socialnetwork.service.FeedbackService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;

    @Override
    public Integer save(FeedbackRequest feedbackRequest, Authentication connectedUser) {
        BookEntity book = bookRepository.findById(feedbackRequest.bookId())
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: " + feedbackRequest.bookId()));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("You cannot give a feedback to an archived or not shareable book.");
        }
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot give a feedback to your own book.");
        }
        FeedbackEntity feedback = feedbackMapper.toFeedbackEntity(feedbackRequest);
        return feedbackRepository.save(feedback).getId();
    }

    @Override
    public PageResponse<FeedbackResponse> findAllFeedbackByBook(Integer bookId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        Page<FeedbackEntity> feedbacks = feedbackRepository.findAllByBookId(bookId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(feedbackEntity -> feedbackMapper.toFeedbackResponse(feedbackEntity, user.getId()))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
