import { createSelector } from '@ngrx/store';

import { AppState } from './app.state';

const selectAuthState = (state: AppState) => state.auth;

export const selectCurrentUser = createSelector(selectAuthState, (auth) => auth.user);
export const selectRoles = createSelector(selectAuthState, (auth) => auth.roles);
export const selectToken = createSelector(selectAuthState, (auth) => auth.token);
