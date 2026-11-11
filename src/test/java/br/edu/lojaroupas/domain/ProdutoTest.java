package br.edu.lojaroupas.domain;

import br.edu.lojaroupas.exception.RegraDeNegocioException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProdutoTest {

    private Produto criarProduto() {
        var categoria = new Categoria("Camisetas");
        return new Produto("Camiseta Básica", "M", "Preta",
                new BigDecimal("49.90"), categoria);
    }

    @Test
    void entradaDeveAumentarOEstoque() {
        // Arrange
        var produto = criarProduto();

        // Act
        produto.registrarEntrada(10);

        // Assert
        assertEquals(10, produto.getQuantidadeEstoque());
    }

    @Test
    void saidaDeveDiminuirOEstoque() {
        var produto = criarProduto();
        produto.registrarEntrada(10);

        produto.registrarSaida(3);

        assertEquals(7, produto.getQuantidadeEstoque());
    }

    @Test
    void naoDevePermitirSaidaMaiorQueOEstoque() {
        var produto = criarProduto();
        produto.registrarEntrada(5);

        assertThrows(RegraDeNegocioException.class,
                () -> produto.registrarSaida(6));

        // o estoque não pode ter mudado
        assertEquals(5, produto.getQuantidadeEstoque());
    }

    @Test
    void devePermitirSaidaDeTodoOEstoque() {
        var produto = criarProduto();
        produto.registrarEntrada(5);

        produto.registrarSaida(5);

        assertEquals(0, produto.getQuantidadeEstoque());
    }

    @Test
    void naoDevePermitirQuantidadeZeroOuNegativa() {
        var produto = criarProduto();

        assertThrows(RegraDeNegocioException.class,
                () -> produto.registrarEntrada(0));
        assertThrows(RegraDeNegocioException.class,
                () -> produto.registrarSaida(-2));
    }

    @Test
    void naoDevePermitirPrecoZeroOuNegativo() {
        var produto = criarProduto();

        assertThrows(RegraDeNegocioException.class,
                () -> produto.atualizarPreco(BigDecimal.ZERO));
        assertThrows(RegraDeNegocioException.class,
                () -> produto.atualizarPreco(new BigDecimal("-10")));
    }
}