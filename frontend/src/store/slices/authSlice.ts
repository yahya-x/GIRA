/**
 * GIRA - Authentication Redux Slice
 * Manages user authentication state and actions
 */

import type { PayloadAction } from '@reduxjs/toolkit';
import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import api from '../../services/apiClient';
import type { AuthState, LoginForm, RegisterForm, User } from '../../types';

/**
 * Async thunk for user login
 * Handles authentication with the backend API
 */
export const loginUser = createAsyncThunk(
  'auth/login',
  async (credentials: LoginForm, { rejectWithValue }) => {
    try {
      const response = await api.post('/api/v1/auth/login', credentials);
      // Backend wraps payload in ApiResponse<AuthResponse>
      const apiResponse = response.data;
      if (!apiResponse?.success) {
        return rejectWithValue(apiResponse?.message || 'Login failed');
      }
      return apiResponse.data; // AuthResponse { accessToken, refreshToken, tokenType, expiresIn, user }
    } catch (error: any) {
      const message = error?.response?.data?.message || 'Network error occurred';
      return rejectWithValue(message);
    }
  }
);

/**
 * Async thunk for user registration
 * Handles new user registration with the backend API
 */
export const registerUser = createAsyncThunk(
  'auth/register',
  async (userData: RegisterForm, { rejectWithValue }) => {
    try {
      const response = await api.post('/api/v1/auth/register', userData);
      const apiResponse = response.data;
      if (!apiResponse?.success) {
        return rejectWithValue(apiResponse?.message || 'Registration failed');
      }
      return apiResponse.data; // UserResponse
    } catch (error: any) {
      const message = error?.response?.data?.message || 'Network error occurred';
      return rejectWithValue(message);
    }
  }
);

/**
 * Async thunk for fetching current user profile
 * Retrieves user information from the backend
 */
export const fetchCurrentUser = createAsyncThunk(
  'auth/fetchCurrentUser',
  async (_, { rejectWithValue }) => {
    try {
      const response = await api.get('/api/v1/auth/me');
      const apiResponse = response.data;
      if (!apiResponse?.success) {
        return rejectWithValue(apiResponse?.message || 'Failed to fetch user profile');
      }
      return apiResponse.data as User; // UserResponse
    } catch (error: any) {
      const message = error?.response?.data?.message || 'Network error occurred';
      return rejectWithValue(message);
    }
  }
);

/**
 * Initial state for authentication slice
 */
const initialState: AuthState = {
  user: null,
  token: localStorage.getItem('token'),
  isAuthenticated: !!localStorage.getItem('token'),
  isLoading: false,
  error: null,
};

/**
 * Authentication Redux slice
 * Manages authentication state and actions
 */
const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    /**
     * Clear authentication error
     */
    clearError: (state) => {
      state.error = null;
    },

    /**
     * Set loading state
     */
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoading = action.payload;
    },

    /**
     * Update user profile
     */
    updateUser: (state, action: PayloadAction<Partial<User>>) => {
      if (state.user) {
        state.user = { ...state.user, ...action.payload } as User;
      }
    },

    /**
     * Logout user and clear state
     */
    logout: (state) => {
      state.user = null;
      state.token = null;
      state.isAuthenticated = false;
      state.error = null;
      localStorage.removeItem('token');
    },

    /**
     * Set authentication token
     */
    setToken: (state, action: PayloadAction<string>) => {
      state.token = action.payload;
      state.isAuthenticated = true;
      localStorage.setItem('token', action.payload);
    },
  },
  extraReducers: (builder) => {
    // Login cases
    builder
      .addCase(loginUser.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(loginUser.fulfilled, (state, action) => {
        state.isLoading = false;
        state.user = action.payload.user as User;
        const accessToken = action.payload.accessToken as string;
        state.token = accessToken;
        state.isAuthenticated = true;
        state.error = null;
        localStorage.setItem('token', accessToken);
      })
      .addCase(loginUser.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
        state.isAuthenticated = false;
      });

    // Register cases
    builder
      .addCase(registerUser.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(registerUser.fulfilled, (state) => {
        state.isLoading = false;
        state.error = null;
      })
      .addCase(registerUser.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
        state.isAuthenticated = false;
      });

    // Fetch current user cases
    builder
      .addCase(fetchCurrentUser.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchCurrentUser.fulfilled, (state, action) => {
        state.isLoading = false;
        state.user = action.payload as User;
        state.isAuthenticated = true;
        state.error = null;
      })
      .addCase(fetchCurrentUser.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
        state.isAuthenticated = false;
        localStorage.removeItem('token');
      });
  },
});

export const { clearError, setLoading, updateUser, logout, setToken } = authSlice.actions;
export default authSlice.reducer; 