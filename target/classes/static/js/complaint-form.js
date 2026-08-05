document.addEventListener('DOMContentLoaded', () => {
    if (!requireAuth()) return;

    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) logoutBtn.addEventListener('click', logout);

    const form = document.getElementById('complaintForm');
    if (form) {
        form.addEventListener('submit', handleSubmit);
        loadLookups();
    }
});

async function loadLookups() {
    try {
        const [categoriesRes, prioritiesRes] = await Promise.all([
            apiRequest('/api/categories'),
            apiRequest('/api/priorities')
        ]);

        const categorySelect = document.getElementById('categoryId');
        categoriesRes.data.forEach(cat => {
            const option = document.createElement('option');
            option.value = cat.id;
            option.textContent = cat.name;
            categorySelect.appendChild(option);
        });

        const prioritySelect = document.getElementById('priorityId');
        prioritiesRes.data.forEach(pri => {
            const option = document.createElement('option');
            option.value = pri.id;
            option.textContent = pri.name;
            prioritySelect.appendChild(option);
        });
    } catch (err) {
        showToast(err.message, 'error');
    }
}

async function handleSubmit(e) {
    e.preventDefault();
    hideAlert('alert');
    
    const form = e.target;
    if (!form.checkValidity()) {
        e.stopPropagation();
        form.classList.add('was-validated');
        return;
    }

    setButtonLoading('submit', true, 'Submit Complaint');

    const payload = {
        title: document.getElementById('title').value.trim(),
        description: document.getElementById('description').value.trim(),
        categoryId: parseInt(document.getElementById('categoryId').value, 10),
        priorityId: parseInt(document.getElementById('priorityId').value, 10)
    };

    try {
        const response = await apiRequest('/api/complaints', {
            method: 'POST',
            body: JSON.stringify(payload)
        });
        showToast('Complaint submitted! Ticket: ' + response.data.ticketNumber, 'success');
        setTimeout(() => {
            window.location.href = '/complaint-detail.html?id=' + response.data.id;
        }, 1500);
    } catch (err) {
        showToast(err.message, 'error');
    } finally {
        setButtonLoading('submit', false, 'Submit Complaint');
    }
}
