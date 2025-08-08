/**
 * GIRA - Application Routes
 * Main routing configuration with protected routes
 */

import { Navigate, Route, Routes } from 'react-router-dom';
import { UserRole } from '../../types';
import ProtectedRoute from './ProtectedRoute';
import PublicRoute from './PublicRoute';

// Public pages
import LoginPage from '../../pages/auth/LoginPage';
import RegisterPage from '../../pages/auth/RegisterPage';
import LandingPage from '../../pages/public/LandingPage';

// Protected pages
import AdminPage from '../../pages/admin/AdminPage';
import ComplaintDetailPage from '../../pages/complaints/ComplaintDetailPage';
import ComplaintsPage from '../../pages/complaints/ComplaintsPage';
import NewComplaintPage from '../../pages/complaints/NewComplaintPage';
import DashboardPage from '../../pages/dashboard/DashboardPage';
import ProfilePage from '../../pages/profile/ProfilePage';

// Layout components
import AuthLayout from '../layout/AuthLayout';
import Layout from '../layout/Layout';

/**
 * Main Application Routes Component
 * Handles routing based on authentication and user roles
 */
const AppRoutes = () => {
  return (
    <Routes>
      {/* Public Routes */}
      <Route path="/" element={<PublicRoute><LandingPage /></PublicRoute>} />
      
      {/* Authentication Routes */}
      <Route path="/auth" element={<AuthLayout />}>
        <Route path="login" element={<PublicRoute><LoginPage /></PublicRoute>} />
        <Route path="register" element={<PublicRoute><RegisterPage /></PublicRoute>} />
      </Route>

      {/* Protected Routes */}
      <Route path="/app" element={<ProtectedRoute><Layout /></ProtectedRoute>}>
        {/* Dashboard Routes */}
        <Route index element={<Navigate to="dashboard" replace />} />
        <Route path="dashboard" element={<DashboardPage />} />
        
        {/* Complaints Routes */}
        <Route path="complaints" element={<ComplaintsPage />} />
        <Route path="complaints/new" element={<NewComplaintPage />} />
        <Route path="complaints/:id" element={<ComplaintDetailPage />} />
        
        {/* Profile Routes */}
        <Route path="profile" element={<ProfilePage />} />
        
        {/* Admin Routes - Only for ADMINISTRATEUR role */}
        <Route 
          path="admin" 
          element={
            <ProtectedRoute requiredRole={UserRole.ADMINISTRATEUR}>
              <AdminPage />
            </ProtectedRoute>
          } 
        />
      </Route>

      {/* Catch-all route */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
};

export default AppRoutes; 