package br.edu.lojaroupas.Service;

import br.edu.lojaroupas.domain.Movimentacao;
import br.edu.lojaroupas.domain.TipoMovimentacao;
import br.edu.lojaroupas.dto.CriarMovimentacaoRequest;
import br.edu.lojaroupas.dto.MovimentacaoResponse;
import br.edu.lojaroupas.exception.RecursoNaoEncontradoException;
import br.edu.lojaroupas.repository.MovimentacaoRepository;
import br.edu.lojaroupas.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovimentacaoService {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    public MovimentacaoService(ProdutoRepository produtoRepository,
                               MovimentacaoRepository movimentacaoRepository) {
        this.produtoRepository = produtoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @Transactional
    public MovimentacaoResponse registrar(CriarMovimentacaoRequest request) {
        var produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Produto não encontrado: " + request.produtoId()));

        // O Service só coordena. A regra de estoque vive no Produto.
        if (request.tipo() == TipoMovimentacao.ENTRADA) {
            produto.registrarEntrada(request.quantidade());
        } else {
            produto.registrarSaida(request.quantidade());
        }

        var movimentacao = new Movimentacao(

                produto, request.tipo(), request.quantidade());

        return toResponse(movimentacaoRepository.save(movimentacao));
    }

    public List<MovimentacaoResponse> listarPorProduto(Long produtoId) {
        if (!produtoRepository.existsById(produtoId)) {
            throw new RecursoNaoEncontradoException(
                    "Produto não encontrado: " + produtoId);
        }
        return movimentacaoRepository
                .findByProdutoIdOrderByDataHoraDesc(produtoId).stream()
                .map(this::toResponse)
                .toList();
    }

    private MovimentacaoResponse toResponse(Movimentacao m) {
        return new MovimentacaoResponse(
                m.getId(),
                m.getProduto().getNome(),
                m.getTipo().name(),
                m.getQuantidade(),
                m.getDataHora());
    }
}