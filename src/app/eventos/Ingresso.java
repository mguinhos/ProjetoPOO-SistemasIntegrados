package app.eventos;

import app.Cliente;

public class Ingresso {
    Evento evento;
    Cliente cliente;
    double preco;
    String setor;
    boolean vendido;

    public Ingresso(Evento evento, double preco, String setor) {
        this.evento = evento;
        this.preco = preco;
        this.setor = setor;
        this.cliente = null;
        this.vendido = false;
    }

    public Evento obterEvento() {
        return this.evento;
    }

    public Cliente obterCliente() {
        return this.cliente;
    }

    public double obterPreco() {
        return this.preco;
    }

    public String obterSetor() {
        return this.setor;
    }

    public boolean foiVendido() {
        return this.vendido;
    }

    public boolean vender(Cliente cliente) {
        if (!this.vendido && cliente.pagar(this.preco)) {
            this.cliente = cliente;
            this.vendido = true;
            return true;
        }
        return false;
    }
}