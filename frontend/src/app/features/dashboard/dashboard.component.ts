import { ChangeDetectionStrategy, Component } from '@angular/core';
import { Observable } from 'rxjs';

import { AuthService, UserProfile } from '../../core/auth/auth.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DashboardComponent {
  readonly user$: Observable<UserProfile | null> = this.authService.user$;
  readonly roles$: Observable<string[]> = this.authService.roles$;

  constructor(private readonly authService: AuthService) {}
}
