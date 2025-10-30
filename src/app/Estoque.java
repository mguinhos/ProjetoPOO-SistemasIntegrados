package app;

import java.util.Vector;

public class Estoque {
    Vector<ItemEstoque> items;

    public Estoque() {
        this.items = new Vector<ItemEstoque>();
    }

    public Vector<ItemEstoque> obterItems() {
        return this.items;
    }

    public void adicionarItem(Recurso recurso, int quantidade) {
        for (ItemEstoque item : items) {
            if (item.obterRecurso().obterNome().equals(recurso.obterNome())) {
                item.adicionarQuantidade(quantidade);
                return;
            }
        }
        items.add(new ItemEstoque(recurso, quantidade));
    }

    public boolean removerItem(Recurso recurso, int quantidade) {
        for (ItemEstoque item : items) {
            if (item.obterRecurso().obterNome().equals(recurso.obterNome())) {
                return item.removerQuantidade(quantidade);
            }
        }
        return false;
    }

    public boolean possuiItem(Recurso recurso, int quantidade) {
        for (ItemEstoque item : items) {
            if (item.obterRecurso().obterNome().equals(recurso.obterNome())) {
                return item.obterQuantidade() >= quantidade;
            }
        }
        return false;
    }

    public int obterQuantidade(Recurso recurso) {
        for (ItemEstoque item : items) {
            if (item.obterRecurso().obterNome().equals(recurso.obterNome())) {
                return item.obterQuantidade();
            }
        }
        return 0;
    }
}