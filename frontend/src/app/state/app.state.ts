import { UserProfile } from '../core/auth/auth.service';

export interface AppState {
  auth: {
    user: UserProfile | null;
    roles: string[];
    token: string | null;
  };
}
