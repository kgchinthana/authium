// Get URL parameters
const urlParams = new URLSearchParams(window.location.search);
const token = urlParams.get('token');

// Check if token is present in URL
if (!token) {
    document.getElementById('error-message').textContent = 'Invalid or missing token.';
    document.getElementById('error-message').style.display = 'block';
}

const form = document.getElementById('resetForm');
const newPasswordInput = document.getElementById('newPassword');
const confirmPasswordInput = document.getElementById('confirmPassword');
const errorMessage = document.getElementById('error-message');
const successMessage = document.getElementById('success-message');

// Handle form submission
form.addEventListener('submit', async (e) => {
    e.preventDefault();

    // Check if passwords match
    if (newPasswordInput.value !== confirmPasswordInput.value) {
        errorMessage.textContent = 'Passwords do not match.';
        errorMessage.style.display = 'block';
        return;
    }

    // Prepare the request payload
    const resetRequest = {
        token: token,
        email: 'user@example.com', // Replace with dynamic email if needed
        newPassword: newPasswordInput.value
    };

    try {
        // Make the password reset request
        const response = await fetch('/api/authenticate/auth/reset-password', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(resetRequest)
        });

        const data = await response.json();

        if (response.ok) {
            successMessage.textContent = 'Your password has been reset successfully!';
            successMessage.style.display = 'block';
            errorMessage.style.display = 'none';
        } else {
            errorMessage.textContent = data.message || 'An error occurred. Please try again.';
            errorMessage.style.display = 'block';
            successMessage.style.display = 'none';
        }
    } catch (error) {
        errorMessage.textContent = 'Failed to reset password. Please try again later.';
        errorMessage.style.display = 'block';
        successMessage.style.display = 'none';
    }
});
