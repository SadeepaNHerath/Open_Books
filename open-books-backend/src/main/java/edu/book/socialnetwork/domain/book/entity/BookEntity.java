package edu.book.socialnetwork.domain.book.entity;

import edu.book.socialnetwork.domain.common.entity.BaseEntity;
import edu.book.socialnetwork.domain.transaction.entity.BookTransactionHistoryEntity;
import edu.book.socialnetwork.domain.feedback.entity.FeedbackEntity;
import edu.book.socialnetwork.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BookEntity extends BaseEntity {

    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @OneToMany(mappedBy = "book")
    private List<FeedbackEntity> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistoryEntity> histories;

    @Transient
    public double getRate() {
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        double rate = this.feedbacks.stream()
                .mapToDouble(FeedbackEntity::getNote)
                .average()
                .orElse(0.0);
        return Math.round(rate * 10.0) / 10.0;
    }

}
