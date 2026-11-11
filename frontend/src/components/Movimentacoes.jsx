import { useEffect, useState } from "react";
import { api } from "../api";
import Mensagem from "./Mensagem";

export default function Movimentacoes() {
    const [produtos, setProdutos] = useState([]);
    const [produtoId, setProdutoId] = useState("");
    const [tipo, setTipo] = useState("ENTRADA");
    const [quantidade, setQuantidade] = useState("");
    const [historico, setHistorico] = useState([]);
    const [mensagem, setMensagem] = useState(null);

    async function carregarProdutos() {
        try {
            setProdutos(await api.listarProdutos());
        } catch (e) {
            setMensagem({ tipo: "erro", texto: e.message });
        }
    }

    async function carregarHistorico(id) {
        if (!id) {
            setHistorico([]);
            return;
        }
        try {
            setHistorico(await api.listarMovimentacoes(id));
        } catch (e) {
            setMensagem({ tipo: "erro", texto: e.message });
        }
    }

    useEffect(() => {
        carregarProdutos();
    }, []);

    // sempre que o produto escolhido muda, busca o histórico dele
    useEffect(() => {
        carregarHistorico(produtoId);
    }, [produtoId]);

    async function registrar(evento) {
        evento.preventDefault();
        setMensagem(null);
        try {
            await api.registrarMovimentacao({
                produtoId: Number(produtoId),
                tipo,
                quantidade: Number(quantidade),
            });
            setQuantidade("");
            setMensagem({
                tipo: "sucesso",
                texto: tipo === "ENTRADA" ? "Entrada registrada." : "Saída registrada.",
            });
            await carregarProdutos(); // atualiza o estoque mostrado
            await carregarHistorico(produtoId);
        } catch (e) {
            setMensagem({ tipo: "erro", texto: e.message });
        }
    }

    const produtoSelecionado = produtos.find((p) => String(p.id) === String(produtoId));

    return (
        <section>
            <h2>Movimentações de estoque</h2>

            <Mensagem mensagem={mensagem} />

            <div className="card">
                <h3>Registrar movimentação</h3>
                <form className="formulario" onSubmit={registrar}>
                    <select value={produtoId} onChange={(e) => setProdutoId(e.target.value)}>
                        <option value="">Produto...</option>
                        {produtos.map((p) => (
                            <option key={p.id} value={p.id}>
                                {p.nome} - {p.tamanho} - {p.cor}
                            </option>
                        ))}
                    </select>

                    <div className="tipo-botoes">
                        <button
                            type="button"
                            className={tipo === "ENTRADA" ? "tipo-botao entrada ativo" : "tipo-botao entrada"}
                            onClick={() => setTipo("ENTRADA")}
                        >
                            Entrada
                        </button>
                        <button
                            type="button"
                            className={tipo === "SAIDA" ? "tipo-botao saida ativo" : "tipo-botao saida"}
                            onClick={() => setTipo("SAIDA")}
                        >
                            Saída
                        </button>
                    </div>

                    <input
                        type="number"
                        placeholder="Quantidade"
                        value={quantidade}
                        onChange={(e) => setQuantidade(e.target.value)}
                    />
                    <button className="btn" type="submit">
                        Registrar
                    </button>
                </form>
            </div>

            {produtoSelecionado && (
                <div className="estoque-atual">
                    Estoque atual de <strong>{produtoSelecionado.nome}</strong>:
                    <span className="etiqueta verde">{produtoSelecionado.quantidadeEstoque} unidades</span>
                </div>
            )}

            <div className="card tabela-wrap">
                <h3>Histórico</h3>
                <table>
                    <thead>
                    <tr>
                        <th>Data</th>
                        <th>Tipo</th>
                        <th>Quantidade</th>
                    </tr>
                    </thead>
                    <tbody>
                    {historico.length === 0 && (
                        <tr>
                            <td colSpan="3" className="vazio">
                                {produtoId
                                    ? "Este produto ainda não tem movimentações."
                                    : "Escolha um produto para ver o histórico."}
                            </td>
                        </tr>
                    )}
                    {historico.map((m) => (
                        <tr key={m.id}>
                            <td>{new Date(m.dataHora).toLocaleString("pt-BR")}</td>
                            <td>
                  <span className={`etiqueta ${m.tipo === "ENTRADA" ? "verde" : "vermelho"}`}>
                    {m.tipo === "ENTRADA" ? "Entrada" : "Saída"}
                  </span>
                            </td>
                            <td>{m.quantidade}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </section>
    );
}