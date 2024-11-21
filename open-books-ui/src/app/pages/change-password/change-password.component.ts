import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.css'
})
export class ChangePasswordComponent {

  passwordRequest = {
    currentPassword: '',
    newPassword: '',
    confirmationPassword: ''
  };
  errorMsg: string[] = [];

  changePassword() {
    this.errorMsg = [];
    
    if (!this.passwordRequest.currentPassword) {
      this.errorMsg.push('Current password is required');
      return;
    }

    if (!this.passwordRequest.newPassword) {
      this.errorMsg.push('New password is required');
      return;
    }

    if (this.passwordRequest.newPassword !== this.passwordRequest.confirmationPassword) {
      this.errorMsg.push('Passwords do not match');
      return;
    }
  }
}
