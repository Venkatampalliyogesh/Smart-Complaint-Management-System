document.addEventListener('DOMContentLoaded', () => {
    if (document.getElementById('profileView')) {
        if (!requireAuth()) return;
        loadProfile();
        setupProfileForm();
    }

    if (document.getElementById('changePasswordForm')) {
        if (!requireAuth()) return;
        document.getElementById('changePasswordForm').addEventListener('submit', handleChangePassword);
    }

    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', logout);
    }
});

async function loadProfile() {
    try {
        const response = await apiRequest('/api/profile/me');
        const profile = response.data;
        renderProfile(profile);
        populateProfileForm(profile);
    } catch (err) {
        showAlert('alert', err.message);
        if (err.message.includes('401') || err.message.includes('Unauthorized')) {
            logout();
        }
    }
}

function renderProfile(profile) {
    // Update profile header
    const profileName = document.getElementById('profileName');
    const profileEmail = document.getElementById('profileEmail');
    const profileRole = document.getElementById('profileRole');
    const profileAvatar = document.getElementById('profileAvatar');
    
    if (profileName) {
        profileName.textContent = `${profile.firstName} ${profile.lastName}`;
    }
    
    if (profileEmail) {
        profileEmail.textContent = profile.email;
    }
    
    if (profileRole) {
        const roles = (profile.roles || []).map(role => role.name || role).join(', ');
        profileRole.innerHTML = `<span class="badge badge-primary">${roles}</span>`;
    }
    
    if (profileAvatar) {
        const initials = `${profile.firstName.charAt(0)}${profile.lastName.charAt(0)}`.toUpperCase();
        profileAvatar.textContent = initials;
    }
    
    // Update profile view in card
    const viewFirstName = document.getElementById('viewFirstName');
    const viewLastName = document.getElementById('viewLastName');
    const viewEmail = document.getElementById('viewEmail');
    const viewPhone = document.getElementById('viewPhone');
    const viewCreatedAt = document.getElementById('viewCreatedAt');
    
    if (viewFirstName) viewFirstName.textContent = profile.firstName;
    if (viewLastName) viewLastName.textContent = profile.lastName;
    if (viewEmail) viewEmail.textContent = profile.email;
    if (viewPhone) viewPhone.textContent = profile.phone || '—';
    if (viewCreatedAt) {
        const date = new Date(profile.createdAt);
        viewCreatedAt.textContent = date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });
    }
}

function populateProfileForm(profile) {
    const firstName = document.getElementById('firstName');
    const lastName = document.getElementById('lastName');
    const phone = document.getElementById('phone');
    if (firstName) firstName.value = profile.firstName || '';
    if (lastName) lastName.value = profile.lastName || '';
    if (phone) phone.value = profile.phone || '';
}

function setupProfileForm() {
    const form = document.getElementById('profileForm');
    if (!form) return;
    form.addEventListener('submit', handleUpdateProfile);
}

async function handleUpdateProfile(e) {
    e.preventDefault();
    hideAlert('alert');
    setFormLoading(e.target, true);

    const payload = {
        firstName: document.getElementById('firstName').value.trim(),
        lastName: document.getElementById('lastName').value.trim(),
        phone: document.getElementById('phone').value.trim() || null
    };

    try {
        const response = await apiRequest('/api/profile/me', {
            method: 'PUT',
            body: JSON.stringify(payload)
        });
        saveUser(response.data);
        renderProfile(response.data);
        showAlert('alert', 'Profile updated successfully', 'success');
    } catch (err) {
        showAlert('alert', err.message);
    } finally {
        setFormLoading(e.target, false);
    }
}

async function handleChangePassword(e) {
    e.preventDefault();
    hideAlert('alert');
    setFormLoading(e.target, true);

    const currentPassword = document.getElementById('currentPassword').value;
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (newPassword !== confirmPassword) {
        showAlert('alert', 'New passwords do not match');
        setFormLoading(e.target, false);
        return;
    }

    try {
        await apiRequest('/api/profile/change-password', {
            method: 'PUT',
            body: JSON.stringify({ currentPassword, newPassword })
        });
        showAlert('alert', 'Password changed successfully. Please login again.', 'success');
        setTimeout(logout, 2000);
    } catch (err) {
        showAlert('alert', err.message);
    } finally {
        setFormLoading(e.target, false);
    }
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}
