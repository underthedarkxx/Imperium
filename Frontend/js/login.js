// js/login.js

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');

    loginForm.addEventListener('submit', async (event) => {
        event.preventDefault(); // Impede o recarregamento da página

        const username = usernameInput.value;
        const password = passwordInput.value;

        try {
            const data = await login(username, password);

            alert(data.message || 'Login bem-sucedido!');
            window.location.href = '/dashboard.html'; // Mude para a sua página de dashboard

        } catch (error) {
            console.error('Falha no login:', error);
            alert(error.message); // Exibe a mensagem de erro vinda da API
        }
    });
});