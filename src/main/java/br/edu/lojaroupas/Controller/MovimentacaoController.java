package br.edu.lojaroupas.controller;

import br.edu.lojaroupas.dto.CriarMovimentacaoRequest;
import br.edu.lojaroupas.dto.MovimentacaoResponse;
import br.edu.lojaroupas.Service.MovimentacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimentacoes")
public class MovimentacaoController {

    private final MovimentacaoService service;

    public MovimentacaoController(MovimentacaoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimentacaoResponse registrar(@Valid @RequestBody CriarMovimentacaoRequest request) {
        return service.registrar(request);
    }

    @GetMapping("/produto/{produtoId}")
    public List<MovimentacaoResponse> listarPorProduto(@PathVariable Long produtoId) {
        return service.listarPorProduto(produtoId);
    }
}
