const API_BASE = '';

function getToken() {
    return localStorage.getItem('accessToken');
}

function setTokens(accessToken, refreshToken) {
    localStorage.setItem('accessToken', accessToken);
    if (refreshToken) {
        localStorage.setItem('refreshToken', refreshToken);
    }
}

function clearTokens() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
}

function saveUser(user) {
    localStorage.setItem('user', JSON.stringify(user));
}

function getUser() {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
}

function requireAuth() {
    if (!getToken()) {
        window.location.href = '/index.html';
        return false;
    }
    return true;
}

async function apiRequest(url, options = {}) {
    const headers = {
        'Content-Type': 'application/json',
        ...(options.headers || {})
    };

    const token = getToken();
    if (token) {
        headers['Authorization'] = 'Bearer ' + token;
    }

    const response = await fetch(API_BASE + url, {
        ...options,
        headers
    });

    const data = await response.json().catch(() => ({}));

    if (!response.ok) {
        let message = data.message || 'Request failed';
        if (data.data && typeof data.data === 'object') {
            const fieldErrors = Object.values(data.data).join(', ');
            if (fieldErrors) message = fieldErrors;
        }
        throw new Error(message);
    }

    return data;
}

function showAlert(elementId, message, type = 'error') {
    const el = document.getElementById(elementId);
    if (!el) return;
    el.textContent = message;
    el.className = 'alert show alert-' + type;
    el.style.display = 'block';
}

function hideAlert(elementId) {
    const el = document.getElementById(elementId);
    if (!el) return;
    el.className = 'alert';
    el.textContent = '';
    el.style.display = 'none';
}

function setFormLoading(form, loading) {
    if (loading) {
        form.classList.add('loading');
    } else {
        form.classList.remove('loading');
    }
}

function logout() {
    clearTokens();
    window.location.href = '/index.html';
}

// Toast notification system
function showToast(message, type = 'success') {
    const toastEl = document.getElementById('toast');
    const toastIcon = document.getElementById('toastIcon');
    const toastTitle = document.getElementById('toastTitle');
    const toastMessage = document.getElementById('toastMessage');

    if (!toastEl) return;

    // Set icon based on type
    const icons = {
        success: 'bi-check-circle-fill text-success',
        error: 'bi-exclamation-circle-fill text-danger',
        warning: 'bi-exclamation-triangle-fill text-warning',
        info: 'bi-info-circle-fill text-info'
    };

    const titles = {
        success: 'Success',
        error: 'Error',
        warning: 'Warning',
        info: 'Information'
    };

    toastIcon.className = 'bi me-2 ' + (icons[type] || icons.info);
    toastTitle.textContent = titles[type] || 'Notification';
    toastMessage.textContent = message;

    const toast = new bootstrap.Toast(toastEl);
    toast.show();
}

// Loading spinner toggle
function toggleLoadingSpinner(show, elementId = 'loadingSpinner') {
    const spinner = document.getElementById(elementId);
    if (spinner) {
        spinner.classList.toggle('d-none', !show);
    }
}

// Button loading state
function setButtonLoading(buttonId, loading, originalText = 'Submit') {
    const button = document.getElementById(buttonId);
    const spinner = document.getElementById(buttonId + 'Spinner');
    const buttonText = document.getElementById(buttonId + 'Text');

    if (button) {
        button.disabled = loading;
    }

    if (spinner) {
        spinner.classList.toggle('d-none', !loading);
    }

    if (buttonText) {
        buttonText.textContent = loading ? 'Loading...' : originalText;
    }
}
