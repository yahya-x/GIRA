import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from '../../store';
import { clearError, loginUser } from '../../store/slices/authSlice';
import type { LoginForm } from '../../types';

const LoginPage = () => {
  const { register, handleSubmit } = useForm<LoginForm>();
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { isAuthenticated, user, isLoading, error } = useAppSelector((s) => s.auth);

  useEffect(() => {
    if (isAuthenticated && user) {
      // Redirect by role
      const role = user.role?.nom;
      if (role === 'ADMINISTRATEUR') navigate('/app/admin');
      else navigate('/app/dashboard');
    }
  }, [isAuthenticated, user, navigate]);

  const onSubmit = (data: LoginForm) => {
    dispatch(clearError());
    dispatch(loginUser(data));
  };

  return (
    <div className="bg-white shadow rounded p-6">
      <h2 className="text-xl font-semibold mb-4">Login</h2>
      {error && <div className="text-red-600 mb-2">{error}</div>}
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-3">
        <div>
          <label className="block text-sm text-gray-700">Email</label>
          <input type="email" className="mt-1 w-full border rounded px-3 py-2" {...register('email', { required: true })} />
        </div>
        <div>
          <label className="block text-sm text-gray-700">Password</label>
          <input type="password" className="mt-1 w-full border rounded px-3 py-2" {...register('password', { required: true })} />
        </div>
        <button
          type="submit"
          disabled={isLoading}
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 disabled:opacity-50"
        >
          {isLoading ? 'Signing in...' : 'Sign In'}
        </button>
      </form>
    </div>
  );
};

export default LoginPage; 