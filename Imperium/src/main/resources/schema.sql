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

-- (CORREÇÃO) Passo 1: Criar a tabela 'funcoes' PRIMEIRO
CREATE TABLE IF NOT EXISTS funcoes (
    id_funcao INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL UNIQUE,
    descricao TEXT
);

-- (CORREÇÃO) Passo 2: Criar a tabela 'Usuario' DEPOIS, para que a referência exista
CREATE TABLE IF NOT EXISTS Usuario(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Nome mais claro e com valor padrão
    ativo BOOLEAN DEFAULT TRUE,
    data_ultimo_acesso DATETIME NULL,
    
    id_Funcao INT,

    FOREIGN KEY (id_Funcao) REFERENCES funcoes(id_funcao)
);

-- Passo 3: Inserir os dados na tabela 'funcoes'
INSERT INTO funcoes (nome, descricao) VALUES
('ADMINISTRADOR_PRINCIPAL', 'Pode gerenciar administradores e usuários.'),
('ADMINISTRADOR', 'Pode gerenciar apenas usuários padrão.'),
('USUARIO_PADRAO', 'Acesso básico ao sistema.');

SELECT f.nome 
FROM funcoes f
JOIN Usuario u ON u.id_Funcao = f.id_funcao
WHERE u.login = 'Admin';

SELECT * FROM Usuario;
