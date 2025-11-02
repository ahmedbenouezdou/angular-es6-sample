import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthConfig, OAuthService } from 'angular-oauth2-oidc';
import { BehaviorSubject, Observable, catchError, of, tap } from 'rxjs';

import { environment } from '../../../environments/environment';

export interface UserProfile {
  sub: string;
  email: string;
  name: string;
  picture?: string;
  roles?: string[];
}

export interface AuthenticatedUserResponse {
  user: UserProfile;
  roles: string[];
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly authConfig: AuthConfig = {
    issuer: 'https://accounts.google.com',
    strictDiscoveryDocumentValidation: false,
    clientId: environment.googleClientId,
    redirectUri: window.location.origin,
    scope: 'openid profile email',
    responseType: 'code'
  };

  private readonly userSubject = new BehaviorSubject<UserProfile | null>(null);
  private readonly rolesSubject = new BehaviorSubject<string[]>([]);
  private readonly tokenSubject = new BehaviorSubject<string | null>(null);

  readonly user$: Observable<UserProfile | null> = this.userSubject.asObservable();
  readonly roles$: Observable<string[]> = this.rolesSubject.asObservable();
  readonly token$: Observable<string | null> = this.tokenSubject.asObservable();

  constructor(
    private readonly oauthService: OAuthService,
    private readonly http: HttpClient
  ) {
    this.oauthService.configure(this.authConfig);
    this.oauthService.setupAutomaticSilentRefresh();
    this.oauthService.events.subscribe(() => this.loadUserProfile());
    void this.oauthService.loadDiscoveryDocumentAndTryLogin().then(() => {
      if (this.oauthService.hasValidIdToken()) {
        this.loadUserProfile();
      }
    });
  }

  signInWithGoogle(): void {
    void this.oauthService.initCodeFlow();
  }

  signOut(): void {
    this.userSubject.next(null);
    this.rolesSubject.next([]);
    this.tokenSubject.next(null);
    void this.oauthService.logOut();
  }

  private loadUserProfile(): void {
    const claims = this.oauthService.getIdentityClaims() as UserProfile | undefined;
    if (!claims) {
      return;
    }

    const idToken = this.oauthService.getIdToken();
    this.http
      .post<AuthenticatedUserResponse>(`${environment.apiUrl}/auth/google`, { token: idToken })
      .pipe(
        tap((response) => {
          this.userSubject.next(response.user);
          this.rolesSubject.next(response.roles);
          this.tokenSubject.next(response.token);
        }),
        catchError((error) => {
          console.error('Unable to validate Google token', error);
          this.signOut();
          return of(null);
        })
      )
      .subscribe();
  }

  get accessToken(): string | null {
    return this.tokenSubject.value;
  }
}
