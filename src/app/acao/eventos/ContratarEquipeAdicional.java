package app.acao.eventos;

import app.acao.Acao;
import app.eventos.Eventos;
import app.eventos.Evento;

public class ContratarEquipeAdicional extends Acao {
    Eventos eventos;
    Evento evento;
    double custo;
    int numeroFuncionarios;

    public ContratarEquipeAdicional(Eventos eventos, Evento evento, int numeroFuncionarios, double custo) {
        this.eventos = eventos;
        this.evento = evento;
        this.numeroFuncionarios = numeroFuncionarios;
        this.custo = custo;
    }

    @Override
    public void executar() {
        if (eventos.removerDinheiroNoCaixa(custo)) {
            System.out.println("  Contratados " + numeroFuncionarios + " funcionários adicionais para " + 
                             evento.obterNome());
            System.out.println("  Custo: R$ " + String.format("%.2f", custo));
        } else {
            System.out.println("  FALHA: Dinheiro insuficiente para contratar equipe");
        }
    }

    @Override
    public String obterNome() {
        return "Contratar Equipe Adicional";
    }

    @Override
    public String obterDescricao() {
        return "Contratar " + numeroFuncionarios + " funcionários para " + evento.obterNome() + 
               " (R$ " + String.format("%.2f", custo) + ")";
    }
}