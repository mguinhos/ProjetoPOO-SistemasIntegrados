package app.eventos;

import app.Estoque;
import app.Agenda;

import app.sistema.Negocio;

import java.util.Vector;

public class Eventos implements Negocio  {
    Estoque estoque;
    Agenda agenda;
    Vector<Evento> eventos;
    double caixa;

    public Eventos() {
        this.estoque = new Estoque();
        this.agenda = new Agenda();
        this.eventos = new Vector<Evento>();
        this.caixa = 3000.0;
    }

    public Estoque obterEstoque() {
        return this.estoque;
    }

    public Agenda obterAgenda() {
        return this.agenda;
    }

    public Vector<Evento> obterEventos() {
        return this.eventos;
    }

    public double obterCaixa() {
        return this.caixa;
    }

    public void adicionarDinheiroNoCaixa(double valor) {
        this.caixa += valor;
    }

    public boolean removerDinheiroNoCaixa(double valor) {
        if (this.caixa >= valor) {
            this.caixa -= valor;
            return true;
        }
        return false;
    }

    public Evento criarEvento(String nome, String descricao, java.util.Date data, TipoEvento tipo, int capacidadeMaxima, double custoOrganizacao) {
        Evento evento = new Evento(nome, descricao, data, tipo, capacidadeMaxima, custoOrganizacao);
        this.eventos.add(evento);
        return evento;
    }

    public Ingresso criarIngresso(Evento evento, double preco, String setor) {
        if (evento.possuiIngressosDisponiveis()) {
            Ingresso ingresso = new Ingresso(evento, preco, setor);
            evento.obterIngressos().add(ingresso);
            return ingresso;
        }
        return null;
    }

    public boolean venderIngresso(Ingresso ingresso, app.Cliente cliente) {
        if (ingresso.vender(cliente)) {
            this.adicionarDinheiroNoCaixa(ingresso.obterPreco());
            return true;
        }
        return false;
    }

    public double getCaixa() {
        return this.caixa;
    }

    @Override
    public String obterNome() {
        return "Eventos";
    }
}