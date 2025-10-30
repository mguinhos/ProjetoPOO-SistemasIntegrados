package app.clinica;

import app.Cliente;
import java.util.Date;
import java.util.Vector;

public class Consulta {
    Cliente paciente;
    Date dataConsulta;
    String diagnostico;
    Vector<Remedio> remediosPrescritos;
    Vector<Exame> examesSolicitados;
    double valorConsulta;
    boolean realizada;

    public Consulta(Cliente paciente, double valorConsulta) {
        this.paciente = paciente;
        this.dataConsulta = new Date();
        this.valorConsulta = valorConsulta;
        this.diagnostico = "";
        this.remediosPrescritos = new Vector<Remedio>();
        this.examesSolicitados = new Vector<Exame>();
        this.realizada = false;
    }

    public Cliente obterPaciente() {
        return this.paciente;
    }

    public Date obterDataConsulta() {
        return this.dataConsulta;
    }

    public String obterDiagnostico() {
        return this.diagnostico;
    }

    public double obterValorConsulta() {
        return this.valorConsulta;
    }

    public boolean foiRealizada() {
        return this.realizada;
    }

    public void definirDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public void prescreverRemedio(Remedio remedio) {
        this.remediosPrescritos.add(remedio);
    }

    public void solicitarExame(Exame exame) {
        this.examesSolicitados.add(exame);
    }

    public Vector<Remedio> obterRemediosPrescritos() {
        return this.remediosPrescritos;
    }

    public Vector<Exame> obterExamesSolicitados() {
        return this.examesSolicitados;
    }

    public void finalizar() {
        this.realizada = true;
    }
}