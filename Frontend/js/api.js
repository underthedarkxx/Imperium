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
        body: JSON.stringify({ login, senha }) // Usando shorthand { login, senha }
    });

    const data = await response.json();

    if (!response.ok) {
        // Se a resposta não for OK, lança um erro com a mensagem do backend.
        throw new Error(data.message || 'Erro ao tentar fazer login.');
    }

    // Se o login for bem-sucedido e o backend retornar um token...
    if (data.token) {
        // Armazenamos o token no localStorage do navegador para usar depois.
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