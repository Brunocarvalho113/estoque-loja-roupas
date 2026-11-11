import { useEffect, useState } from "react";
import { api } from "../api";
import Mensagem from "./Mensagem";

const formVazio = { nome: "", tamanho: "", cor: "", preco: "", categoriaId: "" };

// Abaixo ou igual a este valor, a tela mostra "Estoque baixo" (é só visual)
const LIMITE_ESTOQUE_BAIXO = 3;

function moeda(valor) {
    return Number(valor).toLocaleString("pt-BR", {
        style: "currency",
        currency: "BRL",
    });
}

function situacao(quantidade) {
    if (quantidade === 0) return { classe: "vermelho", texto: "Sem estoque" };
    if (quantidade <= LIMITE_ESTOQUE_BAIXO) return { classe: "amarelo", texto: "Estoque baixo" };
    return { classe: "verde", texto: "Em estoque" };
}

export default function Produtos() {
    const [produtos, setProdutos] = useState([]);
    const [categorias, setCategorias] = useState([]);
    const [form, setForm] = useState(formVazio);
    const [busca, setBusca] = useState("");
    const [filtroCategoria, setFiltroCategoria] = useState("");
    const [editandoId, setEditandoId] = useState(null);
    const [novoPreco, setNovoPreco] = useState("");
    const [mensagem, setMensagem] = useState(null);

    async function carregar() {
        try {
            setProdutos(await api.listarProdutos());
            setCategorias(await api.listarCategorias());
        } catch (e) {
            setMensagem({ tipo: "erro", texto: e.message });
        }
    }

    useEffect(() => {
        carregar();
    }, []);

    function alterarCampo(evento) {
        setForm({ ...form, [evento.target.name]: evento.target.value });
    }

    async function cadastrar(evento) {
        evento.preventDefault();
        setMensagem(null);
        try {
            await api.criarProduto({
                ...form,
                preco: Number(form.preco),
                categoriaId: Number(form.categoriaId),
            });
            setForm(formVazio);
            setMensagem({ tipo: "sucesso", texto: "Produto cadastrado com sucesso." });
            await carregar();
        } catch (e) {
            setMensagem({ tipo: "erro", texto: e.message });
        }
    }

    function iniciarEdicao(produto) {
        setEditandoId(produto.id);
        setNovoPreco(produto.preco);
    }

    async function salvarPreco(produto) {
        setMensagem(null);
        try {
            await api.atualizarPreco(produto.id, Number(novoPreco));
            setEditandoId(null);
            setMensagem({ tipo: "sucesso", texto: "Preço atualizado." });
            await carregar();
        } catch (e) {
            setMensagem({ tipo: "erro", texto: e.message });
        }
    }

    // ----- números do resumo -----
    const totalUnidades = produtos.reduce((soma, p) => soma + p.quantidadeEstoque, 0);
    const valorTotal = produtos.reduce(
        (soma, p) => soma + Number(p.preco) * p.quantidadeEstoque,
        0
    );
    const comEstoqueBaixo = produtos.filter(
        (p) => p.quantidadeEstoque <= LIMITE_ESTOQUE_BAIXO
    ).length;

    // ----- busca e filtro -----
    const produtosFiltrados = produtos.filter(
        (p) =>
            p.nome.toLowerCase().includes(busca.toLowerCase()) &&
            (filtroCategoria === "" || p.categoria === filtroCategoria)
    );

    return (
        <section>
            <h2>Produtos</h2>

            <div className="resumo">
                <div className="resumo-item">
                    <span>Produtos cadastrados</span>
                    <strong>{produtos.length}</strong>
                </div>
                <div className="resumo-item">
                    <span>Unidades em estoque</span>
                    <strong>{totalUnidades}</strong>
                </div>
                <div className="resumo-item">
                    <span>Valor em estoque</span>
                    <strong>{moeda(valorTotal)}</strong>
                </div>
                <div className="resumo-item alerta">
                    <span>Com estoque baixo</span>
                    <strong>{comEstoqueBaixo}</strong>
                </div>
            </div>

            <Mensagem mensagem={mensagem} />

            <div className="card">
                <h3>Novo produto</h3>
                <form className="formulario" onSubmit={cadastrar}>
                    <input name="nome" placeholder="Nome" value={form.nome} onChange={alterarCampo} />
                    <input
                        name="tamanho"
                        placeholder="Tamanho"
                        value={form.tamanho}
                        onChange={alterarCampo}
                    />
                    <input name="cor" placeholder="Cor" value={form.cor} onChange={alterarCampo} />
                    <input
                        name="preco"
                        type="number"
                        step="0.01"
                        placeholder="Preço"
                        value={form.preco}
                        onChange={alterarCampo}
                    />
                    <select name="categoriaId" value={form.categoriaId} onChange={alterarCampo}>
                        <option value="">Categoria...</option>
                        {categorias.map((c) => (
                            <option key={c.id} value={c.id}>
                                {c.nome}
                            </option>
                        ))}
                    </select>
                    <button className="btn" type="submit">
                        Cadastrar
                    </button>
                </form>
            </div>

            <div className="card">
                <div className="formulario">
                    <input
                        placeholder="Buscar por nome..."
                        value={busca}
                        onChange={(e) => setBusca(e.target.value)}
                    />
                    <select value={filtroCategoria} onChange={(e) => setFiltroCategoria(e.target.value)}>
                        <option value="">Todas as categorias</option>
                        {categorias.map((c) => (
                            <option key={c.id} value={c.nome}>
                                {c.nome}
                            </option>
                        ))}
                    </select>
                </div>
            </div>

            <div className="card tabela-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Produto</th>
                        <th>Tam.</th>
                        <th>Cor</th>
                        <th>Categoria</th>
                        <th>Preço</th>
                        <th>Estoque</th>
                        <th>Situação</th>
                    </tr>
                    </thead>
                    <tbody>
                    {produtosFiltrados.length === 0 && (
                        <tr>
                            <td colSpan="7" className="vazio">
                                Nenhum produto encontrado.
                            </td>
                        </tr>
                    )}
                    {produtosFiltrados.map((p) => {
                        const s = situacao(p.quantidadeEstoque);
                        return (
                            <tr key={p.id}>
                                <td>{p.nome}</td>
                                <td>{p.tamanho}</td>
                                <td>{p.cor}</td>
                                <td>{p.categoria}</td>
                                <td>
                                    {editandoId === p.id ? (
                                        <div className="acoes">
                                            <input
                                                type="number"
                                                step="0.01"
                                                value={novoPreco}
                                                onChange={(e) => setNovoPreco(e.target.value)}
                                            />
                                            <button className="btn-leve" onClick={() => salvarPreco(p)}>
                                                Salvar
                                            </button>
                                            <button className="btn-leve" onClick={() => setEditandoId(null)}>
                                                Cancelar
                                            </button>
                                        </div>
                                    ) : (
                                        <div className="acoes">
                                            {moeda(p.preco)}
                                            <button className="btn-leve" onClick={() => iniciarEdicao(p)}>
                                                Alterar
                                            </button>
                                        </div>
                                    )}
                                </td>
                                <td>{p.quantidadeEstoque}</td>
                                <td>
                                    <span className={`etiqueta ${s.classe}`}>{s.texto}</span>
                                </td>
                            </tr>
                        );
                    })}
                    </tbody>
                </table>
            </div>
        </section>
    );
}