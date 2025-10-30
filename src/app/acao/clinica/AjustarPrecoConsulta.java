package app.acao.clinica;

import app.acao.Acao;

public class AjustarPrecoConsulta extends Acao {
    double precoAtual;
    double novoPreco;
    String motivo;

    public AjustarPrecoConsulta(double precoAtual, double novoPreco, String motivo) {
        this.precoAtual = precoAtual;
        this.novoPreco = novoPreco;
        this.motivo = motivo;
    }

    @Override
    public void executar() {
        System.out.println("  Preço de consulta ajustado de R$ " + String.format("%.2f", precoAtual) + " para R$ " + String.format("%.2f", novoPreco));
        System.out.println("  Motivo: " + motivo);
    }

    @Override
    public String obterNome() {
        return "Ajustar Preço de Consulta";
    }

    @Override
    public String obterDescricao() {
        return "Ajustar preço de consultas para R$ " + String.format("%.2f", novoPreco) + " - " + motivo;
    }
}