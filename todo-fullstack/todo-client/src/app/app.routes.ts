import { Routes } from '@angular/router';
import { NotFoundComponent } from './layout/not-found/not-found.component';

export const routes: Routes = [
  {
    path: 'sign-up',
    loadComponent: () =>
      import('./features/auth/components/sign-up/sign-up.component').then(
        (c) => c.SignUpComponent
      ),
  },
  //   {
  //     path: '**',
  //     component: NotFoundComponent,
  //   },
];
