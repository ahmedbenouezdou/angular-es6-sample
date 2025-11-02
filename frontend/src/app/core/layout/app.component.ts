import { Component } from '@angular/core';
import { Observable } from 'rxjs';

import { AuthService, UserProfile } from '../auth/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  readonly user$: Observable<UserProfile | null> = this.authService.user$;
  readonly roles$: Observable<string[]> = this.authService.roles$;

  constructor(private readonly authService: AuthService) {}

  login(): void {
    this.authService.signInWithGoogle();
  }

  logout(): void {
    this.authService.signOut();
  }
}
