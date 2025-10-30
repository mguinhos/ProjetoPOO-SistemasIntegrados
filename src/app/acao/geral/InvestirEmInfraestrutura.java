package app.acao.geral;

import app.acao.Acao;

public class InvestirEmInfraestrutura extends Acao {
    String tipoMelhoria;
    double custo;
    String beneficioEsperado;

    public InvestirEmInfraestrutura(String tipoMelhoria, double custo, String beneficioEsperado) {
        this.tipoMelhoria = tipoMelhoria;
        this.custo = custo;
        this.beneficioEsperado = beneficioEsperado;
    }

    @Override
    public void executar() {
        System.out.println("  Investimento em infraestrutura: " + tipoMelhoria);
        System.out.println("  Custo: R$ " + String.format("%.2f", custo));
        System.out.println("  Benefício esperado: " + beneficioEsperado);
    }

    @Override
    public String obterNome() {
        return "Investir em Infraestrutura";
    }

    @Override
    public String obterDescricao() {
        return "Investir em " + tipoMelhoria + " (R$ " + String.format("%.2f", custo) + 
               ") - " + beneficioEsperado;
    }
}