/**
 * Smart Complaint Management System - Main JavaScript
 * Navigation, Interactivity, and API Integration
 */

// ==========================================
// GLOBAL VARIABLES
// ==========================================
const API_BASE_URL = '/api';
let currentUser = null;
let theme = localStorage.getItem('theme') || 'light';

// ==========================================
// UTILITY FUNCTIONS
// ==========================================
function showToast(message, type = 'info', title = 'Notification') {
    const toastContainer = document.querySelector('.toast-container');
    if (!toastContainer) return;
    
    const toast = document.getElementById('toast');
    if (!toast) return;
    
    const toastIcon = document.getElementById('toastIcon');
    const toastTitle = document.getElementById('toastTitle');
    const toastMessage = document.getElementById('toastMessage');
    
    // Set icon based on type
    const icons = {
        success: 'bi-check-circle',
        error: 'bi-x-circle',
        warning: 'bi-exclamation-triangle',
        info: 'bi-info-circle'
    };
    
    toastIcon.className = `bi me-2 ${icons[type] || icons.info}`;
    toastTitle.textContent = title;
    toastMessage.textContent = message;
    
    // Show toast
    const bsToast = new bootstrap.Toast(toast);
    bsToast.show();
}

function showAlert(message, type = 'info') {
    const alertElement = document.getElementById('alert');
    if (!alertElement) return;
    
    const alertClasses = {
        success: 'alert-success',
        error: 'alert-danger',
        warning: 'alert-warning',
        info: 'alert-info'
    };
    
    alertElement.className = `alert show ${alertClasses[type] || alertClasses.info}`;
    alertElement.innerHTML = `
        <i class="bi bi-${type === 'success' ? 'check-circle' : type === 'error' ? 'x-circle' : type === 'warning' ? 'exclamation-triangle' : 'info-circle'} me-2"></i>
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `;
    
    // Auto-hide after 5 seconds
    setTimeout(() => {
        alertElement.classList.remove('show');
    }, 5000);
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

function formatDateTime(dateString) {
    const date = new Date(dateString);
    return date.toLocaleString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

function formatRelativeTime(dateString) {
    const date = new Date(dateString);
    const now = new Date();
    const diffMs = now - date;
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMs / 3600000);
    const diffDays = Math.floor(diffMs / 86400000);
    
    if (diffMins < 1) return 'Just now';
    if (diffMins < 60) return `${diffMins} minute${diffMins > 1 ? 's' : ''} ago`;
    if (diffHours < 24) return `${diffHours} hour${diffHours > 1 ? 's' : ''} ago`;
    if (diffDays < 7) return `${diffDays} day${diffDays > 1 ? 's' : ''} ago`;
    return formatDate(dateString);
}

// ==========================================
// THEME MANAGEMENT
// ==========================================
function initializeTheme() {
    const themeToggle = document.getElementById('themeToggle');
    if (!themeToggle) return;
    
    // Apply saved theme
    if (theme === 'dark') {
        document.body.classList.add('dark-theme');
        themeToggle.innerHTML = '<i class="bi bi-sun"></i>';
    }
    
    // Toggle theme on click
    themeToggle.addEventListener('click', function() {
        theme = theme === 'light' ? 'dark' : 'light';
        document.body.classList.toggle('dark-theme', theme === 'dark');
        themeToggle.innerHTML = theme === 'dark' ? '<i class="bi bi-sun"></i>' : '<i class="bi bi-moon"></i>';
        localStorage.setItem('theme', theme);
    });
}

// ==========================================
// SIDEBAR MANAGEMENT
// ==========================================
function initializeSidebar() {
    const sidebarToggle = document.getElementById('sidebarToggle');
    const mainContent = document.getElementById('mainContent');
    
    if (!sidebarToggle || !mainContent) return;
    
    sidebarToggle.addEventListener('click', function() {
        mainContent.classList.toggle('sidebar-collapsed');
    });
}

// ==========================================
// API INTEGRATION
// ==========================================
const API = {
    // Authentication
    async login(email, password) {
        try {
            const response = await fetch(`${API_BASE_URL}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, password })
            });
            
            if (!response.ok) throw new Error('Login failed');
            
            const data = await response.json();
            localStorage.setItem('token', data.token);
            localStorage.setItem('user', JSON.stringify(data.user));
            currentUser = data.user;
            
            return data;
        } catch (error) {
            console.error('Login error:', error);
            throw error;
        }
    },
    
    async register(userData) {
        try {
            const response = await fetch(`${API_BASE_URL}/auth/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });
            
            if (!response.ok) throw new Error('Registration failed');
            
            return await response.json();
        } catch (error) {
            console.error('Registration error:', error);
            throw error;
        }
    },
    
    async logout() {
        try {
            await fetch(`${API_BASE_URL}/auth/logout`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
        } catch (error) {
            console.error('Logout error:', error);
        } finally {
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            currentUser = null;
            window.location.href = '/index.html';
        }
    },
    
    // Complaints
    async getComplaints(filters = {}) {
        try {
            const queryString = new URLSearchParams(filters).toString();
            const response = await fetch(`${API_BASE_URL}/complaints${queryString ? '?' + queryString : ''}`, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            
            if (!response.ok) throw new Error('Failed to fetch complaints');
            
            return await response.json();
        } catch (error) {
            console.error('Get complaints error:', error);
            throw error;
        }
    },
    
    async getComplaint(id) {
        try {
            const response = await fetch(`${API_BASE_URL}/complaints/${id}`, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            
            if (!response.ok) throw new Error('Failed to fetch complaint');
            
            return await response.json();
        } catch (error) {
            console.error('Get complaint error:', error);
            throw error;
        }
    },
    
    async createComplaint(complaintData) {
        try {
            const response = await fetch(`${API_BASE_URL}/complaints`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(complaintData)
            });
            
            if (!response.ok) throw new Error('Failed to create complaint');
            
            return await response.json();
        } catch (error) {
            console.error('Create complaint error:', error);
            throw error;
        }
    },
    
    async updateComplaint(id, updates) {
        try {
            const response = await fetch(`${API_BASE_URL}/complaints/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(updates)
            });
            
            if (!response.ok) throw new Error('Failed to update complaint');
            
            return await response.json();
        } catch (error) {
            console.error('Update complaint error:', error);
            throw error;
        }
    },
    
    async addComment(complaintId, comment) {
        try {
            const response = await fetch(`${API_BASE_URL}/complaints/${complaintId}/comments`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify({ text: comment })
            });
            
            if (!response.ok) throw new Error('Failed to add comment');
            
            return await response.json();
        } catch (error) {
            console.error('Add comment error:', error);
            throw error;
        }
    },
    
    // Users (Admin only)
    async getUsers(filters = {}) {
        try {
            const queryString = new URLSearchParams(filters).toString();
            const response = await fetch(`${API_BASE_URL}/admin/users${queryString ? '?' + queryString : ''}`, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            
            if (!response.ok) throw new Error('Failed to fetch users');
            
            return await response.json();
        } catch (error) {
            console.error('Get users error:', error);
            throw error;
        }
    },
    
    async updateUser(id, updates) {
        try {
            const response = await fetch(`${API_BASE_URL}/admin/users/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(updates)
            });
            
            if (!response.ok) throw new Error('Failed to update user');
            
            return await response.json();
        } catch (error) {
            console.error('Update user error:', error);
            throw error;
        }
    },
    
    async deleteUser(id) {
        try {
            const response = await fetch(`${API_BASE_URL}/admin/users/${id}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            
            if (!response.ok) throw new Error('Failed to delete user');
            
            return await response.json();
        } catch (error) {
            console.error('Delete user error:', error);
            throw error;
        }
    },
    
    // Dashboard Stats
    async getDashboardStats() {
        try {
            const response = await fetch(`${API_BASE_URL}/dashboard/stats`, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            
            if (!response.ok) throw new Error('Failed to fetch dashboard stats');
            
            return await response.json();
        } catch (error) {
            console.error('Get dashboard stats error:', error);
            throw error;
        }
    },
    
    // Notifications
    async getNotifications() {
        try {
            const response = await fetch(`${API_BASE_URL}/notifications`, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            
            if (!response.ok) throw new Error('Failed to fetch notifications');
            
            return await response.json();
        } catch (error) {
            console.error('Get notifications error:', error);
            throw error;
        }
    },
    
    async markNotificationAsRead(id) {
        try {
            const response = await fetch(`${API_BASE_URL}/notifications/${id}/read`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            
            if (!response.ok) throw new Error('Failed to mark notification as read');
            
            return await response.json();
        } catch (error) {
            console.error('Mark notification as read error:', error);
            throw error;
        }
    },
    
    // Profile
    async getProfile() {
        try {
            const response = await fetch(`${API_BASE_URL}/profile`, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            
            if (!response.ok) throw new Error('Failed to fetch profile');
            
            return await response.json();
        } catch (error) {
            console.error('Get profile error:', error);
            throw error;
        }
    },
    
    async updateProfile(updates) {
        try {
            const response = await fetch(`${API_BASE_URL}/profile`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(updates)
            });
            
            if (!response.ok) throw new Error('Failed to update profile');
            
            return await response.json();
        } catch (error) {
            console.error('Update profile error:', error);
            throw error;
        }
    },
    
    async changePassword(currentPassword, newPassword) {
        try {
            const response = await fetch(`${API_BASE_URL}/profile/password`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify({
                    currentPassword,
                    newPassword
                })
            });
            
            if (!response.ok) throw new Error('Failed to change password');
            
            return await response.json();
        } catch (error) {
            console.error('Change password error:', error);
            throw error;
        }
    }
};

// ==========================================
// FORM VALIDATION
// ==========================================
function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

function validatePassword(password) {
    // At least 6 characters, one letter, one number
    const minLength = password.length >= 6;
    const hasLetter = /[a-zA-Z]/.test(password);
    const hasNumber = /\d/.test(password);
    
    return minLength && hasLetter && hasNumber;
}

function validateForm(form) {
    const inputs = form.querySelectorAll('input[required], select[required], textarea[required]');
    let isValid = true;
    
    inputs.forEach(input => {
        if (!input.value.trim()) {
            input.classList.add('is-invalid');
            isValid = false;
        } else {
            input.classList.remove('is-invalid');
        }
        
        // Email validation
        if (input.type === 'email' && input.value.trim()) {
            if (!validateEmail(input.value)) {
                input.classList.add('is-invalid');
                isValid = false;
            }
        }
        
        // Password validation
        if (input.type === 'password' && input.value.trim()) {
            if (!validatePassword(input.value)) {
                input.classList.add('is-invalid');
                isValid = false;
            }
        }
    });
    
    return isValid;
}

// ==========================================
// FILE UPLOAD
// ==========================================
function handleFileUpload(fileInput, maxSize = 10 * 1024 * 1024, allowedTypes = ['image/png', 'image/jpeg', 'image/jpg', 'application/pdf']) {
    const file = fileInput.files[0];
    
    if (!file) return null;
    
    // Validate file type
    if (!allowedTypes.includes(file.type)) {
        showToast('Please upload a PNG, JPG, or PDF file', 'error');
        fileInput.value = '';
        return null;
    }
    
    // Validate file size
    if (file.size > maxSize) {
        showToast('File size must be less than 10MB', 'error');
        fileInput.value = '';
        return null;
    }
    
    return file;
}

// ==========================================
// LOADING STATES
// ==========================================
function showLoading(button, originalText) {
    button.disabled = true;
    button.innerHTML = `
        <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
        <span class="ms-2">Loading...</span>
    `;
    button.dataset.originalText = originalText;
}

function hideLoading(button) {
    button.disabled = false;
    button.innerHTML = button.dataset.originalText || 'Submit';
}

// ==========================================
// NAVIGATION HELPERS
// ==========================================
function navigateTo(page) {
    window.location.href = page;
}

function getCurrentUser() {
    const userStr = localStorage.getItem('user');
    if (userStr) {
        currentUser = JSON.parse(userStr);
    }
    return currentUser;
}

function isAuthenticated() {
    return localStorage.getItem('token') !== null;
}

function checkAuth() {
    if (!isAuthenticated()) {
        navigateTo('/index.html');
        return false;
    }
    return true;
}

// ==========================================
// INITIALIZATION
// ==========================================
document.addEventListener('DOMContentLoaded', function() {
    // Initialize theme
    initializeTheme();
    
    // Initialize sidebar
    initializeSidebar();
    
    // Check authentication on protected pages
    const protectedPages = [
        'user-dashboard.html',
        'admin-dashboard.html',
        'complaint-create.html',
        'complaint-track.html',
        'complaint-detail.html',
        'manage-complaints.html',
        'manage-users.html',
        'notifications.html',
        'profile.html'
    ];
    
    const currentPage = window.location.pathname.split('/').pop();
    
    if (protectedPages.includes(currentPage)) {
        checkAuth();
    }
    
    // Set up global error handling
    window.addEventListener('error', function(event) {
        console.error('Global error:', event.error);
        showToast('An unexpected error occurred', 'error');
    });
    
    // Set up form validation
    const forms = document.querySelectorAll('form[novalidate]');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            if (!validateForm(form)) {
                e.preventDefault();
                e.stopPropagation();
            }
        });
    });
});

// ==========================================
// EXPORT FOR USE IN OTHER FILES
// ==========================================
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        API,
        showToast,
        showAlert,
        formatDate,
        formatDateTime,
        formatRelativeTime,
        validateEmail,
        validatePassword,
        validateForm,
        handleFileUpload,
        showLoading,
        hideLoading,
        navigateTo,
        getCurrentUser,
        isAuthenticated,
        checkAuth
    };
}