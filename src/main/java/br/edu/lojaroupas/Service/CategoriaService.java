package br.edu.lojaroupas.Service;

import br.edu.lojaroupas.domain.Categoria;
import br.edu.lojaroupas.dto.CategoriaResponse;
import br.edu.lojaroupas.dto.CriarCategoriaRequest;
import br.edu.lojaroupas.exception.RecursoNaoEncontradoException;
import br.edu.lojaroupas.exception.RegraDeNegocioException;
import br.edu.lojaroupas.repository.CategoriaRepository;
import br.edu.lojaroupas.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    public final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository,
                            ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public CategoriaResponse criar(CriarCategoriaRequest request) {
        if (categoriaRepository.existsByNomeIgnoreCase(request.nome())) {
            throw new RegraDeNegocioException(
                    "Já existe uma categoria com esse nome.");
        }
        var categoria = categoriaRepository.save(new Categoria(request.nome()));
        return toResponse(categoria);
    }

    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoriaResponse buscar(Long id) {
        return toResponse(buscarEntidade(id));
    }

    // REGRA: não excluir categoria que ainda tem produtos
    @Transactional
    public void excluir(Long id) {
        var categoria = buscarEntidade(id);

        if (produtoRepository.existsByCategoriaId(id)) {
            throw new RegraDeNegocioException(
                    "Não é possível excluir uma categoria que possui produtos.");
        }
        categoriaRepository.delete(categoria);
    }

    private Categoria buscarEntidade(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria não encontrada: " + id));
    }

    private CategoriaResponse toResponse(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNome());
    }
}

