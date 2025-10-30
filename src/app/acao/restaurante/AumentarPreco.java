package app.acao.restaurante;

import app.acao.Acao;

import app.restaurante.Cardapio;
import app.restaurante.ItemCardapio;

public class AumentarPreco extends Acao {
    Cardapio cardapio;
    ItemCardapio item;
    double percentual;

    public AumentarPreco(Cardapio cardapio, ItemCardapio item, double percentual) {
        this.cardapio = cardapio;
        this.item = item;
        this.percentual = percentual;
    }

    @Override
    public void executar() {
        double precoAtual = item.obterPreco();
        double novoPreco = precoAtual * (1 + percentual / 100);
        item.definirPreco(novoPreco);
        System.out.println("  Preço aumentado de R$ " + String.format("%.2f", precoAtual) + " para R$ "
                + String.format("%.2f", novoPreco));
    }

    @Override
    public String obterNome() {
        return "Aumentar Preço";
    }

    @Override
    public String obterDescricao() {
        return "Aumentar preço de " + item.obterNome() + " em " + percentual + "%";
    }
}