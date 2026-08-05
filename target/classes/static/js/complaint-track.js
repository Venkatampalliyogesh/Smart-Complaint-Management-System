document.addEventListener('DOMContentLoaded', () => {
    if (!requireAuth()) return;

    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) logoutBtn.addEventListener('click', logout);

    document.getElementById('trackForm').addEventListener('submit', handleTrack);
});

async function handleTrack(e) {
    e.preventDefault();
    hideAlert('alert');
    setFormLoading(e.target, true);

    const ticketNumber = document.getElementById('ticketNumber').value.trim();

    try {
        const response = await apiRequest('/api/complaints/ticket/' + encodeURIComponent(ticketNumber));
        renderTrackResult(response.data);
    } catch (err) {
        showAlert('alert', err.message);
        document.getElementById('trackResult').innerHTML = '';
    } finally {
        setFormLoading(e.target, false);
    }
}

function renderTrackResult(c) {
    const container = document.getElementById('trackResult');
    container.innerHTML = `
        <div class="card">
            <div class="page-header">
                <div>
                    <span class="ticket-number">${escapeHtml(c.ticketNumber)}</span>
                    <h3>${escapeHtml(c.title)}</h3>
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
                    <span class="detail-value">${escapeHtml(c.priority?.name || '—')}</span>
                </div>
                <div class="detail-item">
                    <span class="detail-label">Assigned To</span>
                    <span class="detail-value">${escapeHtml(formatUser(c.assignedTo) || 'Unassigned')}</span>
                </div>
                <div class="detail-item">
                    <span class="detail-label">Created</span>
                    <span class="detail-value">${formatDateTime(c.createdAt)}</span>
                </div>
            </div>
            <div class="nav-actions">
                <a href="/complaint-detail.html?id=${c.id}" class="btn btn-primary">View Full Details</a>
            </div>
        </div>
    `;
}

function formatUser(user) {
    if (!user) return '';
    return user.firstName + ' ' + user.lastName;
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
