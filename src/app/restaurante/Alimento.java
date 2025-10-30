package app.restaurante;

import java.util.Vector;

public class Alimento extends Consumivel {
    String nome;
    Vector<Ingrediente> ingredientes;

    public Alimento(String nome, Vector<Ingrediente> ingredientes) {
        this.nome = nome;
        this.ingredientes = ingredientes;
    }

    @Override
    public String obterNome() {
        return this.nome;
    }

    @Override
    public Vector<Ingrediente> obterIngredientes() {
        return this.ingredientes;
    }

    @Override
    public double obterCustoDeProducao() {
        double custo = 0.0;
        for (Ingrediente ingrediente : ingredientes) {
            custo += ingrediente.obterCustoDeObtencao();
        }
        return custo;
    }
}