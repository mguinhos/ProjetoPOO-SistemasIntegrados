package app.clinica;

import app.Recurso;

public class Remedio extends Recurso {
    String principioAtivo;
    String fabricante;

    public Remedio(String nome, String descricao, double custo, String principioAtivo, String fabricante) {
        super(nome, descricao, custo);
        this.principioAtivo = principioAtivo;
        this.fabricante = fabricante;
    }

    public String obterPrincipioAtivo() {
        return this.principioAtivo;
    }

    public String obterFabricante() {
        return this.fabricante;
    }
}