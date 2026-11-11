export default function Mensagem({ mensagem }) {
    if (!mensagem) return null;

    return <div className={`mensagem ${mensagem.tipo}`}>{mensagem.texto}</div>;
}