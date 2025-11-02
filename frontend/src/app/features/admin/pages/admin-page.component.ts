import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminPageComponent {}
