package app;

public class Recurso {
    String nome;
    String descricao;
    double custo_de_obtencao;

    public Recurso(String nome, String descricao, double custo_de_obtencao) {
        this.nome = nome;
        this.descricao = descricao;
        this.custo_de_obtencao = custo_de_obtencao;
    }

    public String obterNome() {
        return this.nome;
    }

    public String obterDescricao() {
        return this.descricao;
    }

    public double obterCustoDeObtencao() {
        return this.custo_de_obtencao;
    }
}