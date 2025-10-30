package app.acao.eventos;

import app.acao.Acao;
import app.eventos.Eventos;
import app.eventos.Evento;

public class InvestirEmMarketing extends Acao {
    Eventos eventos;
    Evento evento;
    double valorInvestimento;

    public InvestirEmMarketing(Eventos eventos, Evento evento, double valorInvestimento) {
        this.eventos = eventos;
        this.evento = evento;
        this.valorInvestimento = valorInvestimento;
    }

    @Override
    public void executar() {
        if (eventos.removerDinheiroNoCaixa(valorInvestimento)) {
            System.out.println("  Investimento em marketing para " + evento.obterNome() + 
                             ": R$ " + String.format("%.2f", valorInvestimento));
            System.out.println("  Expectativa: Aumento de 20-30% nas vendas");
        } else {
            System.out.println("  FALHA: Dinheiro insuficiente para investimento em marketing");
        }
    }

    @Override
    public String obterNome() {
        return "Investir em Marketing";
    }

    @Override
    public String obterDescricao() {
        return "Investir R$ " + String.format("%.2f", valorInvestimento) + 
               " em marketing para " + evento.obterNome();
    }
}