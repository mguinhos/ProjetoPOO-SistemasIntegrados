package app;

import java.util.Vector;
import java.util.Date;

public class Agenda {
    Vector<EventoAgenda> eventos;

    public Agenda() {
        this.eventos = new Vector<EventoAgenda>();
    }

    public void adicionarEvento(EventoAgenda evento) {
        this.eventos.add(evento);
    }

    public void removerEvento(EventoAgenda evento) {
        this.eventos.remove(evento);
    }

    public Vector<EventoAgenda> obterEventos() {
        return this.eventos;
    }

    public Vector<EventoAgenda> obterEventosPorData(Date data) {
        Vector<EventoAgenda> eventosNaData = new Vector<EventoAgenda>();
        for (EventoAgenda evento : eventos) {
            if (evento.obterData().equals(data)) {
                eventosNaData.add(evento);
            }
        }
        return eventosNaData;
    }

    public boolean possuiEventoNaData(Date data) {
        for (EventoAgenda evento : eventos) {
            if (evento.obterData().equals(data)) {
                return true;
            }
        }
        return false;
    }
}