let complaintId = null;
let currentComplaint = null;

document.addEventListener('DOMContentLoaded', () => {
    if (!requireAuth()) return;

    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) logoutBtn.addEventListener('click', logout);

    const params = new URLSearchParams(window.location.search);
    complaintId = params.get('id');
    if (!complaintId) {
        window.location.href = '/complaints.html';
        return;
    }

    document.getElementById('assignBtn').addEventListener('click', handleAssign);
    document.getElementById('updateStatusBtn').addEventListener('click', handleStatusUpdate);
    document.getElementById('closeComplaintBtn').addEventListener('click', handleClose);

    loadComplaint();
    loadHistory();
});

async function loadComplaint() {
    try {
        const response = await apiRequest('/api/complaints/' + complaintId);
        currentComplaint = response.data;
        renderComplaint(currentComplaint);
        setupActions(currentComplaint);
    } catch (err) {
        showAlert('alert', err.message);
    }
}

async function loadHistory() {
    try {
        const response = await apiRequest('/api/complaints/' + complaintId + '/history');
        renderHistory(response.data);
    } catch (err) {
        document.getElementById('historyTimeline').innerHTML =
            '<p class="text-muted">Failed to load history</p>';
    }
}

function renderComplaint(c) {
    const container = document.getElementById('complaintDetail');
    container.innerHTML = `
        <div class="card">
            <div class="page-header">
                <div>
                    <span class="ticket-number">${escapeHtml(c.ticketNumber)}</span>
                    <h2>${escapeHtml(c.title)}</h2>
                </div>
                <span class="status-badge status-${formatStatusClass(c.status)}">${formatStatus(c.status)}</span>
            </div>
            <div class="detail-grid">
                <div class="detail-item">
                    <span class="detail-label">Category</span>
                    <span class="detail-value">${escapeHtml(c.category?.name || '—')}</span>
                </div>
                <div class="detail-item">
                    <span class="detail-label">Priority</span>
                    <span class="detail-value priority-badge priority-${(c.priority?.name || '').toLowerCase()}">${escapeHtml(c.priority?.name || '—')}</span>
                </div>
                <div class="detail-item">
                    <span class="detail-label">Submitted By</span>
                    <span class="detail-value">${escapeHtml(formatUser(c.submittedBy))}</span>
                </div>
                <div class="detail-item">
                    <span class="detail-label">Assigned To</span>
                    <span class="detail-value">${escapeHtml(formatUser(c.assignedTo) || 'Unassigned')}</span>
                </div>
                <div class="detail-item">
                    <span class="detail-label">Created</span>
                    <span class="detail-value">${formatDateTime(c.createdAt)}</span>
                </div>
                <div class="detail-item">
                    <span class="detail-label">Updated</span>
                    <span class="detail-value">${formatDateTime(c.updatedAt)}</span>
                </div>
            </div>
            <div class="detail-section">
                <h4>Description</h4>
                <p>${escapeHtml(c.description)}</p>
            </div>
            ${c.resolutionNotes ? `
            <div class="detail-section">
                <h4>Resolution Notes</h4>
                <p>${escapeHtml(c.resolutionNotes)}</p>
            </div>` : ''}
        </div>
    `;
}

function renderHistory(history) {
    const container = document.getElementById('historyTimeline');

    if (!history || history.length === 0) {
        container.innerHTML = '<p class="text-muted">No history available</p>';
        return;
    }

    container.innerHTML = history.map(h => `
        <div class="timeline-item">
            <div class="timeline-marker"></div>
            <div class="timeline-content">
                <div class="timeline-header">
                    <span class="status-badge status-${formatStatusClass(h.newStatus)}">${formatStatus(h.newStatus)}</span>
                    <span class="text-muted">${formatDateTime(h.createdAt)}</span>
                </div>
                <p class="timeline-meta">By ${escapeHtml(formatUser(h.changedBy))}${h.previousStatus ? ' · from ' + formatStatus(h.previousStatus) : ''}</p>
                ${h.comment ? `<p>${escapeHtml(h.comment)}</p>` : ''}
            </div>
        </div>
    `).join('');
}

async function setupActions(c) {
    const user = getUser();
    const roles = user?.roles || [];
    const isStaff = roles.includes('ROLE_STAFF') || roles.includes('ROLE_ADMIN');
    const isOwner = user && c.submittedBy && user.id === c.submittedBy.id;

    if (isStaff) {
        document.getElementById('staffActions').style.display = 'block';
        loadAssignableStaff();
    }

    if (isOwner && c.status === 'RESOLVED') {
        document.getElementById('userActions').style.display = 'block';
    }
}

async function loadAssignableStaff() {
    try {
        const response = await apiRequest('/api/complaints/staff/assignable');
        const select = document.getElementById('assignToId');
        response.data.forEach(staff => {
            const option = document.createElement('option');
            option.value = staff.id;
            option.textContent = staff.firstName + ' ' + staff.lastName;
            select.appendChild(option);
        });
    } catch (err) {
        console.error('Failed to load staff', err);
    }
}

async function handleAssign() {
    hideAlert('alert');
    const assignedToId = document.getElementById('assignToId').value;
    if (!assignedToId) {
        showAlert('alert', 'Please select a staff member');
        return;
    }

    try {
        await apiRequest('/api/complaints/' + complaintId + '/assign', {
            method: 'PATCH',
            body: JSON.stringify({
                assignedToId: parseInt(assignedToId, 10),
                comment: document.getElementById('assignComment').value.trim() || null
            })
        });
        showAlert('alert', 'Complaint assigned successfully', 'success');
        loadComplaint();
        loadHistory();
    } catch (err) {
        showAlert('alert', err.message);
    }
}

async function handleStatusUpdate() {
    hideAlert('alert');
    const status = document.getElementById('newStatus').value;
    if (!status) {
        showAlert('alert', 'Please select a status');
        return;
    }

    const resolutionNotes = document.getElementById('resolutionNotes').value.trim();

    try {
        await apiRequest('/api/complaints/' + complaintId + '/status', {
            method: 'PATCH',
            body: JSON.stringify({
                status,
                comment: document.getElementById('statusComment').value.trim() || null,
                resolutionNotes: resolutionNotes || null
            })
        });
        showAlert('alert', 'Status updated successfully', 'success');
        loadComplaint();
        loadHistory();
    } catch (err) {
        showAlert('alert', err.message);
    }
}

async function handleClose() {
    hideAlert('alert');
    try {
        await apiRequest('/api/complaints/' + complaintId + '/status', {
            method: 'PATCH',
            body: JSON.stringify({
                status: 'CLOSED',
                comment: 'Complaint closed by user'
            })
        });
        showAlert('alert', 'Complaint closed successfully', 'success');
        document.getElementById('userActions').style.display = 'none';
        loadComplaint();
        loadHistory();
    } catch (err) {
        showAlert('alert', err.message);
    }
}

function formatUser(user) {
    if (!user) return '';
    return user.firstName + ' ' + user.lastName + ' (' + user.email + ')';
}

function formatStatus(status) {
    if (!status) return '—';
    return status.replace(/_/g, ' ').replace(/\b\w/g, c => c.toUpperCase());
}

function formatStatusClass(status) {
    return (status || '').toLowerCase().replace(/_/g, '-');
}

function formatDateTime(dateStr) {
    if (!dateStr) return '—';
    return new Date(dateStr).toLocaleString(undefined, {
        year: 'numeric', month: 'short', day: 'numeric',
        hour: '2-digit', minute: '2-digit'
    });
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text || '';
    return div.innerHTML;
}
