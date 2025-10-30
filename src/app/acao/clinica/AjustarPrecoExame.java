package app.acao.clinica;

import app.acao.Acao;
import app.clinica.TipoExame;

public class AjustarPrecoExame extends Acao {
    TipoExame tipoExame;
    double precoAtual;
    double novoPreco;

    public AjustarPrecoExame(TipoExame tipoExame, double precoAtual, double novoPreco) {
        this.tipoExame = tipoExame;
        this.precoAtual = precoAtual;
        this.novoPreco = novoPreco;
    }

    @Override
    public void executar() {
        System.out.println("  Preço de " + tipoExame + " ajustado de R$ " + String.format("%.2f", precoAtual) + " para R$ " + String.format("%.2f", novoPreco));
    }

    @Override
    public String obterNome() {
        return "Ajustar Preço de Exame";
    }

    @Override
    public String obterDescricao() {
        return "Ajustar preço de " + tipoExame + " para R$ " + String.format("%.2f", novoPreco);
    }
}