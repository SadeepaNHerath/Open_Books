import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  set token(token: string) {
    localStorage.setItem('token', token);
  }

  set name(name: string) {
    localStorage.setItem('user', name);
  }

  get token() {
    return localStorage.getItem('token') as string;
  }

  get name() {
    return localStorage.getItem('user') as string;
  }

  isTokenValid() {
    const token = this.token;
    if (!token) {
      return false;
    }
    const jwtHelper = new JwtHelperService();
    const isTokenExpired = jwtHelper.isTokenExpired(token);
    if (isTokenExpired) {
      localStorage.clear();
      return false;
    }
    return true;
  }

  isTokenNotValid() {
    return !this.isTokenValid();
  }
}
