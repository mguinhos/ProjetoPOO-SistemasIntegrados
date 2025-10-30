package app.acao.clinica;

import app.acao.Acao;
import app.clinica.Clinica;

public class AgendarManutencao extends Acao {
    Clinica clinica;
    double custo;
    String descricao;

    public AgendarManutencao(Clinica clinica, double custo, String descricao) {
        this.clinica = clinica;
        this.custo = custo;
        this.descricao = descricao;
    }

    @Override
    public void executar() {
        if (clinica.removerDinheiroNoCaixa(custo)) {
            System.out.println("  Manutenção agendada: " + descricao);
            System.out.println("  Custo: R$ " + String.format("%.2f", custo));
        } else {
            System.out.println("  FALHA: Dinheiro insuficiente para manutenção");
        }
    }

    @Override
    public String obterNome() {
        return "Agendar Manutenção";
    }

    @Override
    public String obterDescricao() {
        return "Agendar manutenção: " + descricao + " (R$ " + String.format("%.2f", custo) + ")";
    }
}