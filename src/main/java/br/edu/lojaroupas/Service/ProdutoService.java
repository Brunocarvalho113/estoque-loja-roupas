package br.edu.lojaroupas.Service;

import br.edu.lojaroupas.domain.Produto;
import br.edu.lojaroupas.dto.AtualizarPrecoRequest;
import br.edu.lojaroupas.dto.CriarProdutoRequest;
import br.edu.lojaroupas.dto.ProdutoResponse;
import br.edu.lojaroupas.exception.RecursoNaoEncontradoException;
import br.edu.lojaroupas.repository.CategoriaRepository;
import br.edu.lojaroupas.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository,
                          CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public ProdutoResponse criar(CriarProdutoRequest request) {
        var categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria não encontrada: " + request.categoriaId()));

        var produto = new Produto(
                request.nome(),
                request.tamanho(),
                request.cor(),
                request.preco(),
                categoria);

        return toResponse(produtoRepository.save(produto));
    }

    public List<ProdutoResponse> listar() {
        return produtoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ProdutoResponse buscar(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public ProdutoResponse atualizarPreco(Long id, AtualizarPrecoRequest request) {
        var produto = buscarEntidade(id);
        produto.atualizarPreco(request.preco());
        return toResponse(produto);
    }

    private Produto buscarEntidade(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Produto não encontrado: " + id));
    }

    private ProdutoResponse toResponse(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getTamanho(),
                produto.getCor(),
                produto.getPreco(),
                produto.getQuantidadeEstoque(),
                produto.getCategoria().getNome());
    }
}