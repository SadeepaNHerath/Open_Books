package edu.book.socialnetwork.service;

import edu.book.socialnetwork.dto.request.FeedbackRequest;
import edu.book.socialnetwork.dto.response.FeedbackResponse;
import edu.book.socialnetwork.dto.response.PageResponse;
import org.springframework.security.core.Authentication;

public interface FeedbackService {
    Integer save(FeedbackRequest feedbackRequest, Authentication connectedUser);

    PageResponse<FeedbackResponse> findAllFeedbackByBook(Integer bookId, int page, int size, Authentication connectedUser);
}