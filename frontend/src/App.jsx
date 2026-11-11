import { useState } from "react";
import Categorias from "./components/Categorias";
import Produtos from "./components/Produtos";
import Movimentacoes from "./components/Movimentacoes";
import "./App.css";

const abas = [
    { id: "produtos", rotulo: "Produtos" },
    { id: "categorias", rotulo: "Categorias" },
    { id: "movimentacoes", rotulo: "Movimentações" },
];

export default function App() {
    const [aba, setAba] = useState("produtos");

    return (
        <>
            <header className="topo">
                <div className="topo-conteudo">
                    <h1>👕 Estoque da Loja de Roupas</h1>
                    <nav>
                        {abas.map((a) => (
                            <button
                                key={a.id}
                                className={aba === a.id ? "aba ativa" : "aba"}
                                onClick={() => setAba(a.id)}
                            >
                                {a.rotulo}
                            </button>
                        ))}
                    </nav>
                </div>
            </header>

            <main className="conteudo">
                {aba === "produtos" && <Produtos />}
                {aba === "categorias" && <Categorias />}
                {aba === "movimentacoes" && <Movimentacoes />}
            </main>
        </>
    );
}