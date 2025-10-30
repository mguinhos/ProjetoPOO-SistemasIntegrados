package app.acao.geral;

import app.acao.Acao;

public class EmergenciaFinanceira extends Acao {
    String nomeNegocio;
    double saldoAtual;

    public EmergenciaFinanceira(String nomeNegocio, double saldoAtual) {
        this.nomeNegocio = nomeNegocio;
        this.saldoAtual = saldoAtual;
    }

    @Override
    public void executar() {
        System.out.println("  ⚠️ EMERGÊNCIA FINANCEIRA DECLARADA!");
        System.out.println("  " + nomeNegocio + " está com saldo crítico: R$ " + 
                         String.format("%.2f", saldoAtual));
        System.out.println("  Ações recomendadas: Reduzir custos, aumentar preços, buscar empréstimos");
    }

    @Override
    public String obterNome() {
        return "Declarar Emergência Financeira";
    }

    @Override
    public String obterDescricao() {
        return "O negócio está em situação financeira crítica e precisa de ações imediatas";
    }
}
