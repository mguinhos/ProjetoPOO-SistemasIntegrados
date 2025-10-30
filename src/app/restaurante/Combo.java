package app.restaurante;

import java.util.Vector;

public class Combo extends Consumivel {
    String nome;
    Vector<Consumivel> itens;

    public Combo(String nome, Vector<Consumivel> itens) {
        this.nome = nome;
        this.itens = itens;
    }

    @Override
    public String obterNome() {
        return this.nome;
    }

    @Override
    public Vector<Ingrediente> obterIngredientes() {
        Vector<Ingrediente> todosIngredientes = new Vector<Ingrediente>();
        for (Consumivel item : itens) {
            todosIngredientes.addAll(item.obterIngredientes());
        }
        return todosIngredientes;
    }

    @Override
    public double obterCustoDeProducao() {
        double custo = 0.0;
        for (Consumivel item : itens) {
            custo += item.obterCustoDeProducao();
        }
        return custo;
    }

    public Vector<Consumivel> obterItens() {
        return this.itens;
    }
}