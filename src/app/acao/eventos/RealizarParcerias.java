package app.acao.eventos;

import app.acao.Acao;
import app.eventos.Eventos;

public class RealizarParcerias extends Acao {
    Eventos eventos;
    String parceiro;
    double custo;

    public RealizarParcerias(Eventos eventos, String parceiro, double custo) {
        this.eventos = eventos;
        this.parceiro = parceiro;
        this.custo = custo;
    }

    @Override
    public void executar() {
        if (eventos.removerDinheiroNoCaixa(custo)) {
            System.out.println("  Parceria estabelecida com " + parceiro);
            System.out.println("  Investimento: R$ " + String.format("%.2f", custo));
        } else {
            System.out.println("  FALHA: Dinheiro insuficiente para estabelecer parceria");
        }
    }

    @Override
    public String obterNome() {
        return "Realizar Parcerias";
    }

    @Override
    public String obterDescricao() {
        return "Estabelecer parceria com " + parceiro + " (R$ " + String.format("%.2f", custo) + ")";
    }
}