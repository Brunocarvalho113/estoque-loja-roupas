package br.edu.lojaroupas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CriarProdutoRequest(
        @NotBlank(message = "O nome é obrigatório.")
        String nome,

        @NotBlank(message = "O tamanho é obrigatório.")
        String tamanho,

        @NotBlank(message = "A cor é obrigatória.")
        String cor,

        @NotNull(message = "O preço é obrigatório.")
        @Positive(message = "O preço deve ser maior que zero.")
        BigDecimal preco,

        @NotNull(message = "A categoria é obrigatória.")
        Long categoriaId
) {}
