/**
 * GIRA - Dashboard Redux Slice
 * Manages dashboard analytics and metrics
 */

import type { PayloadAction } from '@reduxjs/toolkit';
import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import type { DashboardAnalytics, DashboardFilters, DashboardState } from '../../types';

/**
 * Async thunk for fetching dashboard analytics
 */
export const fetchDashboardAnalytics = createAsyncThunk(
  'dashboard/fetchAnalytics',
  async ({ filters }: { filters?: DashboardFilters }, { rejectWithValue }) => {
    try {
      const queryParams = new URLSearchParams();
      if (filters) {
        if (filters.periode) queryParams.append('periode', filters.periode);
        if (filters.dateDebut) queryParams.append('dateDebut', filters.dateDebut);
        if (filters.dateFin) queryParams.append('dateFin', filters.dateFin);
        if (filters.agent) queryParams.append('agent', filters.agent);
        if (filters.categorie) queryParams.append('categorie', filters.categorie);
      }

      const response = await fetch(`/api/dashboard/analytics?${queryParams}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        return rejectWithValue('Failed to fetch dashboard analytics');
      }

      const data = await response.json();
      return data;
    } catch (error) {
      return rejectWithValue('Network error occurred');
    }
  }
);

/**
 * Async thunk for fetching admin dashboard data
 */
export const fetchAdminDashboard = createAsyncThunk(
  'dashboard/fetchAdminDashboard',
  async ({ filters }: { filters?: DashboardFilters }, { rejectWithValue }) => {
    try {
      const queryParams = new URLSearchParams();
      if (filters) {
        if (filters.periode) queryParams.append('periode', filters.periode);
        if (filters.dateDebut) queryParams.append('dateDebut', filters.dateDebut);
        if (filters.dateFin) queryParams.append('dateFin', filters.dateFin);
      }

      const response = await fetch(`/api/dashboard/admin?${queryParams}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        return rejectWithValue('Failed to fetch admin dashboard');
      }

      const data = await response.json();
      return data;
    } catch (error) {
      return rejectWithValue('Network error occurred');
    }
  }
);

/**
 * Async thunk for fetching agent dashboard data
 */
export const fetchAgentDashboard = createAsyncThunk(
  'dashboard/fetchAgentDashboard',
  async ({ filters }: { filters?: DashboardFilters }, { rejectWithValue }) => {
    try {
      const queryParams = new URLSearchParams();
      if (filters) {
        if (filters.periode) queryParams.append('periode', filters.periode);
        if (filters.dateDebut) queryParams.append('dateDebut', filters.dateDebut);
        if (filters.dateFin) queryParams.append('dateFin', filters.dateFin);
      }

      const response = await fetch(`/api/dashboard/agent?${queryParams}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        return rejectWithValue('Failed to fetch agent dashboard');
      }

      const data = await response.json();
      return data;
    } catch (error) {
      return rejectWithValue('Network error occurred');
    }
  }
);

/**
 * Async thunk for fetching passenger dashboard data
 */
export const fetchPassengerDashboard = createAsyncThunk(
  'dashboard/fetchPassengerDashboard',
  async ({ filters }: { filters?: DashboardFilters }, { rejectWithValue }) => {
    try {
      const queryParams = new URLSearchParams();
      if (filters) {
        if (filters.dateDebut) queryParams.append('dateDebut', filters.dateDebut);
        if (filters.dateFin) queryParams.append('dateFin', filters.dateFin);
      }

      const response = await fetch(`/api/dashboard/passenger?${queryParams}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        return rejectWithValue('Failed to fetch passenger dashboard');
      }

      const data = await response.json();
      return data;
    } catch (error) {
      return rejectWithValue('Network error occurred');
    }
  }
);

/**
 * Initial state for dashboard slice
 */
const initialState: DashboardState = {
  analytics: null,
  isLoading: false,
  error: null,
  filters: {
    periode: 'jour',
  },
};

/**
 * Dashboard Redux slice
 * Manages dashboard analytics and metrics
 */
const dashboardSlice = createSlice({
  name: 'dashboard',
  initialState,
  reducers: {
    /**
     * Clear dashboard error
     */
    clearError: (state) => {
      state.error = null;
    },

    /**
     * Update dashboard filters
     */
    updateFilters: (state, action: PayloadAction<DashboardFilters>) => {
      state.filters = { ...state.filters, ...action.payload };
    },

    /**
     * Clear dashboard filters
     */
    clearFilters: (state) => {
      state.filters = { periode: 'jour' };
    },

    /**
     * Set dashboard analytics
     */
    setAnalytics: (state, action: PayloadAction<DashboardAnalytics>) => {
      state.analytics = action.payload;
    },

    /**
     * Update specific analytics metric
     */
    updateAnalyticsMetric: (state, action: PayloadAction<Partial<DashboardAnalytics>>) => {
      if (state.analytics) {
        state.analytics = { ...state.analytics, ...action.payload };
      }
    },

    /**
     * Reset dashboard state
     */
    resetDashboard: (state) => {
      state.analytics = null;
      state.error = null;
      state.filters = { periode: 'jour' };
    },
  },
  extraReducers: (builder) => {
    // Fetch analytics cases
    builder
      .addCase(fetchDashboardAnalytics.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchDashboardAnalytics.fulfilled, (state, action) => {
        state.isLoading = false;
        state.analytics = action.payload;
        state.error = null;
      })
      .addCase(fetchDashboardAnalytics.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });

    // Fetch admin dashboard cases
    builder
      .addCase(fetchAdminDashboard.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchAdminDashboard.fulfilled, (state, action) => {
        state.isLoading = false;
        state.analytics = action.payload;
        state.error = null;
      })
      .addCase(fetchAdminDashboard.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });

    // Fetch agent dashboard cases
    builder
      .addCase(fetchAgentDashboard.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchAgentDashboard.fulfilled, (state, action) => {
        state.isLoading = false;
        state.analytics = action.payload;
        state.error = null;
      })
      .addCase(fetchAgentDashboard.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });

    // Fetch passenger dashboard cases
    builder
      .addCase(fetchPassengerDashboard.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchPassengerDashboard.fulfilled, (state, action) => {
        state.isLoading = false;
        state.analytics = action.payload;
        state.error = null;
      })
      .addCase(fetchPassengerDashboard.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });
  },
});

export const {
  clearError,
  updateFilters,
  clearFilters,
  setAnalytics,
  updateAnalyticsMetric,
  resetDashboard,
} = dashboardSlice.actions;

export default dashboardSlice.reducer; 