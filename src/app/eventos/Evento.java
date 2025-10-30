package app.eventos;

import java.util.Date;
import java.util.Vector;

public class Evento {
    String nome;
    String descricao;
    Date data;
    TipoEvento tipo;
    int capacidadeMaxima;
    Vector<Ingresso> ingressos;
    double custoOrganizacao;

    public Evento(String nome, String descricao, Date data, TipoEvento tipo, int capacidadeMaxima, double custoOrganizacao) {
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
        this.tipo = tipo;
        this.capacidadeMaxima = capacidadeMaxima;
        this.custoOrganizacao = custoOrganizacao;
        this.ingressos = new Vector<Ingresso>();
    }

    public String obterNome() {
        return this.nome;
    }

    public String obterDescricao() {
        return this.descricao;
    }

    public Date obterData() {
        return this.data;
    }

    public TipoEvento obterTipo() {
        return this.tipo;
    }

    public int obterCapacidadeMaxima() {
        return this.capacidadeMaxima;
    }

    public double obterCustoOrganizacao() {
        return this.custoOrganizacao;
    }

    public Vector<Ingresso> obterIngressos() {
        return this.ingressos;
    }

    public int obterIngressosVendidos() {
        int vendidos = 0;
        for (Ingresso ingresso : ingressos) {
            if (ingresso.foiVendido()) {
                vendidos++;
            }
        }
        return vendidos;
    }

    public int obterIngressosDisponiveis() {
        return this.capacidadeMaxima - this.obterIngressosVendidos();
    }

    public boolean possuiIngressosDisponiveis() {
        return this.obterIngressosDisponiveis() > 0;
    }
}