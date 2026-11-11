package br.edu.lojaroupas.controller;

import br.edu.lojaroupas.dto.AtualizarPrecoRequest;
import br.edu.lojaroupas.dto.CriarProdutoRequest;
import br.edu.lojaroupas.dto.ProdutoResponse;
import br.edu.lojaroupas.Service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse criar(@Valid @RequestBody CriarProdutoRequest request) {
        return service.criar(request);
    }

    @GetMapping
    public List<ProdutoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PatchMapping("/{id}/preco")
    public ProdutoResponse atualizarPreco(@PathVariable Long id,
                                          @Valid @RequestBody AtualizarPrecoRequest request) {
        return service.atualizarPreco(id, request);
    }
}