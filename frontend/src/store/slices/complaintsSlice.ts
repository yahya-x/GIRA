/**
 * GIRA - Complaints Redux Slice
 * Manages complaint state and operations
 */

import type { PayloadAction } from '@reduxjs/toolkit';
import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import type {
    CommentForm,
    Complaint,
    ComplaintFilters,
    ComplaintForm,
    ComplaintState,
    PaginationState
} from '../../types';

/**
 * Async thunk for fetching complaints with pagination and filters
 */
export const fetchComplaints = createAsyncThunk(
  'complaints/fetchComplaints',
  async (params: { page?: number; size?: number; filters?: ComplaintFilters }, { rejectWithValue }) => {
    try {
      // TODO: Replace with actual API call
      const queryParams = new URLSearchParams();
      if (params.page) queryParams.append('page', params.page.toString());
      if (params.size) queryParams.append('size', params.size.toString());
      
      const response = await fetch(`/api/complaints?${queryParams}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        return rejectWithValue('Failed to fetch complaints');
      }

      const data = await response.json();
      return data;
    } catch (error) {
      return rejectWithValue('Network error occurred');
    }
  }
);

/**
 * Async thunk for creating a new complaint
 */
export const createComplaint = createAsyncThunk(
  'complaints/createComplaint',
  async (complaintData: ComplaintForm, { rejectWithValue }) => {
    try {
      const formData = new FormData();
      formData.append('titre', complaintData.titre);
      formData.append('description', complaintData.description);
      formData.append('categorieId', complaintData.categorieId);
      if (complaintData.sousCategorieId) {
        formData.append('sousCategorieId', complaintData.sousCategorieId);
      }
      formData.append('priorite', complaintData.priorite);
      
      if (complaintData.fichiers) {
        complaintData.fichiers.forEach((file) => {
          formData.append('fichiers', file);
        });
      }

      const response = await fetch('/api/complaints', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: formData,
      });

      if (!response.ok) {
        const error = await response.json();
        return rejectWithValue(error.message || 'Failed to create complaint');
      }

      const data = await response.json();
      return data;
    } catch (error) {
      return rejectWithValue('Network error occurred');
    }
  }
);

/**
 * Async thunk for updating complaint status
 */
export const updateComplaintStatus = createAsyncThunk(
  'complaints/updateStatus',
  async ({ complaintId, status }: { complaintId: string; status: string }, { rejectWithValue }) => {
    try {
      const response = await fetch(`/api/complaints/${complaintId}/status`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify({ status }),
      });

      if (!response.ok) {
        return rejectWithValue('Failed to update complaint status');
      }

      const data = await response.json();
      return data;
    } catch (error) {
      return rejectWithValue('Network error occurred');
    }
  }
);

/**
 * Async thunk for adding a comment to a complaint
 */
export const addComment = createAsyncThunk(
  'complaints/addComment',
  async ({ complaintId, comment }: { complaintId: string; comment: CommentForm }, { rejectWithValue }) => {
    try {
      const response = await fetch(`/api/complaints/${complaintId}/comments`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify(comment),
      });

      if (!response.ok) {
        return rejectWithValue('Failed to add comment');
      }

      const data = await response.json();
      return { complaintId, comment: data };
    } catch (error) {
      return rejectWithValue('Network error occurred');
    }
  }
);

/**
 * Async thunk for fetching a single complaint by ID
 */
export const fetchComplaintById = createAsyncThunk(
  'complaints/fetchComplaintById',
  async (complaintId: string, { rejectWithValue }) => {
    try {
      const response = await fetch(`/api/complaints/${complaintId}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        return rejectWithValue('Failed to fetch complaint');
      }

      const data = await response.json();
      return data;
    } catch (error) {
      return rejectWithValue('Network error occurred');
    }
  }
);

/**
 * Initial state for complaints slice
 */
const initialState: ComplaintState = {
  items: [],
  currentComplaint: null,
  isLoading: false,
  error: null,
  filters: {},
  pagination: {
    page: 0,
    size: 10,
    total: 0,
    totalPages: 0,
  },
};

/**
 * Complaints Redux slice
 * Manages complaint state and operations
 */
const complaintsSlice = createSlice({
  name: 'complaints',
  initialState,
  reducers: {
    /**
     * Clear complaints error
     */
    clearError: (state) => {
      state.error = null;
    },

    /**
     * Set current complaint
     */
    setCurrentComplaint: (state, action: PayloadAction<Complaint | null>) => {
      state.currentComplaint = action.payload;
    },

    /**
     * Update complaint filters
     */
    updateFilters: (state, action: PayloadAction<ComplaintFilters>) => {
      state.filters = { ...state.filters, ...action.payload };
    },

    /**
     * Clear complaint filters
     */
    clearFilters: (state) => {
      state.filters = {};
    },

    /**
     * Update pagination
     */
    updatePagination: (state, action: PayloadAction<Partial<PaginationState>>) => {
      state.pagination = { ...state.pagination, ...action.payload };
    },

    /**
     * Add a new complaint to the list
     */
    addComplaint: (state, action: PayloadAction<Complaint>) => {
      state.items.unshift(action.payload);
    },

    /**
     * Update an existing complaint in the list
     */
    updateComplaint: (state, action: PayloadAction<Complaint>) => {
      const index = state.items.findIndex(c => c.id === action.payload.id);
      if (index !== -1) {
        state.items[index] = action.payload;
      }
      if (state.currentComplaint?.id === action.payload.id) {
        state.currentComplaint = action.payload;
      }
    },

    /**
     * Remove a complaint from the list
     */
    removeComplaint: (state, action: PayloadAction<string>) => {
      state.items = state.items.filter(c => c.id !== action.payload);
      if (state.currentComplaint?.id === action.payload) {
        state.currentComplaint = null;
      }
    },
  },
  extraReducers: (builder) => {
    // Fetch complaints cases
    builder
      .addCase(fetchComplaints.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchComplaints.fulfilled, (state, action) => {
        state.isLoading = false;
        state.items = action.payload.content;
        state.pagination = {
          page: action.payload.number,
          size: action.payload.size,
          total: action.payload.totalElements,
          totalPages: action.payload.totalPages,
        };
        state.error = null;
      })
      .addCase(fetchComplaints.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });

    // Create complaint cases
    builder
      .addCase(createComplaint.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(createComplaint.fulfilled, (state, action) => {
        state.isLoading = false;
        state.items.unshift(action.payload);
        state.error = null;
      })
      .addCase(createComplaint.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });

    // Update status cases
    builder
      .addCase(updateComplaintStatus.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(updateComplaintStatus.fulfilled, (state, action) => {
        state.isLoading = false;
        const index = state.items.findIndex(c => c.id === action.payload.id);
        if (index !== -1) {
          state.items[index] = action.payload;
        }
        if (state.currentComplaint?.id === action.payload.id) {
          state.currentComplaint = action.payload;
        }
        state.error = null;
      })
      .addCase(updateComplaintStatus.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });

    // Add comment cases
    builder
      .addCase(addComment.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(addComment.fulfilled, (state, action) => {
        state.isLoading = false;
        const { complaintId, comment } = action.payload;
        
        // Update complaint in list
        const complaintIndex = state.items.findIndex(c => c.id === complaintId);
        if (complaintIndex !== -1) {
          state.items[complaintIndex].commentaires.push(comment);
        }
        
        // Update current complaint if it's the same
        if (state.currentComplaint?.id === complaintId) {
          state.currentComplaint.commentaires.push(comment);
        }
        
        state.error = null;
      })
      .addCase(addComment.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });

    // Fetch complaint by ID cases
    builder
      .addCase(fetchComplaintById.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchComplaintById.fulfilled, (state, action) => {
        state.isLoading = false;
        state.currentComplaint = action.payload;
        state.error = null;
      })
      .addCase(fetchComplaintById.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });
  },
});

export const {
  clearError,
  setCurrentComplaint,
  updateFilters,
  clearFilters,
  updatePagination,
  addComplaint,
  updateComplaint,
  removeComplaint,
} = complaintsSlice.actions;

export default complaintsSlice.reducer; 