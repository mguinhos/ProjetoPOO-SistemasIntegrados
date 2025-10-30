package app.acao.clinica;

import app.acao.Acao;
import app.clinica.Clinica;

public class ComprarEquipamento extends Acao {
    Clinica clinica;
    String equipamento;
    double custo;

    public ComprarEquipamento(Clinica clinica, String equipamento, double custo) {
        this.clinica = clinica;
        this.equipamento = equipamento;
        this.custo = custo;
    }

    @Override
    public void executar() {
        if (clinica.removerDinheiroNoCaixa(custo)) {
            System.out.println("  Equipamento adquirido: " + equipamento);
            System.out.println("  Custo: R$ " + String.format("%.2f", custo));
        } else {
            System.out.println("  FALHA: Dinheiro insuficiente para comprar equipamento");
        }
    }

    @Override
    public String obterNome() {
        return "Comprar Equipamento";
    }

    @Override
    public String obterDescricao() {
        return "Adquirir " + equipamento + " (R$ " + String.format("%.2f", custo) + ")";
    }
}