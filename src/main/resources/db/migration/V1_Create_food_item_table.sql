-- Primeira Migrations do projeto

CREATE TABLE Food_Item(
id BIGINT PRIMARY KEY AUTO_INCREMENT,
nome VARCHAR(255),
categoria VARCHAR(255),
quantidade INTEGER,
validade TIMESTAMP
);