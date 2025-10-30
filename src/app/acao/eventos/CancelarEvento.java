package app.acao.eventos;

import app.acao.Acao;
import app.eventos.Eventos;
import app.eventos.Evento;

public class CancelarEvento extends Acao {
    Eventos eventos;
    Evento evento;

    public CancelarEvento(Eventos eventos, Evento evento) {
        this.eventos = eventos;
        this.evento = evento;
    }

    @Override
    public void executar() {
        System.out.println("  Evento " + evento.obterNome() + " cancelado");
        System.out.println("  Motivo: Baixa venda de ingressos ou problemas operacionais");
        
        // Reembolsar ingressos vendidos
        int ingressosVendidos = evento.obterIngressosVendidos();
        if (ingressosVendidos > 0) {
            System.out.println("  Processando reembolso de " + ingressosVendidos + " ingressos...");
        }
    }

    @Override
    public String obterNome() {
        return "Cancelar Evento";
    }

    @Override
    public String obterDescricao() {
        return "Cancelar evento " + evento.obterNome() + " (baixo desempenho)";
    }
}