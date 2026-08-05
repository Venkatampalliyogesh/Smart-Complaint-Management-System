let currentPage = 0;
const pageSize = 10;

document.addEventListener('DOMContentLoaded', () => {
    if (!requireAuth()) return;

    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) logoutBtn.addEventListener('click', logout);

    document.getElementById('applyFiltersBtn').addEventListener('click', () => {
        currentPage = 0;
        loadUsers();
    });

    document.getElementById('searchInput').addEventListener('keydown', (e) => {
        if (e.key === 'Enter') {
            currentPage = 0;
            loadUsers();
        }
    });

    loadUsers();
});

async function loadUsers() {
    toggleLoadingSpinner(true);
    hideAlert('alert');

    try {
        const response = await apiRequest('/api/admin/users?page=' + currentPage + '&size=' + pageSize);
        renderUsers(response.data.content);
        renderPagination(response.data);
    } catch (err) {
        showToast(err.message, 'error');
        document.getElementById('usersTableBody').innerHTML =
            '<tr><td colspan="7" class="text-center text-muted">Failed to load users</td></tr>';
    } finally {
        toggleLoadingSpinner(false);
    }
}

function renderUsers(users) {
    const tbody = document.getElementById('usersTableBody');

    if (!users || users.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center text-muted">No users found</td></tr>';
        return;
    }

    tbody.innerHTML = users.map(u => `
        <tr>
            <td>${escapeHtml(u.firstName + ' ' + u.lastName)}</td>
            <td>${escapeHtml(u.email)}</td>
            <td>${escapeHtml(u.phone || '—')}</td>
            <td>${formatRoles(u.roles)}</td>
            <td>
                <span class="badge ${u.active ? 'bg-success' : 'bg-danger'}">
                    ${u.active ? 'Active' : 'Inactive'}
                </span>
            </td>
            <td>${formatDate(u.createdAt)}</td>
            <td>
                <button onclick="toggleStatus(${u.id}, ${!u.active})" class="btn btn-sm ${u.active ? 'btn-outline-danger' : 'btn-outline-success'}">
                    <i class="bi bi-${u.active ? 'person-x' : 'person-check'} me-1"></i>
                    ${u.active ? 'Deactivate' : 'Activate'}
                </button>
            </td>
        </tr>
    `).join('');
}

function renderPagination(pageData) {
    const container = document.getElementById('pagination');
    if (pageData.totalPages <= 1) {
        container.innerHTML = `<span class="text-muted">${pageData.totalElements} user(s)</span>`;
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
    loadUsers();
}

async function toggleStatus(userId, newStatus) {
    hideAlert('alert');
    try {
        await apiRequest('/api/admin/users/' + userId + '/status?active=' + newStatus, {
            method: 'PATCH'
        });
        showToast('User status updated successfully', 'success');
        loadUsers();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

function formatRoles(roles) {
    if (!roles || roles.length === 0) return '—';
    return roles.map(role => role.replace('ROLE_', '')).join(', ');
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