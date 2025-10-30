package app;

public class Cliente {
    String nome;
    int idade;
    double dinheiro;
    
    public Cliente(String nome, int idade) {
        this.nome = nome;
        this.idade = idade;
        this.dinheiro = 100.0;
    }

    public Cliente(String nome, int idade, double dinheiro) {
        this.nome = nome;
        this.idade = idade;
        this.dinheiro = dinheiro;
    }

    public String obterNome() {
        return this.nome;
    }

    public int obterIdade() {
        return this.idade;
    }

    public double obterDinheiro() {
        return this.dinheiro;
    }

    public boolean possuiDinheiro(double valor) {
        return this.dinheiro >= valor;
    }

    public boolean pagar(double valor) {
        if (this.dinheiro >= valor) {
            this.dinheiro -= valor;
            return true;
        }
        return false;
    }

    public void adicionarDinheiro(double valor) {
        this.dinheiro += valor;
    }
}