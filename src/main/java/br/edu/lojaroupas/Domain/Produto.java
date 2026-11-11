package br.edu.lojaroupas.domain;

import br.edu.lojaroupas.exception.RegraDeNegocioException;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String tamanho;

    @Column(nullable = false)
    private String cor;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(name = "quantidade_estoque", nullable = false)
    private int quantidadeEstoque;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    protected Produto() {
    }

    public Produto(String nome, String tamanho, String cor,
                   BigDecimal preco, br.edu.lojaroupas.domain.Categoria categoria) {
        this.nome = nome;
        this.tamanho = tamanho;
        this.cor = cor;
        this.preco = preco;
        this.categoria = categoria;
        this.quantidadeEstoque = 0;
    }

    // REGRA: entrada precisa ser positiva
    public void registrarEntrada(int quantidade) {
        validarQuantidade(quantidade);
        this.quantidadeEstoque += quantidade;
    }

    // REGRA: saída não pode deixar estoque negativo
    public void registrarSaida(int quantidade) {
        validarQuantidade(quantidade);
        if (quantidade > quantidadeEstoque) {
            throw new RegraDeNegocioException(
                    "Estoque insuficiente. Disponível: " + quantidadeEstoque);
        }
        this.quantidadeEstoque -= quantidade;
    }

    private void validarQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new RegraDeNegocioException(
                    "A quantidade deve ser maior que zero.");
        }
    }

    public void atualizarPreco(BigDecimal novoPreco) {
        if (novoPreco == null || novoPreco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("O preço deve ser maior que zero.");
        }
        this.preco = novoPreco;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getTamanho() { return tamanho; }
    public String getCor() { return cor; }
    public BigDecimal getPreco() { return preco; }
    public int getQuantidadeEstoque() { return quantidadeEstoque; }
    public Categoria getCategoria() { return categoria; }
}
