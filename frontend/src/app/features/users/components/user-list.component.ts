import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';

import { UserProfile } from '../../../core/auth/auth.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserListComponent implements OnInit {
  users$!: Observable<UserProfile[]>;

  constructor(private readonly userService: UserService) {}

  ngOnInit(): void {
    this.users$ = this.userService.findAll();
  }
}
