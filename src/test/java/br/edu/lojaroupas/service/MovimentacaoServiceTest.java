package br.edu.lojaroupas.service;

import br.edu.lojaroupas.Service.MovimentacaoService;
import br.edu.lojaroupas.domain.Categoria;
import br.edu.lojaroupas.domain.Movimentacao;
import br.edu.lojaroupas.domain.Produto;
import br.edu.lojaroupas.domain.TipoMovimentacao;
import br.edu.lojaroupas.dto.CriarMovimentacaoRequest;
import br.edu.lojaroupas.exception.RecursoNaoEncontradoException;
import br.edu.lojaroupas.exception.RegraDeNegocioException;
import br.edu.lojaroupas.repository.MovimentacaoRepository;
import br.edu.lojaroupas.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MovimentacaoServiceTest {

    private ProdutoRepository produtoRepository;
    private MovimentacaoRepository movimentacaoRepository;
    private MovimentacaoService service;
    private Produto produto;

    @BeforeEach
    void preparar() {
        produtoRepository = mock(ProdutoRepository.class);
        movimentacaoRepository = mock(MovimentacaoRepository.class);
        service = new MovimentacaoService(produtoRepository, movimentacaoRepository);

        produto = new Produto("Camiseta Básica", "M", "Preta",
                new BigDecimal("49.90"), new Categoria("Camisetas"));
        produto.registrarEntrada(7);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(movimentacaoRepository.save(any(Movimentacao.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));
    }

    @Test
    void naoDeveGravarMovimentacaoQuandoEstoqueForInsuficiente() {
        var request = new CriarMovimentacaoRequest(1L, TipoMovimentacao.SAIDA, 100);

        assertThrows(RegraDeNegocioException.class,
                () -> service.registrar(request));

        verify(movimentacaoRepository, never()).save(any());
        assertEquals(7, produto.getQuantidadeEstoque());
    }

    @Test
    void saidaValidaDeveAtualizarOEstoqueEGravarMovimentacao() {
        var request = new CriarMovimentacaoRequest(1L, TipoMovimentacao.SAIDA, 3);

        var resposta = service.registrar(request);

        assertEquals(4, produto.getQuantidadeEstoque());
        assertEquals("SAIDA", resposta.tipo());
        assertEquals(3, resposta.quantidade());
        verify(movimentacaoRepository).save(any(Movimentacao.class));
    }

    @Test
    void entradaDeveAumentarOEstoque() {
        var request = new CriarMovimentacaoRequest(1L, TipoMovimentacao.ENTRADA, 5);

        service.registrar(request);

        assertEquals(12, produto.getQuantidadeEstoque());
    }

    @Test
    void naoDeveMovimentarProdutoInexistente() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());
        var request = new CriarMovimentacaoRequest(99L, TipoMovimentacao.ENTRADA, 5);

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.registrar(request));
    }
}