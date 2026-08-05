document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }

    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', handleRegister);
    }

    const forgotForm = document.getElementById('forgotForm');
    if (forgotForm) {
        forgotForm.addEventListener('submit', handleForgotPassword);
    }

    const resetForm = document.getElementById('resetForm');
    if (resetForm) {
        const params = new URLSearchParams(window.location.search);
        const token = params.get('token');
        if (token) {
            document.getElementById('resetToken').value = token;
        }
        resetForm.addEventListener('submit', handleResetPassword);
    }
});

async function handleLogin(e) {
    e.preventDefault();
    hideAlert('alert');
    setFormLoading(e.target, true);

    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;

    try {
        const response = await apiRequest('/api/auth/login', {
            method: 'POST',
            body: JSON.stringify({ email, password })
        });

        setTokens(response.data.accessToken, response.data.refreshToken);
        saveUser(response.data.user);
        window.location.href = '/profile.html';
    } catch (err) {
        showAlert('alert', err.message);
    } finally {
        setFormLoading(e.target, false);
    }
}

async function handleRegister(e) {
    e.preventDefault();
    hideAlert('alert');
    setFormLoading(e.target, true);

    const payload = {
        email: document.getElementById('email').value.trim(),
        password: document.getElementById('password').value,
        firstName: document.getElementById('firstName').value.trim(),
        lastName: document.getElementById('lastName').value.trim(),
        phone: document.getElementById('phone').value.trim() || null
    };

    try {
        const response = await apiRequest('/api/auth/register', {
            method: 'POST',
            body: JSON.stringify(payload)
        });

        setTokens(response.data.accessToken, response.data.refreshToken);
        saveUser(response.data.user);
        window.location.href = '/profile.html';
    } catch (err) {
        showAlert('alert', err.message);
    } finally {
        setFormLoading(e.target, false);
    }
}

async function handleForgotPassword(e) {
    e.preventDefault();
    hideAlert('alert');
    setFormLoading(e.target, true);

    const email = document.getElementById('email').value.trim();

    try {
        await apiRequest('/api/auth/forgot-password', {
            method: 'POST',
            body: JSON.stringify({ email })
        });
        showAlert('alert', 'If the email exists, a reset link has been sent. Check server logs if mail is not configured.', 'success');
    } catch (err) {
        showAlert('alert', err.message);
    } finally {
        setFormLoading(e.target, false);
    }
}

async function handleResetPassword(e) {
    e.preventDefault();
    hideAlert('alert');
    setFormLoading(e.target, true);

    const token = document.getElementById('resetToken').value.trim();
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (newPassword !== confirmPassword) {
        showAlert('alert', 'Passwords do not match');
        setFormLoading(e.target, false);
        return;
    }

    try {
        await apiRequest('/api/auth/reset-password', {
            method: 'POST',
            body: JSON.stringify({ token, newPassword })
        });
        showAlert('alert', 'Password reset successfully. Redirecting to login...', 'success');
        setTimeout(() => { window.location.href = '/index.html'; }, 2000);
    } catch (err) {
        showAlert('alert', err.message);
    } finally {
        setFormLoading(e.target, false);
    }
}
