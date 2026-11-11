package br.edu.lojaroupas.service;

import br.edu.lojaroupas.Service.CategoriaService;
import br.edu.lojaroupas.domain.Categoria;
import br.edu.lojaroupas.dto.CriarCategoriaRequest;
import br.edu.lojaroupas.exception.RegraDeNegocioException;
import br.edu.lojaroupas.repository.CategoriaRepository;
import br.edu.lojaroupas.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CategoriaServiceTest {

    private CategoriaRepository categoriaRepository;
    private ProdutoRepository produtoRepository;
    private CategoriaService service;

    @BeforeEach
    void preparar() {
        categoriaRepository = mock(CategoriaRepository.class);
        produtoRepository = mock(ProdutoRepository.class);
        service = new CategoriaService(categoriaRepository, produtoRepository);
    }

    @Test
    void naoDeveExcluirCategoriaQueTemProdutos() {
        // Arrange
        var categoria = new Categoria("Camisetas");
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(produtoRepository.existsByCategoriaId(1L)).thenReturn(true);

        // Act + Assert
        assertThrows(RegraDeNegocioException.class, () -> service.excluir(1L));

        // a exclusão nunca pode ter sido chamada
        verify(categoriaRepository, never()).delete(any());
    }

    @Test
    void deveExcluirCategoriaSemProdutos() {
        var categoria = new Categoria("Camisetas");
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(produtoRepository.existsByCategoriaId(1L)).thenReturn(false);

        service.excluir(1L);

        verify(categoriaRepository).delete(categoria);
    }

    @Test
    void naoDeveCriarCategoriaComNomeDuplicado() {
        when(categoriaRepository.existsByNomeIgnoreCase("Camisetas")).thenReturn(true);

        assertThrows(RegraDeNegocioException.class,
                () -> service.criar(new CriarCategoriaRequest("Camisetas")));

        verify(categoriaRepository, never()).save(any());
    }
}
