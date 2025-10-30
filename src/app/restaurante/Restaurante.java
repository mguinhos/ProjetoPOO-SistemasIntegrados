package app.restaurante;

import app.Estoque;

import app.sistema.Negocio;

import java.util.Vector;

public class Restaurante implements Negocio  {
    Estoque estoque;
    Cardapio cardapio;
    Vector<Pedido> pedidos;
    double caixa;

    public Restaurante() {
        this.estoque = new Estoque();
        this.cardapio = new Cardapio();
        this.pedidos = new Vector<Pedido>();
        this.caixa = 1000.0;
    }

    public Estoque obterEstoque() {
        return this.estoque;
    }

    public Cardapio obterCardapio() {
        return this.cardapio;
    }

    public Vector<Pedido> obterPedidos() {
        return this.pedidos;
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

    public Pedido criarPedido(app.Cliente cliente, ItemCardapio item) {
        Pedido pedido = new Pedido(cliente, item);
        this.pedidos.add(pedido);
        return pedido;
    }

    public boolean processarPagamento(Pedido pedido) {
        if (pedido.obterCliente().pagar(pedido.obterItem().obterPreco())) {
            this.adicionarDinheiroNoCaixa(pedido.obterItem().obterPreco());
            pedido.finalizar();
            return true;
        }
        return false;
    }

    public double getCaixa() {
        return this.caixa;
    }

    public Cardapio getCardapio() {
        return this.cardapio;
    }

    public Estoque getEstoque() {
        return this.estoque;
    }

    @Override
    public String obterNome() {
        return "Restaurante";
    }
}