/**
 * GIRA - Notifications Redux Slice
 * Manages notification state and real-time updates
 */

import type { PayloadAction } from '@reduxjs/toolkit';
import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import type { Notification, NotificationState } from '../../types';

/**
 * Async thunk for fetching user notifications
 */
export const fetchNotifications = createAsyncThunk(
  'notifications/fetchNotifications',
  async (_, { rejectWithValue }) => {
    try {
      const response = await fetch('/api/notifications', {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        return rejectWithValue('Failed to fetch notifications');
      }

      const data = await response.json();
      return data;
    } catch (error) {
      return rejectWithValue('Network error occurred');
    }
  }
);

/**
 * Async thunk for marking a notification as read
 */
export const markNotificationAsRead = createAsyncThunk(
  'notifications/markAsRead',
  async (notificationId: string, { rejectWithValue }) => {
    try {
      const response = await fetch(`/api/notifications/${notificationId}/read`, {
        method: 'PATCH',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        return rejectWithValue('Failed to mark notification as read');
      }

      return notificationId;
    } catch (error) {
      return rejectWithValue('Network error occurred');
    }
  }
);

/**
 * Async thunk for marking all notifications as read
 */
export const markAllNotificationsAsRead = createAsyncThunk(
  'notifications/markAllAsRead',
  async (_, { rejectWithValue }) => {
    try {
      const response = await fetch('/api/notifications/read-all', {
        method: 'PATCH',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        return rejectWithValue('Failed to mark all notifications as read');
      }

      return true;
    } catch (error) {
      return rejectWithValue('Network error occurred');
    }
  }
);

/**
 * Async thunk for deleting a notification
 */
export const deleteNotification = createAsyncThunk(
  'notifications/deleteNotification',
  async (notificationId: string, { rejectWithValue }) => {
    try {
      const response = await fetch(`/api/notifications/${notificationId}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        return rejectWithValue('Failed to delete notification');
      }

      return notificationId;
    } catch (error) {
      return rejectWithValue('Network error occurred');
    }
  }
);

/**
 * Initial state for notifications slice
 */
const initialState: NotificationState = {
  items: [],
  unreadCount: 0,
  isLoading: false,
  error: null,
};

/**
 * Notifications Redux slice
 * Manages notification state and real-time updates
 */
const notificationsSlice = createSlice({
  name: 'notifications',
  initialState,
  reducers: {
    /**
     * Clear notifications error
     */
    clearError: (state) => {
      state.error = null;
    },

    /**
     * Add a new notification (for real-time updates)
     */
    addNotification: (state, action: PayloadAction<Notification>) => {
      state.items.unshift(action.payload);
      if (!action.payload.isLue) {
        state.unreadCount += 1;
      }
    },

    /**
     * Update an existing notification
     */
    updateNotification: (state, action: PayloadAction<Notification>) => {
      const index = state.items.findIndex(n => n.id === action.payload.id);
      if (index !== -1) {
        const wasRead = state.items[index].isLue;
        state.items[index] = action.payload;
        
        // Update unread count
        if (!wasRead && action.payload.isLue) {
          state.unreadCount = Math.max(0, state.unreadCount - 1);
        } else if (wasRead && !action.payload.isLue) {
          state.unreadCount += 1;
        }
      }
    },

    /**
     * Remove a notification
     */
    removeNotification: (state, action: PayloadAction<string>) => {
      const index = state.items.findIndex(n => n.id === action.payload);
      if (index !== -1) {
        if (!state.items[index].isLue) {
          state.unreadCount = Math.max(0, state.unreadCount - 1);
        }
        state.items.splice(index, 1);
      }
    },

    /**
     * Mark a notification as read locally
     */
    markAsRead: (state, action: PayloadAction<string>) => {
      const notification = state.items.find(n => n.id === action.payload);
      if (notification && !notification.isLue) {
        notification.isLue = true;
        state.unreadCount = Math.max(0, state.unreadCount - 1);
      }
    },

    /**
     * Mark all notifications as read locally
     */
    markAllAsRead: (state) => {
      state.items.forEach(notification => {
        notification.isLue = true;
      });
      state.unreadCount = 0;
    },

    /**
     * Set unread count
     */
    setUnreadCount: (state, action: PayloadAction<number>) => {
      state.unreadCount = action.payload;
    },

    /**
     * Clear all notifications
     */
    clearNotifications: (state) => {
      state.items = [];
      state.unreadCount = 0;
    },
  },
  extraReducers: (builder) => {
    // Fetch notifications cases
    builder
      .addCase(fetchNotifications.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchNotifications.fulfilled, (state, action) => {
        state.isLoading = false;
        state.items = action.payload.notifications || action.payload;
        state.unreadCount = action.payload.unreadCount || 
          state.items.filter(n => !n.isLue).length;
        state.error = null;
      })
      .addCase(fetchNotifications.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });

    // Mark as read cases
    builder
      .addCase(markNotificationAsRead.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(markNotificationAsRead.fulfilled, (state, action) => {
        state.isLoading = false;
        const notification = state.items.find(n => n.id === action.payload);
        if (notification && !notification.isLue) {
          notification.isLue = true;
          state.unreadCount = Math.max(0, state.unreadCount - 1);
        }
        state.error = null;
      })
      .addCase(markNotificationAsRead.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });

    // Mark all as read cases
    builder
      .addCase(markAllNotificationsAsRead.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(markAllNotificationsAsRead.fulfilled, (state) => {
        state.isLoading = false;
        state.items.forEach(notification => {
          notification.isLue = true;
        });
        state.unreadCount = 0;
        state.error = null;
      })
      .addCase(markAllNotificationsAsRead.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });

    // Delete notification cases
    builder
      .addCase(deleteNotification.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(deleteNotification.fulfilled, (state, action) => {
        state.isLoading = false;
        const index = state.items.findIndex(n => n.id === action.payload);
        if (index !== -1) {
          if (!state.items[index].isLue) {
            state.unreadCount = Math.max(0, state.unreadCount - 1);
          }
          state.items.splice(index, 1);
        }
        state.error = null;
      })
      .addCase(deleteNotification.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });
  },
});

export const {
  clearError,
  addNotification,
  updateNotification,
  removeNotification,
  markAsRead,
  markAllAsRead,
  setUnreadCount,
  clearNotifications,
} = notificationsSlice.actions;

export default notificationsSlice.reducer; 