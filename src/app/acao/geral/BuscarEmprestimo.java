package app.acao.geral;

import app.acao.Acao;

public class BuscarEmprestimo extends Acao {
    double valorEmprestimo;
    double taxaJuros;

    public BuscarEmprestimo(double valorEmprestimo, double taxaJuros) {
        this.valorEmprestimo = valorEmprestimo;
        this.taxaJuros = taxaJuros;
    }

    @Override
    public void executar() {
        System.out.println("  Empréstimo solicitado: R$ " + String.format("%.2f", valorEmprestimo));
        System.out.println("  Taxa de juros: " + String.format("%.1f", taxaJuros) + "% ao mês");
        double valorTotal = valorEmprestimo * (1 + taxaJuros / 100);
        System.out.println("  Valor a pagar: R$ " + String.format("%.2f", valorTotal));
    }

    @Override
    public String obterNome() {
        return "Buscar Empréstimo";
    }

    @Override
    public String obterDescricao() {
        return "Solicitar empréstimo de R$ " + String.format("%.2f", valorEmprestimo) + 
               " com juros de " + String.format("%.1f", taxaJuros) + "%";
    }
}