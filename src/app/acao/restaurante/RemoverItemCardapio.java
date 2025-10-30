package app.acao.restaurante;

import app.acao.Acao;
import app.restaurante.Cardapio;
import app.restaurante.ItemCardapio;

public class RemoverItemCardapio extends Acao {
    Cardapio cardapio;
    ItemCardapio item;

    public RemoverItemCardapio(Cardapio cardapio, ItemCardapio item) {
        this.cardapio = cardapio;
        this.item = item;
    }

    @Override
    public void executar() {
        cardapio.removerItem(item);
        System.out.println("  Item " + item.obterNome() + " removido do cardápio");
    }

    @Override
    public String obterNome() {
        return "Remover Item do Cardápio";
    }

    @Override
    public String obterDescricao() {
        return "Remover " + item.obterNome() + " do cardápio (baixo desempenho)";
    }
}