export type Role = 'USER' | 'ADMIN';

export interface User {
    email: string;
    role: Role;
}

export interface AuthState {
    user: User | null;
    token: string | null;
    isAuthenticated: boolean;
}

export interface LoginCredentials {
    email: string;
    password: string;
}

export interface LoginResponse {
    token: string;
}
