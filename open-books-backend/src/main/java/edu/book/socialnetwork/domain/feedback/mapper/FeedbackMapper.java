package edu.book.socialnetwork.domain.feedback.mapper;

import edu.book.socialnetwork.domain.feedback.dto.request.FeedbackRequest;
import edu.book.socialnetwork.domain.feedback.dto.response.FeedbackResponse;
import edu.book.socialnetwork.domain.feedback.entity.FeedbackEntity;

public interface FeedbackMapper {
    FeedbackEntity toFeedbackEntity(FeedbackRequest feedbackRequest);

    FeedbackResponse toFeedbackResponse(FeedbackEntity feedbackEntity, Integer id);
}
