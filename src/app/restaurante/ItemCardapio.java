package app.restaurante;

public class ItemCardapio {
    Consumivel consumivel;
    double preco;
    double avaliacaoMedia;
    int numeroAvaliacoes;

    public ItemCardapio(double preco, Consumivel consumivel) {
        this.preco = preco;
        this.consumivel = consumivel;
        this.avaliacaoMedia = 0.5;
        this.numeroAvaliacoes = 0;
    }

    public double obterPreco() {
        return this.preco;
    }

    public void definirPreco(double preco) {
        this.preco = preco;
    }

    public Consumivel obterConsumivel() {
        return this.consumivel;
    }

    public String obterNome() {
        return this.consumivel.obterNome();
    }

    public double obterCustoDeProducao() {
        return this.consumivel.obterCustoDeProducao();
    }

    public double obterLucro() {
        return this.preco - this.obterCustoDeProducao();
    }

    public void avaliar(double nota) {
        this.avaliacaoMedia = ((this.avaliacaoMedia * this.numeroAvaliacoes) + nota) / (this.numeroAvaliacoes + 1);
        this.numeroAvaliacoes++;
    }

    public double obterAvaliacao() {
        return this.avaliacaoMedia;
    }

    public int obterNumeroAvaliacoes() {
        return this.numeroAvaliacoes;
    }
}