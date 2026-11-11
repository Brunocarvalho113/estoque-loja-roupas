CREATE DATABASE IF NOT EXISTS estoque_roupas;
USE estoque_roupas;

CREATE TABLE categoria (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE produto (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(150) NOT NULL,
                         tamanho VARCHAR(10) NOT NULL,
                         cor VARCHAR(50) NOT NULL,
                         preco DECIMAL(10,2) NOT NULL CHECK (preco > 0),
                         quantidade_estoque INT NOT NULL DEFAULT 0 CHECK (quantidade_estoque >= 0),
                         categoria_id BIGINT NOT NULL,
                         CONSTRAINT fk_produto_categoria
                             FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

CREATE TABLE movimentacao (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              produto_id BIGINT NOT NULL,
                              tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('ENTRADA', 'SAIDA')),
                              quantidade INT NOT NULL CHECK (quantidade > 0),
                              data_hora DATETIME NOT NULL,
                              CONSTRAINT fk_movimentacao_produto
                                  FOREIGN KEY (produto_id) REFERENCES produto(id)
);