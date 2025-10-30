package app.acao.geral;

import app.acao.Acao;

public class RealizarPromocaoGeral extends Acao {
    String descricaoPromocao;
    double percentualDesconto;
    int duracaoDias;

    public RealizarPromocaoGeral(String descricaoPromocao, double percentualDesconto, int duracaoDias) {
        this.descricaoPromocao = descricaoPromocao;
        this.percentualDesconto = percentualDesconto;
        this.duracaoDias = duracaoDias;
    }

    @Override
    public void executar() {
        System.out.println("  🎉 PROMOÇÃO ATIVA: " + descricaoPromocao);
        System.out.println("  Desconto: " + String.format("%.0f", percentualDesconto) + "%");
        System.out.println("  Duração: " + duracaoDias + " dias");
    }

    @Override
    public String obterNome() {
        return "Realizar Promoção Geral";
    }

    @Override
    public String obterDescricao() {
        return descricaoPromocao + " - " + String.format("%.0f", percentualDesconto) + "% de desconto por " + duracaoDias + " dias";
    }
}