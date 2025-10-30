package app.acao.eventos;

import app.acao.Acao;
import app.eventos.Evento;

public class AjustarPrecoIngresso extends Acao {
    Evento evento;
    double percentualAjuste;
    String setor;

    public AjustarPrecoIngresso(Evento evento, String setor, double percentualAjuste) {
        this.evento = evento;
        this.setor = setor;
        this.percentualAjuste = percentualAjuste;
    }

    @Override
    public void executar() {
        System.out.println("  Preços do setor " + setor + " do evento " + evento.obterNome() + 
                         " ajustados em " + percentualAjuste + "%");
    }

    @Override
    public String obterNome() {
        return "Ajustar Preço de Ingresso";
    }

    @Override
    public String obterDescricao() {
        return "Ajustar preço de ingressos " + setor + " do evento " + evento.obterNome() + 
               " em " + percentualAjuste + "%";
    }
}