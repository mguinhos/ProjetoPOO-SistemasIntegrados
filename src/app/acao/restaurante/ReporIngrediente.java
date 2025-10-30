package app.acao.restaurante;

import app.acao.Acao;
import app.restaurante.Restaurante;
import app.restaurante.Ingrediente;

public class ReporIngrediente extends Acao {
    Restaurante restaurante;
    Ingrediente ingrediente;
    int quantidade;

    public ReporIngrediente(Restaurante restaurante, Ingrediente ingrediente, int quantidade) {
        this.restaurante = restaurante;
        this.ingrediente = ingrediente;
        this.quantidade = quantidade;
    }

    @Override
    public void executar() {
        double custo = ingrediente.obterCustoDeObtencao() * quantidade;
        if (restaurante.removerDinheiroNoCaixa(custo)) {
            restaurante.obterEstoque().adicionarItem(ingrediente, quantidade);
            System.out.println("  Reposto " + quantidade + "x " + ingrediente.obterNome() + 
                             " por R$ " + String.format("%.2f", custo));
        } else {
            System.out.println("  FALHA: Dinheiro insuficiente para repor " + ingrediente.obterNome());
        }
    }

    @Override
    public String obterNome() {
        return "Repor Ingrediente";
    }

    @Override
    public String obterDescricao() {
        return "Repor " + quantidade + "x " + ingrediente.obterNome() + " no estoque";
    }
}