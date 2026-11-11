const BASE_URL = "http://localhost:8080/api";

// Função única que faz todas as chamadas e traduz os erros do backend
async function requisicao(caminho, opcoes = {}) {
    let resposta;
    try {
        resposta = await fetch(`${BASE_URL}${caminho}`, {
            headers: { "Content-Type": "application/json" },
            ...opcoes,
        });
    } catch {
        throw new Error("Não foi possível conectar ao servidor.");
    }

    if (!resposta.ok) {
        let mensagem = "Erro inesperado.";
        try {
            const corpo = await resposta.json();
            mensagem = corpo.erro || mensagem;
        } catch {
            // resposta sem JSON: mantém a mensagem padrão
        }
        throw new Error(mensagem);
    }

    if (resposta.status === 204) return null; // DELETE não devolve corpo
    return resposta.json();
}

export const api = {
    listarCategorias: () => requisicao("/categorias"),
    criarCategoria: (nome) =>
        requisicao("/categorias", { method: "POST", body: JSON.stringify({ nome }) }),
    excluirCategoria: (id) =>
        requisicao(`/categorias/${id}`, { method: "DELETE" }),

    listarProdutos: () => requisicao("/produtos"),
    criarProduto: (dados) =>
        requisicao("/produtos", { method: "POST", body: JSON.stringify(dados) }),
    atualizarPreco: (id, preco) =>
        requisicao(`/produtos/${id}/preco`, {
            method: "PATCH",
            body: JSON.stringify({ preco }),
        }),

    registrarMovimentacao: (dados) =>
        requisicao("/movimentacoes", { method: "POST", body: JSON.stringify(dados) }),
    listarMovimentacoes: (produtoId) =>
        requisicao(`/movimentacoes/produto/${produtoId}`),
};