/**
 * GIRA - Public Route Component
 * Redirects authenticated users away from public pages
 */

import type { ReactNode } from 'react';
import { Navigate } from 'react-router-dom';
import { useAppSelector } from '../../store';
import { UserRole } from '../../types';

interface PublicRouteProps {
  children: ReactNode;
}

/**
 * Public Route Component
 * Redirects authenticated users to their appropriate dashboard
 */
const PublicRoute = ({ children }: PublicRouteProps) => {
  const auth = useAppSelector((state) => state.auth);
  const { isAuthenticated, user } = auth;

  // If authenticated, redirect to appropriate dashboard
  if (isAuthenticated && user) {
    switch (user.role.nom) {
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

export default PublicRoute; 