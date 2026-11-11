package br.edu.lojaroupas.dto;

import java.math.BigDecimal;

public record ProdutoResponse(
        Long id,
        String nome,
        String tamanho,
        String cor,
        BigDecimal preco,
        int quantidadeEstoque,
        String categoria
) {}
