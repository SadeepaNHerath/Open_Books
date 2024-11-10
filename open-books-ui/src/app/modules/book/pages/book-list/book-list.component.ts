import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { BookService } from '../../../../services/services/book.service';
import { Router } from '@angular/router';
import { BookResponse, PageResponseBookResponse } from '../../../../services/models';
import { BookCardComponent } from '../../components/book-card/book-card.component';

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [CommonModule, BookCardComponent],
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.css'
})
export class BookListComponent implements OnInit {

  bookResponse: PageResponseBookResponse = {};
  page: number = 0;
  size: number = 5;
  pages: any = [];
  message = '';
  level: 'success' | 'error' = 'success';

  constructor(
    private bookService: BookService,
    private router: Router,
  ) {

  }
  ngOnInit(): void {
    this.findAllBooks();
  }

  private findAllBooks() {
    this.bookService.findAllBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (books) => {
        this.bookResponse = books;
      }
    })
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBooks();
  }

  goToPreviousPage() {
    this.page--;
    this.findAllBooks();
  }

  gotToPage(index: number) {
    this.page = index;
    this.findAllBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllBooks();
  }
  goToLastPage() {
    this.page = this.bookResponse.totalPages as number - 1;
    this.findAllBooks();
  }

  get isLastPage(): boolean {
    return this.page == this.bookResponse.totalPages as number - 1;
  }

  borrowBook(book: BookResponse) {
    this.message = '';
    this.level = 'success';
    this.bookService.borrowBook({
      'book-id': book.id as number
    }).subscribe({
      next: () => {
        this.level = 'success';
        this.message = 'Book successfully added to your list';
      },
      error: (err) => {
        console.log(err);
        this.level = 'error';
        this.message = err.error.error;
      }
    });
  }

  displayBookDetails(book: BookResponse) {
    this.router.navigate(['books', 'details', book.id]);
  }

}
