import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BookRequest } from '../../../../services/models/book-request';
import { BookService } from '../../../../services/services/book.service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-manage-books',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './manage-books.component.html',
  styleUrl: './manage-books.component.css'
})
export class ManageBooksComponent implements OnInit {
  errorMsg: Array<string> = [];
  bookRequest: BookRequest = {
    authorName: '',
    isbn: '',
    synopsis: '',
    title: ''
  };
  selectedBookCover: any;
  selectedPicture: string | undefined;

  constructor(
    private bookService: BookService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    const bookId = this.activatedRoute.snapshot.params['bookId'];
    if (bookId) {
      this.bookService.findBookById({
        'book-id': bookId
      }).subscribe({
        next: (book) => {
          this.bookRequest = {
            id: book.id,
            title: book.title as string,
            authorName: book.authorName as string,
            isbn: book.isbn as string,
            synopsis: book.synopsis as string,
            shareable: book.shareable
          };
          this.selectedPicture = 'data:image/jpg;base64,' + book.cover;
        }
      });
    }
  }

  saveBook() {
    if (!this.bookRequest.title || !this.bookRequest.authorName) {
      console.error('Book title and author name are required');
      return;
    }

    this.bookService.saveBook({
      body: this.bookRequest
    }).subscribe({
      next: (bookId) => {
        if (!this.selectedBookCover) {
          console.error('No book cover selected');
          return;
        }

        this.bookService.uploadBookCover({
          'book-id': bookId,
          body: {
            file: this.selectedBookCover
          }
        }).subscribe({
          next: () => {
            this.router.navigate(['/books/my-books']);
          },
          error: (uploadError) => {
            console.error('Error uploading book cover:', uploadError);
          }
        });
      },
      error: (err) => {
        console.error('Error saving book:', err);
        this.errorMsg = err.error?.validationErrors || ['An unknown error occurred'];
      }
    });
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
        this.selectedBookCover = input.files[0];
        console.log(this.selectedBookCover);

        if (this.selectedBookCover) {
            const reader = new FileReader();
            reader.onload = () => {
                this.selectedPicture = reader.result as string;
            };
            reader.onerror = (error) => {
                console.error('Error reading file:', error);
            };
            reader.readAsDataURL(this.selectedBookCover);
        }
    } else {
        console.error('No file selected');
    }
}
}