package app;

public class ItemEstoque {
    Recurso recurso;
    int quantidade;

    public ItemEstoque(Recurso recurso, int quantidade) {
        this.recurso = recurso;
        this.quantidade = quantidade;
    }

    public Recurso obterRecurso() {
        return this.recurso;
    }

    public int obterQuantidade() {
        return this.quantidade;
    }

    public void adicionarQuantidade(int valor) {
        this.quantidade += valor;
    }

    public boolean removerQuantidade(int valor) {
        if (this.quantidade >= valor) {
            this.quantidade -= valor;
            return true;
        }
        return false;
    }
}