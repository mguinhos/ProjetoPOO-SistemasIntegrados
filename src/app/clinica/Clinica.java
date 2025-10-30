package app.clinica;

import app.Estoque;
import app.Agenda;

import app.sistema.Negocio;

import java.util.Vector;

public class Clinica implements Negocio {
    Estoque estoque;
    Agenda agenda;
    Vector<Consulta> consultas;
    Vector<Exame> exames;
    double caixa;

    public Clinica() {
        this.estoque = new Estoque();
        this.agenda = new Agenda();
        this.consultas = new Vector<Consulta>();
        this.exames = new Vector<Exame>();
        this.caixa = 5000.0;
    }

    public Estoque obterEstoque() {
        return this.estoque;
    }

    public Agenda obterAgenda() {
        return this.agenda;
    }

    public Vector<Consulta> obterConsultas() {
        return this.consultas;
    }

    public Vector<Exame> obterExames() {
        return this.exames;
    }

    public double obterCaixa() {
        return this.caixa;
    }

    public void adicionarDinheiroNoCaixa(double valor) {
        this.caixa += valor;
    }

    public boolean removerDinheiroNoCaixa(double valor) {
        if (this.caixa >= valor) {
            this.caixa -= valor;
            return true;
        }
        return false;
    }

    public Consulta agendarConsulta(app.Cliente paciente, double valorConsulta) {
        Consulta consulta = new Consulta(paciente, valorConsulta);
        this.consultas.add(consulta);
        return consulta;
    }

    public Exame solicitarExame(app.Cliente paciente, TipoExame tipo, double custo) {
        Exame exame = new Exame(paciente, tipo, custo);
        this.exames.add(exame);
        return exame;
    }

    public boolean processarPagamentoConsulta(Consulta consulta) {
        if (consulta.obterPaciente().pagar(consulta.obterValorConsulta())) {
            this.adicionarDinheiroNoCaixa(consulta.obterValorConsulta());
            return true;
        }
        return false;
    }

    public boolean processarPagamentoExame(Exame exame) {
        if (exame.obterPaciente().pagar(exame.obterCusto())) {
            this.adicionarDinheiroNoCaixa(exame.obterCusto());
            return true;
        }
        return false;
    }

    public double getCaixa() {
        return this.caixa;
    }

    @Override
    public String obterNome() {
        return "Clínica";
    }
}