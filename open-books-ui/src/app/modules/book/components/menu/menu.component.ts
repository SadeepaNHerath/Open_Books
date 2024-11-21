import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthenticationService } from '../../../../services/services';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit {

  username: String | null =null;

  ngOnInit(): void {
    const linkColor: NodeListOf<Element> = document.querySelectorAll('.nav-link');
    linkColor.forEach(link => {
      if (window.location.href.endsWith(link.getAttribute('href') || '')) {
        link.classList.add('active');
      }
      link.addEventListener('click', () => {
        linkColor.forEach(l => l.classList.remove('active'));
        link.classList.add('active');
      });
    });

    this.username = localStorage.getItem('user');
  }


  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.reload();
  }

}
