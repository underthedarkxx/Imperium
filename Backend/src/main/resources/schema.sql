DROP DATABASE IF EXISTS Imperium;

-- Cria o banco de dados com a codificação recomendada
CREATE DATABASE IF NOT EXISTS Imperium
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Seleciona o banco de dados para usar
USE Imperium;

CREATE TABLE IF NOT EXISTS Setor(
    idSetor INT PRIMARY KEY AUTO_INCREMENT,
    nomeSetor VARCHAR(50) NOT NULL,
    ramalSetor SMALLINT CHECK (ramalSetor <= 9999),
    descricao VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Usuario(

    idUsuario BIGINT PRIMARY KEY AUTO_INCREMENT,
    emailUsuario varchar(100) NOT NULL UNIQUE,
    senhaUsuario VARCHAR(255) NOT NULL,
    papelUsuario ENUM('Colaborador', 'Gerente', 'Administrador', 'CEO') NOT NULL,
    statusUsuario ENUM('Ativo', 'Desativado') NOT NULL DEFAULT 'Ativo',
    dataInicioUsuario DATETIME,
    ultimaAlteracaoUsuario DATETIME,
    idSetor INT,
    FOREIGN KEY (idSetor) REFERENCES Setor(idSetor)
);

CREATE TABLE IF NOT EXISTS Chamados(
    idChamados INT PRIMARY KEY AUTO_INCREMENT,
    tituloChamado VARCHAR(50) NOT NULL,
    descricaoChamado TEXT NOT NULL,
    dataAberturaChamado DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dataFechamentoChamado DATETIME NULL,
    statusChamado ENUM('Aberto', 'Em Processo', 'Fechado') NOT NULL DEFAULT 'Aberto',
    prioridadeChamado ENUM('Baixa', 'Media', 'Alta') NOT NULL DEFAULT 'Baixa',
    idUsuario BIGINT NOT NULL,
    idSetor INT NOT NULL,
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idSetor) REFERENCES Setor(idSetor)
);

CREATE TABLE IF NOT EXISTS HistoricoChamado(
    idHistorico INT PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(255),
    dataHora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    idChamado INT NOT NULL,
    idUsuario BIGINT NOT NULL,
    FOREIGN KEY (idChamado) REFERENCES Chamados(idChamados),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

CREATE TABLE IF NOT EXISTS Sugestoes(
    idSugestao BIGINT PRIMARY KEY AUTO_INCREMENT,
    tituloSugestao VARCHAR(50) NOT NULL,
    descricaoSugestao TEXT NOT NULL,
    dataEnvioSugestao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    statusSugestao ENUM ('Enviada', 'EmAnalise', 'Aprovada', 'Rejeitada') NOT NULL DEFAULT 'Enviada',
    idUsuario BIGINT NOT NULL,
    idSetor INT NOT NULL,
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idSetor) REFERENCES Setor(idSetor)
);

CREATE TABLE IF NOT EXISTS Categorias (
    idCategoria INT PRIMARY KEY AUTO_INCREMENT,
    nomeCategoria VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Produtos (
    idProdutos INT PRIMARY KEY AUTO_INCREMENT,
    nomeProduto VARCHAR(100) NOT NULL,
    descricaoProduto TEXT,
    precoProduto DECIMAL(10, 2) NOT NULL CHECK (precoProduto >= 0),
    idCategoria INT,
    FOREIGN KEY (idCategoria) REFERENCES Categorias(idCategoria)
);

INSERT INTO Setor (nomeSetor, ramalSetor, descricao)
VALUES ('Diretoria', 1001, 'Setor responsável pela direção da empresa');

INSERT INTO Usuario (
    emailUsuario,
    senhaUsuario,
    papelUsuario,
    statusUsuario,
    dataInicioUsuario,
    ultimaAlteracaoUsuario,
    idSetor
)
VALUES (
    'ceo@imperium.com',
    '$2a$10$7eXJwSvKk.3p7/9rj0R2aO4kRxebby/5hW/TZyC3H1Z.HFawMwc9C',
    'CEO',
    'Ativo',
    NOW(),
    NOW(),
    1
);