import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

@Component({
  selector: 'app-role-badge',
  templateUrl: './role-badge.component.html',
  styleUrls: ['./role-badge.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RoleBadgeComponent {
  @Input() role = '';
}
