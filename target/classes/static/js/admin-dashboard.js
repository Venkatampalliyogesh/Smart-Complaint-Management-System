document.addEventListener('DOMContentLoaded', () => {
    if (!requireAuth()) return;

    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) logoutBtn.addEventListener('click', logout);

    loadDashboardStats();
});

async function loadDashboardStats() {
    toggleLoadingSpinner(true);
    hideAlert('alert');

    try {
        const response = await apiRequest('/api/admin/dashboard');
        const stats = response.data;
        
        document.getElementById('totalUsers').textContent = stats.totalUsers || 0;
        document.getElementById('activeUsers').textContent = stats.activeUsers || 0;
        document.getElementById('totalComplaints').textContent = stats.totalComplaints || 0;
        document.getElementById('openComplaints').textContent = stats.openComplaints || 0;
        document.getElementById('inProgressComplaints').textContent = stats.inProgressComplaints || 0;
        document.getElementById('resolvedComplaints').textContent = stats.resolvedComplaints || 0;
        document.getElementById('assignedToMe').textContent = stats.assignedToMe || 0;
        document.getElementById('totalStaff').textContent = stats.totalStaff || 0;

        renderStatusChart(stats.complaintsByStatus);
        renderRoleChart(stats.usersByRole);
    } catch (err) {
        showToast(err.message, 'error');
    } finally {
        toggleLoadingSpinner(false);
    }
}

function renderStatusChart(data) {
    const container = document.getElementById('statusChart');
    if (!data) {
        container.innerHTML = '<p class="text-muted">No data available</p>';
        return;
    }

    const entries = Object.entries(data);
    const max = Math.max(...entries.map(([, value]) => value), 1);

    container.innerHTML = entries.map(([status, count]) => `
        <div class="mb-3">
            <div class="d-flex justify-content-between mb-1">
                <span class="fw-medium">${formatStatus(status)}</span>
                <span class="text-muted">${count}</span>
            </div>
            <div class="progress" style="height: 10px;">
                <div class="progress-bar" style="width: ${(count / max) * 100}%; background-color: ${getStatusColor(status)}"></div>
            </div>
        </div>
    `).join('');
}

function renderRoleChart(data) {
    const container = document.getElementById('roleChart');
    if (!data) {
        container.innerHTML = '<p class="text-muted">No data available</p>';
        return;
    }

    const entries = Object.entries(data);
    const max = Math.max(...entries.map(([, value]) => value), 1);

    container.innerHTML = entries.map(([role, count]) => `
        <div class="mb-3">
            <div class="d-flex justify-content-between mb-1">
                <span class="fw-medium">${formatRole(role)}</span>
                <span class="text-muted">${count}</span>
            </div>
            <div class="progress" style="height: 10px;">
                <div class="progress-bar bg-info" style="width: ${(count / max) * 100}%"></div>
            </div>
        </div>
    `).join('');
}

function formatStatus(status) {
    if (!status) return '—';
    return status.replace(/_/g, ' ').replace(/\b\w/g, c => c.toUpperCase());
}

function getStatusColor(status) {
    const colors = {
        'SUBMITTED': '#2563EB',
        'ASSIGNED': '#0891B2',
        'IN_PROGRESS': '#F59E0B',
        'RESOLVED': '#16A34A',
        'CLOSED': '#64748B',
        'REJECTED': '#DC2626'
    };
    return colors[status] || '#64748B';
}

function formatRole(role) {
    if (!role) return '—';
    return role.replace('ROLE_', '').replace('_', ' ');
}