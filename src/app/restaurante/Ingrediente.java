package app.restaurante;

import app.Recurso;

public class Ingrediente extends Recurso {
    public Ingrediente(String nome, double custo) {
        super(nome, "Ingrediente para preparação de alimentos", custo);
    }

    public Ingrediente(String nome, String descricao, double custo) {
        super(nome, descricao, custo);
    }
}