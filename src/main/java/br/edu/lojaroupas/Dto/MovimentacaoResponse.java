package br.edu.lojaroupas.dto;

import java.time.LocalDateTime;

public record MovimentacaoResponse(
        Long id,
        String produto,
        String tipo,
        int quantidade,
        LocalDateTime dataHora
) {}