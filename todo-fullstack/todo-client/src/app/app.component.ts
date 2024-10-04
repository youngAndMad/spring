import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  constructor(private readonly http: HttpClient) {
    http.post('/api/v1/auth/register', { email: 'a', password: 'pass' }).subscribe((data) => console.log(data))
  }


  title = 'todo-client';
}
