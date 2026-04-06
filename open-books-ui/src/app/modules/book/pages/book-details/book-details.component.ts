import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { BookService } from '../../../../services/services/book.service';
import { FeedbackService } from '../../../../services/services/feedback.service';
import { BookResponse, PageResponseFeedbackResponse } from '../../../../services/models';
import { RatingComponent } from '../../components/rating/rating.component';

@Component({
  selector: 'app-book-details',
  standalone: true,
  imports: [CommonModule, RouterLink, RatingComponent],
  templateUrl: './book-details.component.html',
  styleUrl: './book-details.component.css'
})
export class BookDetailsComponent implements OnInit {
  book: BookResponse | undefined;
  feedbacks: PageResponseFeedbackResponse = {};
  page = 0;
  size = 5;
  pages: number[] = [];
  loading = true;

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly bookService: BookService,
    private readonly feedbackService: FeedbackService
  ) {
  }

  ngOnInit(): void {
    const bookId = Number(this.activatedRoute.snapshot.params['bookId']);
    this.loadBook(bookId);
    this.loadFeedbacks(bookId);
  }

  private loadBook(bookId: number) {
    this.bookService.findBookById({
      'book-id': bookId
    }).subscribe({
      next: (resp) => {
        this.book = resp;
      }
    });
  }

  private loadFeedbacks(bookId: number) {
    this.feedbackService.findAllFeedbackByBook({
      'book-id': bookId,
      page: this.page,
      size: this.size
    }).subscribe({
      next: (resp) => {
        this.feedbacks = resp;
        this.pages = Array(this.feedbacks.totalPages || 0)
          .fill(0)
          .map((_, index) => index);
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  goToPage(page: number) {
    this.page = page;
    const bookId = Number(this.activatedRoute.snapshot.params['bookId']);
    this.loadFeedbacks(bookId);
  }

  get bookCover(): string {
    return this.book?.cover ? `data:image/jpeg;base64,${this.book.cover}` : 'https://fastly.picsum.photos/id/84/200/300.jpg?grayscale';
  }

  get averageRating(): number {
    return this.book?.rate || 0;
  }
}