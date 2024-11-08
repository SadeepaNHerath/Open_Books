package edu.book.socialnetwork.domain.feedback.service;

import edu.book.socialnetwork.domain.feedback.dto.request.FeedbackRequest;
import edu.book.socialnetwork.domain.feedback.dto.response.FeedbackResponse;
import edu.book.socialnetwork.domain.common.dto.PageResponse;
import org.springframework.security.core.Authentication;

public interface FeedbackService {
    Integer save(FeedbackRequest feedbackRequest, Authentication connectedUser);

    PageResponse<FeedbackResponse> findAllFeedbackByBook(Integer bookId, int page, int size, Authentication connectedUser);
}