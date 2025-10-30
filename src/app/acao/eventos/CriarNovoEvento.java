package app.acao.eventos;

import app.acao.Acao;
import app.eventos.Eventos;
import app.eventos.TipoEvento;

public class CriarNovoEvento extends Acao {
    Eventos eventos;
    String nomeEvento;
    TipoEvento tipo;
    int capacidade;

    public CriarNovoEvento(Eventos eventos, String nomeEvento, TipoEvento tipo, int capacidade) {
        this.eventos = eventos;
        this.nomeEvento = nomeEvento;
        this.tipo = tipo;
        this.capacidade = capacidade;
    }

    @Override
    public void executar() {
        System.out.println("  Novo evento criado: " + nomeEvento);
        System.out.println("  Tipo: " + tipo);
        System.out.println("  Capacidade: " + capacidade + " pessoas");
    }

    @Override
    public String obterNome() {
        return "Criar Novo Evento";
    }

    @Override
    public String obterDescricao() {
        return "Criar evento " + nomeEvento + " (" + tipo + ") para " + capacidade + " pessoas";
    }
}