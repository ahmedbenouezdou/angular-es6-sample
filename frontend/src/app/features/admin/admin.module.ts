import { NgModule } from '@angular/core';

import { SharedModule } from '../../shared/shared.module';
import { AdminRoutingModule } from './admin-routing.module';
import { AdminPageComponent } from './pages/admin-page.component';

@NgModule({
  declarations: [AdminPageComponent],
  imports: [SharedModule, AdminRoutingModule]
})
export class AdminModule {}
