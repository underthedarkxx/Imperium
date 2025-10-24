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
function uid() { return Date.now().toString(36) + Math.random().toString(36).slice(2,8); }
function getFuncs() { const raw = localStorage.getItem('funcionarios'); return raw ? JSON.parse(raw) : seedData(); }
function saveFuncs(arr) { localStorage.setItem('funcionarios', JSON.stringify(arr)); }

function seedData() {
  const sample = [
    {id: uid(), nome: 'ana silva', email: 'ana@example.com', telefone: '27999990001', senha:'123456', cargo:'relacionamento', ativo:true},
    {id: uid(), nome: 'bruno costa', email: 'bruno@example.com', telefone: '27999990002', senha:'123456', cargo:'recrutamento', ativo:true},
    {id: uid(), nome: 'carla melo', email: 'carla@example.com', telefone: '27999990003', senha:'123456', cargo:'vendas', ativo:true},
    {id: uid(), nome: 'diego alves', email: 'diego@example.com', telefone: '27999990004', senha:'123456', cargo:'admin', ativo:true},
    {id: uid(), nome: 'eduarda ribeiro', email: 'eduarda@example.com', telefone: '27999990005', senha:'123456', cargo:'vendas', ativo:true},
    {id: uid(), nome: 'felipe moreira', email: 'felipe@example.com', telefone: '27999990006', senha:'123456', cargo:'relacionamento', ativo:true},
    {id: uid(), nome: 'gabriela costa', email: 'gabi@example.com', telefone: '27999990007', senha:'123456', cargo:'recrutamento', ativo:true},
    {id: uid(), nome: 'henrique luiz', email: 'henrique@example.com', telefone: '27999990008', senha:'123456', cargo:'admin', ativo:true},
    {id: uid(), nome: 'isadora luz', email: 'isadora@example.com', telefone: '27999990009', senha:'123456', cargo:'vendas', ativo:true},
    {id: uid(), nome: 'joao pedro', email: 'joao@example.com', telefone: '27999990010', senha:'123456', cargo:'relacionamento', ativo:true}
  ];
  saveFuncs(sample);
  return sample;
}

// Navegação de views
function showView(id) {
  document.querySelectorAll('.view').forEach(v => { v.classList.remove('active-view'); v.style.display = 'none'; });
  const el = document.getElementById('view-' + id);
  if (!el) return console.warn('view não encontrada:', id);
  el.style.display = 'block';
  el.classList.add('active-view');
  currentView = id;
  if (id === 'list') renderList();
  else if (id === 'add') document.getElementById('formAdd').reset();
}
function navigateTo(view, payload) {
  if (view === 'edit') loadEdit(payload);
  else if (view === 'profile') loadProfile(payload);
  else showView(view);
  history.pushState({view, payload}, '', '#' + view + (payload ? ('|' + payload) : ''));
}
window.onpopstate = (ev) => {
  const state = ev.state;
  if (!state) { closeSidePanel(); showView('list'); return; }
  if (state.view === 'profile') {
    if (state.payload) loadProfile(state.payload, true);
    else closeSidePanel();
  } else if (state.view === 'edit') loadEdit(state.payload, true);
  else showView(state.view);
};

// Lista & paginação
function onSearchInput() { currentQuery = document.getElementById('searchInput').value.trim().toLowerCase(); currentPage = 1; renderList(); }
function filterFuncs(all, q) {
  if (!q) return all;
  return all.filter(f => (f.nome||'').toLowerCase().includes(q) || (f.email||'').toLowerCase().includes(q) || (f.cargo||'').toLowerCase().includes(q));
}
function renderList() {
  const all = getFuncs();
  const filtered = filterFuncs(all, currentQuery);
  const total = filtered.length;
  const totalPages = Math.max(1, Math.ceil(total / PAGE_SIZE));
  if (currentPage > totalPages) currentPage = totalPages;
  const start = (currentPage - 1) * PAGE_SIZE;
  const pageItems = filtered.slice(start, start + PAGE_SIZE);

  const tbody = document.getElementById('funcTbody');
  tbody.innerHTML = '';
  if (pageItems.length === 0) {
    tbody.innerHTML = `<tr><td colspan="5" class="text-center">Nenhum funcionario cadastrado.</td></tr>`;
  } else {
    for (const f of pageItems) {
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td><a href="#" onclick="navigateTo('profile','${f.id}'); return false;">${escapeHtml(f.nome)}</a></td>
        <td>${escapeHtml(f.email)}</td>
        <td>${escapeHtml(f.telefone)}</td>
        <td>${escapeHtml(f.cargo)}</td>
        <td>
          <button class="btn btn-sm btn-warning btn-xs" onclick="navigateTo('edit','${f.id}')">Editar</button>
          <button class="btn btn-sm btn-danger btn-xs" onclick="onDelete('${f.id}')">Deletar</button>
          <button class="btn btn-sm btn-secondary btn-xs" onclick="onResetPassword('${f.id}')">Redefinir Senha</button>
        </td>
      `;
      tbody.appendChild(tr);
    }
  }
  renderPagination(totalPages);
}
function renderPagination(totalPages) {
  const ul = document.getElementById('pagination');
  ul.innerHTML = '';
  function addLi(content, active=false, disabled=false, onclick=null) {
    const li = document.createElement('li');
    li.className = 'page-item' + (active ? ' active' : '') + (disabled ? ' disabled' : '');
    const a = document.createElement('a');
    a.className = 'page-link';
    a.innerHTML = content;
    if (!disabled && onclick) a.onclick = onclick;
    li.appendChild(a);
    ul.appendChild(li);
  }
  if (currentPage > 1) addLi('Anterior', false, false, () => { currentPage--; renderList(); });
  else addLi('Anterior', false, true, null);
  if (totalPages <= 7) {
    for (let p = 1; p <= totalPages; p++) addLi(p, p === currentPage, false, () => { currentPage = p; renderList(); });
  } else {
    addLi(1, currentPage === 1, false, () => { currentPage = 1; renderList(); });
    if (currentPage > 4) addLi('...', false, true, null);
    for (let p = currentPage - 2; p <= currentPage + 2; p++) if (p > 1 && p < totalPages) addLi(p, p === currentPage, false, () => { currentPage = p; renderList(); });
    if (currentPage < totalPages - 3) addLi('...', false, true, null);
    addLi(totalPages, currentPage === totalPages, false, () => { currentPage = totalPages; renderList(); });
  }
  if (currentPage < totalPages) addLi('Próxima', false, false, () => { currentPage++; renderList(); });
  else addLi('Próxima', false, true, null);
}

// ADD
function submitAdd(ev) {
  ev.preventDefault();
  const nome = document.getElementById('addNome').value.trim();
  const email = document.getElementById('addEmail').value.trim();
  const telefone = document.getElementById('addTelefone').value.trim();
  const senha = document.getElementById('addSenha').value;
  const cargo = document.getElementById('addCargo').value;
  const all = getFuncs();
  const novo = { id: uid(), nome, email, telefone, senha, cargo, ativo:true };
  all.unshift(novo);
  saveFuncs(all);
  currentPage = 1; currentQuery = ''; document.getElementById('searchInput').value = '';
  navigateTo('list');
}

// EDIT
function loadEdit(id, restoring=false) {
  const all = getFuncs();
  const f = all.find(x => x.id === id);
  if (!f) { alert('Funcionário não encontrado.'); navigateTo('list'); return; }
  document.getElementById('editId').value = f.id;
  document.getElementById('editNome').value = f.nome || '';
  document.getElementById('editEmail').value = f.email || '';
  document.getElementById('editTelefone').value = f.telefone || '';
  document.getElementById('editSenha').value = f.senha || '';
  document.getElementById('editCargo').value = f.cargo || 'relacionamento';
  showView('edit');
  if (!restoring) history.pushState({view:'edit', payload:id}, '', '#edit|' + id);
}
function submitEdit(ev) {
  ev.preventDefault();
  const id = document.getElementById('editId').value;
  const nome = document.getElementById('editNome').value.trim();
  const email = document.getElementById('editEmail').value.trim();
  const telefone = document.getElementById('editTelefone').value.trim();
  const senha = document.getElementById('editSenha').value;
  const cargo = document.getElementById('editCargo').value;
  const all = getFuncs();
  const idx = all.findIndex(x => x.id === id);
  if (idx === -1) { alert('Funcionário não encontrado.'); return; }
  all[idx] = { id, nome, email, telefone, senha, cargo, ativo: all[idx].ativo !== false };
  saveFuncs(all);
  navigateTo('list');
}

// DELETE & RESET (quick)
function onDelete(id) {
  if (!id) return;
  if (!confirm('Tem certeza que deseja deletar?')) return;
  let all = getFuncs();
  all = all.filter(x => x.id !== id);
  saveFuncs(all);
  // se o painel estava aberto para esse id, fecha
  if (currentProfileId === id) closeSidePanel();
  renderList();
}
function onResetPassword(id) {
  // abre modal e pré-configura target id
  if (!id) return;
  currentProfileId = id;
  openResetPasswordModal();
}

// PROFILE PANEL
const overlay = document.getElementById('overlay');
const sidePanel = document.getElementById('sidePanel');
const resetPwdModal = new bootstrap.Modal(document.getElementById('resetPwdModal'), { keyboard: true });

function loadProfile(id, restoring=false) {
  const all = getFuncs();
  const f = all.find(x => x.id === id);
  if (!f) { alert('Funcionário não encontrado.'); return; }
  currentProfileId = id;

  document.getElementById('panelTitle').textContent = capitalize(f.nome || 'Perfil');
  document.getElementById('panelSubtitle').textContent = `Cargo: ${f.cargo || '-'}`;
  document.getElementById('profileNome').textContent = f.nome || '-';
  document.getElementById('profileEmail').textContent = f.email || '-';
  document.getElementById('profileTelefone').textContent = f.telefone || '-';
  document.getElementById('profileCargo').textContent = f.cargo || '-';
  document.getElementById('profileStatus').textContent = f.ativo === false ? 'Inativo' : 'Ativo';

  // Edit button from header
  document.getElementById('editFromPanelBtn').onclick = () => editFromPanel();

  // Toggle active button
  const toggleActiveBtn = document.getElementById('toggleActiveBtn');
  toggleActiveBtn.onclick = () => toggleActive(f.id);
  toggleActiveBtn.innerHTML = f.ativo === false ? '<i class="bi bi-check-circle"></i> Reativar' : '<i class="bi bi-slash-circle"></i> Desativar';

  // Montar abas: A,B,C,E,G,H
  const tabs = document.getElementById('profileTabs');
  const content = document.getElementById('profileTabContent');
  tabs.innerHTML = ''; content.innerHTML = '';

  addProfileTab('tab-dados', 'Dados pessoais', `
    <dl class="row">
      <dt class="col-sm-4">Nome</dt><dd class="col-sm-8">${escapeHtml(f.nome || '-')}</dd>
      <dt class="col-sm-4">CPF</dt><dd class="col-sm-8">${escapeHtml(f.cpf || '—')}</dd>
      <dt class="col-sm-4">Nascimento</dt><dd class="col-sm-8">${escapeHtml(f.nascimento || '—')}</dd>
    </dl>
  `, true);

  addProfileTab('tab-cargo', 'Cargo & Setor', `
    <p><strong>Cargo:</strong> ${escapeHtml(f.cargo || '—')}</p>
    <p><strong>Setor / Unidade:</strong> ${escapeHtml(f.setor || '—')}</p>
  `);

  addProfileTab('tab-contato', 'Contato', `
    <p><strong>Email:</strong> ${escapeHtml(f.email || '—')}</p>
    <p><strong>Telefone:</strong> ${escapeHtml(f.telefone || '—')}</p>
    <p><strong>Endereço:</strong> ${escapeHtml(f.endereco || '—')}</p>
  `);

  addProfileTab('tab-historico', 'Histórico', `
    <p>Últimas ações (exemplo):</p>
    <ul>
      <li>01/10/2025 - Acesso ao sistema</li>
      <li>09/09/2025 - Atualização de dados</li>
      <li>02/08/2025 - Participou do treinamento X</li>
    </ul>
  `);

  addProfileTab('tab-permissoes', 'Permissões', `
    <p><strong>Nível de acesso:</strong> ${f.cargo === 'admin' ? 'Administrador' : 'Usuário padrão'}</p>
    <p><strong>Papéis:</strong> ${f.cargo === 'recrutamento' ? 'Recrutador' : (f.cargo === 'vendas' ? 'Vendedor' : 'Atendente')}</p>
  `);

  addProfileTab('tab-config', 'Configurações', `
    <p><button class="btn btn-sm btn-outline-primary" onclick="openResetPasswordModal()">Redefinir senha</button></p>
    <p><button class="btn btn-sm btn-outline-secondary" onclick="editFromPanel()">Editar perfil</button></p>
  `);

  openSidePanel();

  if (!restoring) history.pushState({view:'profile', payload:id}, '', '#profile|' + id);
}

// adiciona uma aba e pane — função completa (corrigida)
function addProfileTab(id, title, htmlContent, active=false) {
  const tabs = document.getElementById('profileTabs');
  const content = document.getElementById('profileTabContent');

  const li = document.createElement('li');
  li.className = 'nav-item';
  const btn = document.createElement('button');
  btn.className = 'nav-link' + (active ? ' active' : '');
  btn.type = 'button';
  btn.setAttribute('data-bs-toggle', 'pill');
  btn.setAttribute('aria-controls', id);
  btn.onclick = () => {
    // controla classes manualmente
    tabs.querySelectorAll('.nav-link').forEach(n => n.classList.remove('active'));
    btn.classList.add('active');
    content.querySelectorAll('.tab-pane').forEach(p => p.classList.remove('show','active'));
    pane.classList.add('show','active');
  };
  btn.innerText = title;
  li.appendChild(btn);
  tabs.appendChild(li);

  const pane = document.createElement('div');
  pane.className = 'tab-pane fade' + (active ? ' show active' : '');
  pane.id = id;
  pane.innerHTML = htmlContent;
  content.appendChild(pane);
}

function openSidePanel() {
  overlay.classList.add('show');
  sidePanel.classList.add('open');
  sidePanel.setAttribute('aria-hidden','false');
  // focus for accessibility
  const closeBtn = sidePanel.querySelector('.btn-danger');
  if (closeBtn) closeBtn.focus();
}
function closeSidePanel() {
  overlay.classList.remove('show');
  sidePanel.classList.remove('open');
  sidePanel.setAttribute('aria-hidden','true');
  currentProfileId = null;
  // voltar à lista
  showView('list');
  // push a state without profile so back/forward stays consistent
  history.pushState({}, '', '#');
}

// editar a partir do painel
function editFromPanel() {
  if (!currentProfileId) return;
  loadEdit(currentProfileId);
  closeSidePanel();
}

// alterna ativo/inativo
function toggleActive(id) {
  const all = getFuncs();
  const idx = all.findIndex(x => x.id === id);
  if (idx === -1) return alert('Funcionário não encontrado.');
  all[idx].ativo = !all[idx].ativo;
  saveFuncs(all);
  loadProfile(id, true); // atualizar painel
  renderList();
}

// Reset password modal handlers
function openResetPasswordModal() {
  if (!currentProfileId) return alert('Nenhum funcionário selecionado.');
  document.getElementById('newPassword').value = '';
  document.getElementById('newPasswordConfirm').value = '';
  resetPwdModal.show();
}

function confirmResetPassword(ev) {
  ev.preventDefault();
  const p1 = document.getElementById('newPassword').value;
  const p2 = document.getElementById('newPasswordConfirm').value;
  if (p1.length < 4) return alert('Senha muito curta.');
  if (p1 !== p2) return alert('As senhas não conferem.');
  // salvar nova senha
  const all = getFuncs();
  const idx = all.findIndex(x => x.id === currentProfileId);
  if (idx === -1) { alert('Funcionário não encontrado.'); resetPwdModal.hide(); return; }
  all[idx].senha = p1;
  saveFuncs(all);
  resetPwdModal.hide();
  alert('Senha atualizada.');
  loadProfile(currentProfileId, true);
}

// quick reset password (button in list) — fallback (resets to '123456')
function onResetPasswordQuick(id) {
  const all = getFuncs();
  const idx = all.findIndex(x => x.id === id);
  if (idx === -1) return alert('Funcionário não encontrado.');
  all[idx].senha = '123456';
  saveFuncs(all);
  alert('Senha redefinida para: 123456');
  if (currentProfileId === id) loadProfile(id, true);
}

// Helpers
function escapeHtml(text) {
  if (text === undefined || text === null) return '';
  return String(text).replace(/[&<>"'`=\/]/g, s => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;','/':'&#x2F;','`':'&#x60;','=':'&#x3D;'}[s]));
}
function capitalize(s) { if (!s) return s; return s.split(' ').map(w => w.charAt(0).toUpperCase()+w.slice(1)).join(' '); }

// Inicialização: seed, render e hash support
(function init() {
  getFuncs(); // seed if needed
  showView('list');

  // handle direct hash links (profile/edit)
  if (location.hash) {
    const h = location.hash.slice(1);
    const [view, payload] = h.split('|');
    if (view === 'profile') loadProfile(payload, true);
    else if (view === 'edit') loadEdit(payload, true);
    else if (view === 'add') showView('add');
    else showView('list');
  }
})();