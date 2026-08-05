// Modern UI Components JavaScript
// Smart Complaint Management System

document.addEventListener('DOMContentLoaded', function() {
    initializeSidebar();
    initializeNavbar();
    initializeUserDropdown();
    initializeNotifications();
    initializeActivePage();
    checkUserRole();
});

// ==========================================
// SIDEBAR FUNCTIONALITY
// ==========================================
function initializeSidebar() {
    const sidebar = document.getElementById('sidebar');
    const sidebarToggle = document.getElementById('sidebarToggle');
    const mobileMenuToggle = document.getElementById('mobileMenuToggle');
    const sidebarOverlay = document.getElementById('sidebarOverlay');
    
    if (!sidebar) return;
    
    // Toggle sidebar collapse (desktop)
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', function() {
            sidebar.classList.toggle('collapsed');
            const mainContent = document.querySelector('.main-content');
            if (mainContent) {
                mainContent.classList.toggle('sidebar-collapsed');
            }
        });
    }
    
    // Toggle mobile sidebar
    if (mobileMenuToggle) {
        mobileMenuToggle.addEventListener('click', function() {
            sidebar.classList.toggle('mobile-open');
            if (sidebarOverlay) {
                sidebarOverlay.classList.toggle('show');
            }
        });
    }
    
    // Close sidebar on overlay click
    if (sidebarOverlay) {
        sidebarOverlay.addEventListener('click', function() {
            sidebar.classList.remove('mobile-open');
            sidebarOverlay.classList.remove('show');
        });
    }
    
    // Close sidebar on escape key
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            sidebar.classList.remove('mobile-open');
            if (sidebarOverlay) {
                sidebarOverlay.classList.remove('show');
            }
        }
    });
}

// ==========================================
// NAVBAR FUNCTIONALITY
// ==========================================
function initializeNavbar() {
    const globalSearch = document.getElementById('globalSearch');
    
    if (globalSearch) {
        globalSearch.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                const searchTerm = this.value.trim();
                if (searchTerm) {
                    // Redirect to complaints page with search parameter
                    window.location.href = `/complaints.html?search=${encodeURIComponent(searchTerm)}`;
                }
            }
        });
    }
}

// ==========================================
// USER DROPDOWN FUNCTIONALITY
// ==========================================
function initializeUserDropdown() {
    const userDropdownToggle = document.getElementById('userDropdownToggle');
    const userDropdownMenu = document.getElementById('userDropdownMenu');
    const userDropdown = document.getElementById('userDropdown');
    
    if (!userDropdownToggle || !userDropdownMenu) return;
    
    userDropdownToggle.addEventListener('click', function(e) {
        e.stopPropagation();
        userDropdownMenu.classList.toggle('show');
    });
    
    // Close dropdown when clicking outside
    document.addEventListener('click', function(e) {
        if (userDropdown && !userDropdown.contains(e.target)) {
            userDropdownMenu.classList.remove('show');
        }
    });
    
    // Load user information
    loadUserInfo();
}

function loadUserInfo() {
    const user = getUser();
    if (user) {
        const userName = document.getElementById('userName');
        const userAvatar = document.getElementById('userAvatar');
        
        if (userName) {
            userName.textContent = `${user.firstName} ${user.lastName}`;
        }
        
        if (userAvatar) {
            const initials = `${user.firstName.charAt(0)}${user.lastName.charAt(0)}`.toUpperCase();
            userAvatar.textContent = initials;
        }
    }
}

// ==========================================
// NOTIFICATIONS FUNCTIONALITY
// ==========================================
function initializeNotifications() {
    const notificationBell = document.getElementById('notificationBell');
    const notificationBadge = document.getElementById('notificationBadge');
    
    if (!notificationBell) return;
    
    notificationBell.addEventListener('click', function() {
        // Here you would load notifications from the API
        showToast('No new notifications', 'info');
        
        // Clear badge
        if (notificationBadge) {
            notificationBadge.style.display = 'none';
        }
    });
}

// ==========================================
// ACTIVE PAGE HIGHLIGHTING
// ==========================================
function initializeActivePage() {
    const currentPage = window.location.pathname;
    const sidebarLinks = document.querySelectorAll('.sidebar-menu-link');
    
    sidebarLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href && currentPage.includes(href)) {
            link.classList.add('active');
        }
    });
}

// ==========================================
// USER ROLE CHECK
// ==========================================
function checkUserRole() {
    const user = getUser();
    const adminSection = document.getElementById('adminSection');
    
    if (user && user.roles && adminSection) {
        const hasAdminRole = user.roles.some(role => 
            role.name === 'ROLE_ADMIN' || role.name === 'ROLE_STAFF'
        );
        
        if (hasAdminRole) {
            adminSection.style.display = 'block';
        }
    }
}

// ==========================================
// LOGOUT FUNCTIONALITY
// ==========================================
document.addEventListener('click', function(e) {
    if (e.target && e.target.id === 'logoutBtn') {
        e.preventDefault();
        logout();
    }
});

// ==========================================
// RESPONSIVE HANDLING
// ==========================================
window.addEventListener('resize', function() {
    const sidebar = document.getElementById('sidebar');
    const sidebarOverlay = document.getElementById('sidebarOverlay');
    
    if (window.innerWidth > 1024) {
        if (sidebar) {
            sidebar.classList.remove('mobile-open');
        }
        if (sidebarOverlay) {
            sidebarOverlay.classList.remove('show');
        }
    }
});

// ==========================================
// ANIMATION HELPERS
// ==========================================
function animateElement(element, animation) {
    element.classList.add(animation);
    setTimeout(() => {
        element.classList.remove(animation);
    }, 300);
}

function fadeIn(element) {
    element.style.opacity = '0';
    element.style.display = 'block';
    setTimeout(() => {
        element.style.transition = 'opacity 0.3s ease-in';
        element.style.opacity = '1';
    }, 10);
}

function fadeOut(element) {
    element.style.transition = 'opacity 0.3s ease-out';
    element.style.opacity = '0';
    setTimeout(() => {
        element.style.display = 'none';
    }, 300);
}

// ==========================================
// CARD LIFT EFFECT
// ==========================================
document.querySelectorAll('.dashboard-card, .card').forEach(card => {
    card.classList.add('card-lift');
});

// ==========================================
// FORM VALIDATION ENHANCEMENT
// ==========================================
function validateForm(form) {
    const inputs = form.querySelectorAll('input[required], select[required], textarea[required]');
    let isValid = true;
    
    inputs.forEach(input => {
        if (!input.value.trim()) {
            isValid = false;
            input.classList.add('is-invalid');
        } else {
            input.classList.remove('is-invalid');
        }
    });
    
    return isValid;
}

// ==========================================
// DATE FORMATTING
// ==========================================
function formatDate(dateString) {
    const date = new Date(dateString);
    const now = new Date();
    const diff = now - date;
    
    // Less than a minute
    if (diff < 60000) {
        return 'Just now';
    }
    
    // Less than an hour
    if (diff < 3600000) {
        const minutes = Math.floor(diff / 60000);
        return `${minutes} minute${minutes > 1 ? 's' : ''} ago`;
    }
    
    // Less than a day
    if (diff < 86400000) {
        const hours = Math.floor(diff / 3600000);
        return `${hours} hour${hours > 1 ? 's' : ''} ago`;
    }
    
    // Less than a week
    if (diff < 604800000) {
        const days = Math.floor(diff / 86400000);
        return `${days} day${days > 1 ? 's' : ''} ago`;
    }
    
    // Format date
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

// ==========================================
// STATUS BADGE HELPER
// ==========================================
function getStatusBadge(status) {
    const badges = {
        'SUBMITTED': 'status-submitted',
        'ASSIGNED': 'status-assigned',
        'IN_PROGRESS': 'status-in-progress',
        'RESOLVED': 'status-resolved',
        'CLOSED': 'status-closed',
        'REJECTED': 'status-rejected'
    };
    
    const badgeClass = badges[status] || 'badge-secondary';
    return `<span class="badge ${badgeClass}">${status.replace(/_/g, ' ')}</span>`;
}

// ==========================================
// PRIORITY BADGE HELPER
// ==========================================
function getPriorityBadge(priority) {
    const badges = {
        'LOW': 'priority-low',
        'MEDIUM': 'priority-medium',
        'HIGH': 'priority-high',
        'CRITICAL': 'priority-critical'
    };
    
    const badgeClass = badges[priority] || 'badge-secondary';
    return `<span class="badge ${badgeClass}">${priority}</span>`;
}

// ==========================================
// EMPTY STATE HELPER
// ==========================================
function showEmptyState(container, message = 'No data found') {
    container.innerHTML = `
        <div class="empty-state">
            <div class="empty-state-icon">
                <i class="bi bi-inbox"></i>
            </div>
            <div class="empty-state-title">No Data</div>
            <div class="empty-state-description">${message}</div>
        </div>
    `;
}

// ==========================================
// CONFIRMATION DIALOG
// ==========================================
function confirmAction(message, callback) {
    if (confirm(message)) {
        callback();
    }
}

// ==========================================
// COPY TO CLIPBOARD
// ==========================================
function copyToClipboard(text) {
    navigator.clipboard.writeText(text).then(() => {
        showToast('Copied to clipboard', 'success');
    }).catch(() => {
        showToast('Failed to copy', 'error');
    });
}

// ==========================================
// EXPORT FUNCTIONS FOR GLOBAL USE
// ==========================================
window.UIComponents = {
    animateElement,
    fadeIn,
    fadeOut,
    validateForm,
    formatDate,
    getStatusBadge,
    getPriorityBadge,
    showEmptyState,
    confirmAction,
    copyToClipboard
};