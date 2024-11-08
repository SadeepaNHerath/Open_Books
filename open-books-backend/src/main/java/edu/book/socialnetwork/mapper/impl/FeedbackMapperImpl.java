package edu.book.socialnetwork.mapper.impl;

import edu.book.socialnetwork.dto.request.FeedbackRequest;
import edu.book.socialnetwork.dto.response.FeedbackResponse;
import edu.book.socialnetwork.entity.BookEntity;
import edu.book.socialnetwork.entity.FeedbackEntity;
import edu.book.socialnetwork.mapper.FeedbackMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapperImpl implements FeedbackMapper {
    @Override
    public FeedbackEntity toFeedbackEntity(FeedbackRequest feedbackRequest) {
        return FeedbackEntity.builder()
                .note(feedbackRequest.note())
                .comment(feedbackRequest.comment())
                .book(BookEntity.builder()
                        .id(feedbackRequest.bookId())
                        .archived(false)
                        .shareable(false)
                        .build()
                )
                .build();
    }

    @Override
    public FeedbackResponse toFeedbackResponse(FeedbackEntity feedbackEntity, Integer id) {
        return FeedbackResponse.builder()
                .note(feedbackEntity.getNote())
                .comment(feedbackEntity.getComment())
                .ownFeedback(Objects.equals(feedbackEntity.getCreatedBy(), id))
                .build();
    }

}
