-- Active: 1743547430456@@127.0.0.1@3306@Imperium
-- Remove o usuário e o banco de dados antigos para um ambiente limpo
DROP USER IF EXISTS 'Admin'@'localhost';
DROP DATABASE IF EXISTS Imperium;

-- Cria o banco de dados com a codificação recomendada
CREATE DATABASE IF NOT EXISTS Imperium
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Cria o usuário e concede as permissões
CREATE USER IF NOT EXISTS 'Admin'@'localhost' IDENTIFIED BY 'Admin123*';
GRANT ALL PRIVILEGES ON Imperium.* TO 'Admin'@'localhost';
FLUSH PRIVILEGES;

-- Seleciona o banco de dados para usar
USE Imperium;

CREATE TABLE IF NOT EXISTS Usuario(

    idUsuario INT PRIMARY KEY AUTO_INCREMENT,
    emailUsuario varchar(100) NOT NULL,
    senhaUsuario VARCHAR(255) NOT NULL,
    papelUsuario ENUM('Colaborador', 'Gerente', 'Administrador', 'CEO') NOT NULL,
    dataInicioUsuario DATE,
    ultimaAlteracaoUsuario DATETIME,
    idSetor INT,
    FOREIGN KEY (idSetor) REFERENCES Setor(idSetor)
);

CREATE TABLE IF NOT EXISTS Setor(
    idSetor INT PRIMARY KEY AUTO_INCREMENT,
    nomeSetor VARCHAR(50) NOT NULL,
    ramalSetor SMALLINT CHECK (ramalSetor <= 9999),
    descricao VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Chamados(
    idChamados INT PRIMARY KEY AUTO_INCREMENT,
    tituloChamado VARCHAR(50) NOT NULL,
    descricaoChamado TEXT NOT NULL,
    dataAberturaChamado DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dataFechamentoChamado DATETIME NOT NULL,
    statusChamado ENUM('Aberto', 'Em Processo', 'Fechado') NOT NULL DEFAULT 'Aberto',
    prioridadeChamado ENUM('Baixa', 'Media', 'Alta') NOT NULL DEFAULT 'Baixa',
    idUsuario INT NOT NULL,
    idSetor INT NOT NULL,
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idSetor) REFERENCES Setor(idSetor),
);

CREATE TABLE IF NOT EXISTS HistoricoChamado(
    idHistorico INT PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(255),
    dataHora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    idChamado INT NOT NULL,
    idUsuario INT NOT NULL,
    FOREIGN KEY (idChamado) REFERENCES Chamados(idChamados),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

CREATE TABLE IF NOT EXISTS Sugestoes(
    idSugestao INT PRIMARY KEY AUTO_INCREMENT,
    tituloSugestão VARCHAR(50) NOT NULL,
    descricaoSugestao TEXT NOT NULL,
    dataEnvioSugestao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    statusSugestao ENUM ('Enviada', 'Em Analise', 'Aprovada', 'Rejeitada') NOT NULL DEFAULT 'Enviada',
    idUsuario INT NOT NULL,
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