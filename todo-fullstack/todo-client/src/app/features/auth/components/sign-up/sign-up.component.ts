import { Component } from '@angular/core';
import {
  FormGroup,
  FormBuilder,
  Validators,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { PasswordModule } from 'primeng/password';

@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [FormsModule, PasswordModule, ReactiveFormsModule],
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.scss',
})
export class SignUpComponent {
  signUpForm: FormGroup;

  constructor(
    private readonly authService: AuthService,
    private readonly fb: FormBuilder
  ) {
    this.signUpForm = this.fb.group({
      email: [null, [Validators.email, Validators.required]],
      password: [null, [Validators.required]],
    });
  }

  handleLoginAttempt() {
    console.log('login attempt');
  }
}
