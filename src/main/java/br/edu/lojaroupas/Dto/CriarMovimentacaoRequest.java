package br.edu.lojaroupas.dto;

import br.edu.lojaroupas.domain.TipoMovimentacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CriarMovimentacaoRequest(
        @NotNull(message = "O produto é obrigatório.")
        Long produtoId,

        @NotNull(message = "O tipo é obrigatório (ENTRADA ou SAIDA).")
        TipoMovimentacao tipo,

        @Positive(message = "A quantidade deve ser maior que zero.")
        int quantidade
) {}