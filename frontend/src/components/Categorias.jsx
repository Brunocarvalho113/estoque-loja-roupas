import { useEffect, useState } from "react";
import { api } from "../api";
import Mensagem from "./Mensagem";

export default function Categorias() {
    const [categorias, setCategorias] = useState([]);
    const [produtos, setProdutos] = useState([]);
    const [nome, setNome] = useState("");
    const [mensagem, setMensagem] = useState(null);

    async function carregar() {
        try {
            setCategorias(await api.listarCategorias());
            setProdutos(await api.listarProdutos());
        } catch (e) {
            setMensagem({ tipo: "erro", texto: e.message });
        }
    }

    useEffect(() => {
        carregar();
    }, []);

    async function cadastrar(evento) {
        evento.preventDefault();
        setMensagem(null);
        try {
            await api.criarCategoria(nome);
            setNome("");
            setMensagem({ tipo: "sucesso", texto: "Categoria cadastrada com sucesso." });
            await carregar();
        } catch (e) {
            setMensagem({ tipo: "erro", texto: e.message });
        }
    }

    async function excluir(categoria) {
        if (!window.confirm(`Excluir a categoria "${categoria.nome}"?`)) return;
        setMensagem(null);
        try {
            await api.excluirCategoria(categoria.id);
            setMensagem({ tipo: "sucesso", texto: "Categoria excluída." });
            await carregar();
        } catch (e) {
            setMensagem({ tipo: "erro", texto: e.message });
        }
    }

    // conta quantos produtos pertencem a cada categoria
    function quantosProdutos(nomeCategoria) {
        return produtos.filter((p) => p.categoria === nomeCategoria).length;
    }

    return (
        <section>
            <h2>Categorias</h2>

            <Mensagem mensagem={mensagem} />

            <div className="card">
                <h3>Nova categoria</h3>
                <form className="formulario" onSubmit={cadastrar}>
                    <input
                        placeholder="Nome da categoria"
                        value={nome}
                        onChange={(e) => setNome(e.target.value)}
                    />
                    <button className="btn" type="submit">
                        Cadastrar
                    </button>
                </form>
            </div>

            <div className="card tabela-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Produtos</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    {categorias.length === 0 && (
                        <tr>
                            <td colSpan="4" className="vazio">
                                Nenhuma categoria cadastrada.
                            </td>
                        </tr>
                    )}
                    {categorias.map((c) => (
                        <tr key={c.id}>
                            <td>{c.id}</td>
                            <td>{c.nome}</td>
                            <td>{quantosProdutos(c.nome)}</td>
                            <td>
                                <button className="btn-leve btn-perigo" onClick={() => excluir(c)}>
                                    Excluir
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </section>
    );
}