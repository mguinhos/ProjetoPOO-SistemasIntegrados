package app.acao.clinica;

import app.acao.Acao;
import app.clinica.Clinica;

public class ContratarMedico extends Acao {
    Clinica clinica;
    String nome;
    String especialidade;
    double salario;

    public ContratarMedico(Clinica clinica, String nome, String especialidade, double salario) {
        this.clinica = clinica;
        this.nome = nome;
        this.especialidade = especialidade;
        this.salario = salario;
    }

    @Override
    public void executar() {
        System.out.println("  Médico(a) contratado(a): " + nome);
        System.out.println("  Especialidade: " + especialidade);
        System.out.println("  Salário mensal: R$ " + String.format("%.2f", salario));
    }

    @Override
    public String obterNome() {
        return "Contratar Médico";
    }

    @Override
    public String obterDescricao() {
        return "Contratar Dr(a). " + nome + " (" + especialidade + ")";
    }
}