package app;

import java.util.Date;

public class EventoAgenda {
    Date data;
    String descricao;
    String tipo;

    public EventoAgenda(Date data, String descricao, String tipo) {
        this.data = data;
        this.descricao = descricao;
        this.tipo = tipo;
    }

    public Date obterData() {
        return this.data;
    }

    public String obterDescricao() {
        return this.descricao;
    }

    public String obterTipo() {
        return this.tipo;
    }
}