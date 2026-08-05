let currentPage = 0;
const pageSize = 10;

document.addEventListener('DOMContentLoaded', () => {
    if (!requireAuth()) return;

    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) logoutBtn.addEventListener('click', logout);

    document.getElementById('applyFiltersBtn').addEventListener('click', () => {
        currentPage = 0;
        loadComplaints();
    });

    document.getElementById('searchInput').addEventListener('keydown', (e) => {
        if (e.key === 'Enter') {
            currentPage = 0;
            loadComplaints();
        }
    });

    loadFilters();
    loadComplaints();
});

async function loadFilters() {
    try {
        const [categoriesRes, prioritiesRes] = await Promise.all([
            apiRequest('/api/categories'),
            apiRequest('/api/priorities')
        ]);

        populateSelect('categoryFilter', categoriesRes.data, 'id', 'name', true);
        populateSelect('priorityFilter', prioritiesRes.data, 'id', 'name', true);
    } catch (err) {
        showToast(err.message, 'error');
    }
}

function populateSelect(elementId, items, valueKey, labelKey, includeAll) {
    const select = document.getElementById(elementId);
    if (!select) return;

    const firstOption = select.options[0];
    select.innerHTML = '';
    if (firstOption) select.appendChild(firstOption);

    items.forEach(item => {
        const option = document.createElement('option');
        option.value = item[valueKey];
        option.textContent = item[labelKey];
        select.appendChild(option);
    });
}

async function loadComplaints() {
    toggleLoadingSpinner(true);
    hideAlert('alert');

    const params = new URLSearchParams();
    params.set('page', currentPage);
    params.set('size', pageSize);

    const search = document.getElementById('searchInput').value.trim();
    const status = document.getElementById('statusFilter').value;
    const categoryId = document.getElementById('categoryFilter').value;
    const priorityId = document.getElementById('priorityFilter').value;

    if (search) params.set('search', search);
    if (status) params.set('status', status);
    if (categoryId) params.set('categoryId', categoryId);
    if (priorityId) params.set('priorityId', priorityId);

    try {
        const response = await apiRequest('/api/complaints?' + params.toString());
        renderComplaints(response.data.content);
        renderPagination(response.data);
    } catch (err) {
        showToast(err.message, 'error');
        document.getElementById('complaintsTableBody').innerHTML =
            '<tr><td colspan="8" class="text-center text-muted">Failed to load complaints</td></tr>';
    } finally {
        toggleLoadingSpinner(false);
    }
}

function renderComplaints(complaints) {
    const tbody = document.getElementById('complaintsTableBody');

    if (!complaints || complaints.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" class="text-center text-muted">No complaints found</td></tr>';
        return;
    }

    tbody.innerHTML = complaints.map(c => `
        <tr>
            <td><span class="font-monospace fw-bold">${escapeHtml(c.ticketNumber)}</span></td>
            <td>${escapeHtml(c.title)}</td>
            <td>${escapeHtml(c.category?.name || '—')}</td>
            <td><span class="badge bg-${getPriorityColor(c.priority?.name)}">${escapeHtml(c.priority?.name || '—')}</span></td>
            <td><span class="badge bg-${getStatusColor(c.status)}">${formatStatus(c.status)}</span></td>
            <td>${escapeHtml(formatUser(c.assignedTo) || 'Unassigned')}</td>
            <td>${formatDate(c.createdAt)}</td>
            <td>
                <a href="/complaint-detail.html?id=${c.id}" class="btn btn-sm btn-outline-primary">
                    <i class="bi bi-eye me-1"></i>View
                </a>
            </td>
        </tr>
    `).join('');
}

function renderPagination(pageData) {
    const container = document.getElementById('pagination');
    if (pageData.totalPages <= 1) {
        container.innerHTML = `<span class="text-muted">${pageData.totalElements} complaint(s)</span>`;
        return;
    }

    let paginationHtml = '<nav><ul class="pagination">';
    
    paginationHtml += `
        <li class="page-item ${pageData.first ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="goToPage(${pageData.page - 1}); return false;">Previous</a>
        </li>
    `;

    for (let i = 0; i < pageData.totalPages; i++) {
        paginationHtml += `
            <li class="page-item ${i === pageData.page ? 'active' : ''}">
                <a class="page-link" href="#" onclick="goToPage(${i}); return false;">${i + 1}</a>
            </li>
        `;
    }

    paginationHtml += `
        <li class="page-item ${pageData.last ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="goToPage(${pageData.page + 1}); return false;">Next</a>
        </li>
    `;

    paginationHtml += '</ul></nav>';
    paginationHtml += `<span class="text-muted ms-3">Page ${pageData.page + 1} of ${pageData.totalPages} (${pageData.totalElements} total)</span>`;
    
    container.innerHTML = paginationHtml;
}

function goToPage(page) {
    currentPage = page;
    loadComplaints();
}

function formatStatus(status) {
    if (!status) return '—';
    return status.replace(/_/g, ' ').replace(/\b\w/g, c => c.toUpperCase());
}

function getStatusColor(status) {
    const colors = {
        'SUBMITTED': 'primary',
        'ASSIGNED': 'info',
        'IN_PROGRESS': 'warning',
        'RESOLVED': 'success',
        'CLOSED': 'secondary',
        'REJECTED': 'danger'
    };
    return colors[status] || 'secondary';
}

function getPriorityColor(priority) {
    const colors = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'dark'
    };
    return colors[priority?.toUpperCase()] || 'secondary';
}

function formatUser(user) {
    if (!user) return '';
    return user.firstName + ' ' + user.lastName;
}

function formatDate(dateStr) {
    if (!dateStr) return '—';
    return new Date(dateStr).toLocaleDateString(undefined, {
        year: 'numeric', month: 'short', day: 'numeric'
    });
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text || '';
    return div.innerHTML;
}