package app.acao;

public abstract class Acao {
    public abstract void executar();
    public abstract String obterNome();
    public abstract String obterDescricao();
    
    public boolean verificarSePodeExecutar() {
        return true;
    }
}