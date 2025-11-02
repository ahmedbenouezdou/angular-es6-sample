import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';
import { Observable, combineLatest, map } from 'rxjs';

import { AuthService } from '../auth/auth.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private readonly authService: AuthService, private readonly router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> {
    const requiredRoles = (route.data['roles'] ?? []) as string[];
    if (!requiredRoles.length) {
      return combineLatest([this.authService.user$, this.authService.roles$]).pipe(
        map(([user]) => Boolean(user))
      );
    }

    return combineLatest([this.authService.user$, this.authService.roles$]).pipe(
      map(([user, roles]) => {
        if (!user) {
          return this.router.createUrlTree(['/'], {
            queryParams: { redirectTo: state.url }
          });
        }
        const hasRole = requiredRoles.every((role) => roles.includes(role));
        if (hasRole) {
          return true;
        }
        return this.router.createUrlTree(['/'], {
          queryParams: { unauthorized: true }
        });
      })
    );
  }
}
