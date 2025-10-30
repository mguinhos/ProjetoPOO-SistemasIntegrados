package app.sistema;

public interface Negocio {
    double obterCaixa();
    boolean removerDinheiroNoCaixa(double valor);
    void adicionarDinheiroNoCaixa(double valor);
    String obterNome();
}