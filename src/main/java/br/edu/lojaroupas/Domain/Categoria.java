package br.edu.lojaroupas.domain;

import jakarta.persistence.*;

@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    protected Categoria() {
        // exigido pelo JPA
    }

    public Categoria(String nome) {
        this.nome = nome;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }

    public void alterarNome(String nome) {
        this.nome = nome;
    }
}
