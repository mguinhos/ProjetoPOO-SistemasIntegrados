package app.clinica;

import app.Cliente;
import java.util.Date;

public class Exame {
    Cliente paciente;
    TipoExame tipo;
    Date dataRealizacao;
    String resultado;
    double custo;
    boolean realizado;

    public Exame(Cliente paciente, TipoExame tipo, double custo) {
        this.paciente = paciente;
        this.tipo = tipo;
        this.custo = custo;
        this.dataRealizacao = new Date();
        this.resultado = "";
        this.realizado = false;
    }

    public Cliente obterPaciente() {
        return this.paciente;
    }

    public TipoExame obterTipo() {
        return this.tipo;
    }

    public Date obterDataRealizacao() {
        return this.dataRealizacao;
    }

    public String obterResultado() {
        return this.resultado;
    }

    public double obterCusto() {
        return this.custo;
    }

    public boolean foiRealizado() {
        return this.realizado;
    }

    public void definirResultado(String resultado) {
        this.resultado = resultado;
        this.realizado = true;
    }
}