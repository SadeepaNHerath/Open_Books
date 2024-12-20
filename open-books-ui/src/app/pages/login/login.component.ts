import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthenticationRequest } from '../../services/models/authentication-request';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/services/authentication.service';
import { TokenService } from '../../services/token/token.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  authRequest: AuthenticationRequest = { email: '', password: '' };
  errorMsg: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService,
  ) { }

  login() {
    this.errorMsg = [];
    this.authService.authenticate({
        body: this.authRequest
    }).subscribe({
        next: (res) => {
            this.tokenService.token = res.token as string;
            localStorage.setItem('user', res.name as string);
            this.router.navigate(['books']);
        },
        error: (err) => {
            console.log(err);
            if (err.error.validationErrors) {
                this.errorMsg = err.error.validationErrors;
            } else {
                this.errorMsg.push(err.error.errorMsg);
            }
        }
    });
  }

  register() {
    this.router.navigate(['register']);
  }

  forgotPassword() {
    this.router.navigate(['change-password']);
  }

}
