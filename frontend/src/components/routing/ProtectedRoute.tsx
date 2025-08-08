/**
 * GIRA - Protected Route Component
 * Handles authentication and role-based access control
 */

import type { ReactNode } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAppSelector } from '../../store';
import { UserRole } from '../../types';

interface ProtectedRouteProps {
  children: ReactNode;
  requiredRole?: UserRole;
}

/**
 * Protected Route Component
 * Redirects to login if not authenticated
 * Checks role permissions if requiredRole is specified
 */
const ProtectedRoute = ({ children, requiredRole }: ProtectedRouteProps) => {
  const auth = useAppSelector((state) => state.auth);
  const { isAuthenticated, user } = auth;
  const location = useLocation();

  // If not authenticated, redirect to login
  if (!isAuthenticated) {
    return <Navigate to="/auth/login" state={{ from: location }} replace />;
  }

  // If role is required, check user role
  if (requiredRole && user?.role.nom !== requiredRole) {
    // Redirect based on user role
    switch (user?.role.nom) {
      case UserRole.ADMINISTRATEUR:
        return <Navigate to="/app/admin" replace />;
      case UserRole.SUPERVISEUR:
      case UserRole.PASSAGER:
        return <Navigate to="/app/dashboard" replace />;
      default:
        return <Navigate to="/app/dashboard" replace />;
    }
  }

  return <>{children}</>;
};

export default ProtectedRoute; 