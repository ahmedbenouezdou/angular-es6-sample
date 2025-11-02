import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { RoleBadgeComponent } from './components/role-badge/role-badge.component';

@NgModule({
  declarations: [RoleBadgeComponent],
  imports: [CommonModule],
  exports: [CommonModule, RoleBadgeComponent]
})
export class SharedModule {}
