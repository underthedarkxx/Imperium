/* SPA + Panel full implementation 
   - painel lateral com overlay
   - editar a partir do painel
   - reset de senha via modal
   - ativar/desativar
   - pushState para controlar fechar com "voltar"
*/

const PAGE_SIZE = 5;
let currentView = 'list';
let currentPage = 1;
let currentQuery = '';
let currentProfileId = null;

// Helpers de persistência
function getFuncs() { const raw = localStorage.getItem('funcionarios'); return raw ? JSON.parse(raw) : seedData(); }
function saveFuncs(arr) { localStorage.setItem('funcionarios', JSON.stringify(arr)); }

async function loadUsuarios() {
    try {
        const response = await fetch('http://localhost:8080/api/usuarios/listar');
        const usuarios = await response.json();

        const tableBody = document.getElementById('usuarioTableBody');
        tableBody.innerHTML = ''; // clear previous rows

        usuarios.forEach(usuario => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${usuario.id}</td>
                <td>${usuario.login}</td>
            `;
            tableBody.appendChild(row);
        });
    } catch (err) {
        console.error('Erro ao carregar usuários', err);
    }
}

// ADD
async function submitAdd(ev) {
    ev.preventDefault();
    const payload = {
        nome: document.getElementById('addNome').value.trim(),
        email: document.getElementById('addEmail').value.trim(),
        telefone: document.getElementById('addTelefone').value.trim(),
        senha: document.getElementById('addSenha').value,
        cargo: document.getElementById('addCargo').value
    };
    try {
        await fetch('http://localhost:8080/api/usuarios', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        currentPage = 1;
        currentQuery = '';
        document.getElementById('searchInput').value = '';
        navigateTo('list');
    } catch(err) { console.error(err); alert('Erro ao adicionar usuário'); }
}

// EDIT
async function submitEdit(ev) {
    ev.preventDefault();
    const id = document.getElementById('editId').value;
    const payload = {
        nome: document.getElementById('editNome').value.trim(),
        email: document.getElementById('editEmail').value.trim(),
        telefone: document.getElementById('editTelefone').value.trim(),
        senha: document.getElementById('editSenha').value,
        cargo: document.getElementById('editCargo').value
    };
    try {
        await fetch(`http://localhost:8080/api/usuarios/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        navigateTo('list');
    } catch(err) { console.error(err); alert('Erro ao editar usuário'); }
}
// DELETE & RESET (quick)
async function onDelete(id) {
    if (!confirm('Tem certeza que deseja deletar?')) return;
    try {
        await fetch(`http://localhost:8080/api/usuarios/${id}`, { method: 'DELETE' });
        closeSidePanel();
        renderList();
    } catch(err) { console.error(err); alert('Erro ao deletar usuário'); }
}

