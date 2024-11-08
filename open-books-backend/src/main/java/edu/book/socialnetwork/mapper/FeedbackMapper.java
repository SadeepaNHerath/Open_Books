package edu.book.socialnetwork.mapper;

import edu.book.socialnetwork.dto.request.FeedbackRequest;
import edu.book.socialnetwork.dto.response.FeedbackResponse;
import edu.book.socialnetwork.entity.FeedbackEntity;

public interface FeedbackMapper {
    FeedbackEntity toFeedbackEntity(FeedbackRequest feedbackRequest);

    FeedbackResponse toFeedbackResponse(FeedbackEntity feedbackEntity, Integer id);
}
