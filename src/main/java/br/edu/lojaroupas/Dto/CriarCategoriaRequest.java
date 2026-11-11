package br.edu.lojaroupas.dto;

import jakarta.validation.constraints.NotBlank;

public record CriarCategoriaRequest(
        @NotBlank(message = "O nome é obrigatório.")
        String nome
) {}