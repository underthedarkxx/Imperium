// js/api.js

// Centralizamos a URL base da API para facilitar a manutenção.
const API_BASE_URL = 'http://localhost:8080/api';

/**
 * Função para realizar o login na API.
 * @param {string} login - O nome de usuário.
 * @param {string} senha - A senha do usuário.
 * @returns {Promise<any>} - A promessa com os dados da resposta.
 */
async function login(login, senha) {
    const response = await fetch(`${API_BASE_URL}/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ login, senha })
    });

    // PRIMEIRO, checamos se a requisição falhou (status 4xx ou 5xx)
    if (!response.ok) {
        // Se falhou, não tentamos ler o JSON.
        // Lançamos um erro com uma mensagem amigável.
        // O status 401 ou 403 geralmente significa credenciais inválidas.
        if (response.status === 401 || response.status === 403) {
            throw new Error('Usuário ou senha inválidos.');
        } else {
            // Para outros erros de servidor (como 500)
            throw new Error('Falha na comunicação com o servidor. Tente novamente mais tarde.');
        }
    }

    // SOMENTE SE a requisição foi um sucesso (status 2xx), tentamos ler o JSON.
    const data = await response.json();

    if (data.token) {
        localStorage.setItem('jwt_token', data.token);
    }
    
    return data;
}

// Futuramente, você pode adicionar outras funções aqui:
/*
async function getFuncoes() {
    const token = localStorage.getItem('jwt_token');
    const response = await fetch(`${API_BASE_URL}/funcoes`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    // ...
}
*/