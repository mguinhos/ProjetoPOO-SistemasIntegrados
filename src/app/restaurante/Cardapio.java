package app.restaurante;

import java.util.Vector;

public class Cardapio {
    Vector<ItemCardapio> items;

    public Cardapio() {
        this.items = new Vector<ItemCardapio>();
    }

    public Cardapio(Vector<ItemCardapio> items) {
        this.items = items;
    }

    public void adicionarItem(ItemCardapio item) {
        this.items.add(item);
    }

    public void removerItem(ItemCardapio item) {
        this.items.remove(item);
    }

    public Vector<ItemCardapio> obterItems() {
        return this.items;
    }

    public void aplicarDesconto(ItemCardapio item, double percentual) {
        double precoAtual = item.obterPreco();
        double novoPreco = precoAtual * (1 - percentual / 100);
        item.definirPreco(novoPreco);
    }
}