package app.acao.clinica;

import app.acao.Acao;
import app.clinica.Clinica;
import app.clinica.Remedio;

public class ReporRemedio extends Acao {
    Clinica clinica;
    Remedio remedio;
    int quantidade;

    public ReporRemedio(Clinica clinica, Remedio remedio, int quantidade) {
        this.clinica = clinica;
        this.remedio = remedio;
        this.quantidade = quantidade;
    }

    @Override
    public void executar() {
        double custo = remedio.obterCustoDeObtencao() * quantidade;
        if (clinica.removerDinheiroNoCaixa(custo)) {
            clinica.obterEstoque().adicionarItem(remedio, quantidade);
            System.out.println("  Reposto " + quantidade + "x " + remedio.obterNome() + 
                             " por R$ " + String.format("%.2f", custo));
        } else {
            System.out.println("  FALHA: Dinheiro insuficiente para repor " + remedio.obterNome());
        }
    }

    @Override
    public String obterNome() {
        return "Repor Remédio";
    }

    @Override
    public String obterDescricao() {
        return "Repor " + quantidade + "x " + remedio.obterNome() + " no estoque";
    }
}