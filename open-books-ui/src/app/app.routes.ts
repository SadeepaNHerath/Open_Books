import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { ActivateAccountComponent } from './pages/activate-account/activate-account.component';
import { authGuard } from './services/guard/auth.guard';
import { ChangePasswordComponent } from './pages/change-password/change-password.component';

export const routes: Routes = [
    {   path: '',
        redirectTo: '/login',
        pathMatch: 'full' 
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'change-password',
        component: ChangePasswordComponent
    },
    {
        path: 'register',
        component: RegisterComponent
    },
    {
        path: 'activate-account',
        component: ActivateAccountComponent
    },
    {
        path: 'books',
        loadChildren: () => import('./modules/book/book.module').then(m => m.BookModule),
        canActivate: [authGuard]
    }
];