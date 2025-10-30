package app.restaurante;

import java.util.Vector;

public abstract class Consumivel {
    public abstract String obterNome();
    public abstract Vector<Ingrediente> obterIngredientes();
    public abstract double obterCustoDeProducao();
}