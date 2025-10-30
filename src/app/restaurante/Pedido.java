package app.restaurante;

import app.Cliente;

public class Pedido {
    Cliente cliente;
    ItemCardapio item;
    boolean finalizado;

    public Pedido(Cliente cliente, ItemCardapio item) {
        this.cliente = cliente;
        this.item = item;
        this.finalizado = false;
    }

    public Cliente obterCliente() {
        return this.cliente;
    }

    public ItemCardapio obterItem() {
        return this.item;
    }

    public boolean estFinalizado() {
        return this.finalizado;
    }

    public void finalizar() {
        this.finalizado = true;
    }
}