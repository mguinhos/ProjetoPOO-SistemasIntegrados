package app.acao.restaurante;

import app.acao.Acao;

import app.restaurante.Cardapio;
import app.restaurante.ItemCardapio;

public class BaixarPreco extends Acao {
    Cardapio cardapio;
    ItemCardapio item;
    double percentual;

    public BaixarPreco(Cardapio cardapio, ItemCardapio item, double percentual) {
        this.cardapio = cardapio;
        this.item = item;
        this.percentual = percentual;
    }

    @Override
    public void executar() {
        double precoAtual = item.obterPreco();
        double novoPreco = precoAtual * (1 - percentual / 100);
        item.definirPreco(novoPreco);
        System.out.println("  Preço reduzido de R$ " + String.format("%.2f", precoAtual) + " para R$ " + String.format("%.2f", novoPreco));
    }

    @Override
    public String obterNome() {
        return "Baixar Preço";
    }

    @Override
    public String obterDescricao() {
        return "Reduzir preço de " + item.obterNome() + " em " + percentual + "%";
    }
}