package app.sistema;

import app.eventos.Eventos;
import app.eventos.Evento;
import app.eventos.Ingresso;
import app.eventos.TipoEvento;
import app.Cliente;
import app.acao.Acao;
import app.acao.eventos.*;
import app.acao.geral.EmergenciaFinanceira;

import java.util.Vector;
import java.util.Random;

public class SistemaEventos extends Sistema<Eventos> {
    private int ingressosVendidos = 0;
    private double receitaDia = 0.0;

    public SistemaEventos(Eventos eventos) {
        super(eventos);
    }

    @Override
    protected void simularOperacoesDoDia() {
        System.out.println("\n=== OPERAÇÕES DO DIA ===");
        ingressosVendidos = 0;
        receitaDia = 0.0;

        Vector<Evento> eventosDisponiveis = negocio.obterEventos();
        
        if (eventosDisponiveis.isEmpty()) {
            System.out.println("Nenhum evento disponível hoje.");
            return;
        }

        Vector<Cliente> clientes = gerarClientesRandomicos();
        System.out.println("Clientes interessados: " + clientes.size());

        Random random = new Random();
        
        for (Cliente cliente : clientes) {
            Evento eventoEscolhido = eventosDisponiveis.get(random.nextInt(eventosDisponiveis.size()));
            
            if (!eventoEscolhido.possuiIngressosDisponiveis()) {
                continue;
            }

            Vector<Ingresso> ingressos = eventoEscolhido.obterIngressos();
            Ingresso ingressoDisponivel = null;
            
            for (Ingresso ing : ingressos) {
                if (!ing.foiVendido()) {
                    ingressoDisponivel = ing;
                    break;
                }
            }

            if (ingressoDisponivel != null && cliente.possuiDinheiro(ingressoDisponivel.obterPreco())) {
                if (negocio.venderIngresso(ingressoDisponivel, cliente)) {
                    ingressosVendidos++;
                    receitaDia += ingressoDisponivel.obterPreco();
                }
            }
        }
    }

    @Override
    protected void exibirResumo() {
        System.out.println("\n=== RESUMO DO DIA ===");
        System.out.println("Ingressos vendidos: " + ingressosVendidos);
        System.out.println("Receita do dia: R$ " + String.format("%.2f", receitaDia));
        System.out.println("Dinheiro em caixa: R$ " + String.format("%.2f", negocio.obterCaixa()));
        
        System.out.println("\n=== STATUS DOS EVENTOS ===");
        for (Evento evento : negocio.obterEventos()) {
            System.out.println("Evento: " + evento.obterNome());
            System.out.println("  Ingressos vendidos: " + evento.obterIngressosVendidos() + "/" + 
                             evento.obterCapacidadeMaxima());
            System.out.println("  Ocupação: " + 
                             String.format("%.1f", (double)evento.obterIngressosVendidos() / 
                             evento.obterCapacidadeMaxima() * 100) + "%");
        }
    }

    private Vector<Cliente> gerarClientesRandomicos() {
        Vector<Cliente> clientes = new Vector<Cliente>();
        Random random = new Random();
        
        int numClientes = 30 + random.nextInt(30);
        for (int i = 0; i < numClientes; i++) {
            Cliente cliente = new Cliente("Cliente" + i, 18 + random.nextInt(50), 
                                        50.0 + random.nextDouble() * 150.0);
            clientes.add(cliente);
        }
        
        return clientes;
    }


    public Vector<Evento> quaisEventosTemMenorTaxaOcupacao() {
        Vector<Evento> eventos = negocio.obterEventos();
        Vector<Evento> eventosOrdenados = new Vector<>(eventos);
        
        // Ordenar por taxa de ocupação (crescente)
        eventosOrdenados.sort((a, b) -> Double.compare(
            (double) a.obterIngressosVendidos() / a.obterCapacidadeMaxima(),
            (double) b.obterIngressosVendidos() / b.obterCapacidadeMaxima()
        ));
        
        // Retornar os eventos com menor taxa de ocupação
        Vector<Evento> resultado = new Vector<>();
        for (int i = 0; i < Math.min(3, eventosOrdenados.size()); i++) {
            resultado.add(eventosOrdenados.get(i));
        }
        
        return resultado;
    }

    public double qualReceitaMediaPorIngressoVendido() {
        double receitaTotal = 0.0;
        int totalIngressosVendidos = 0;
        
        for (Evento evento : negocio.obterEventos()) {
            for (Ingresso ingresso : evento.obterIngressos()) {
                if (ingresso.foiVendido()) {
                    receitaTotal += ingresso.obterPreco();
                    totalIngressosVendidos++;
                }
            }
        }
        
        return totalIngressosVendidos > 0 ? receitaTotal / totalIngressosVendidos : 0.0;
    }

    public Vector<String> quaisSetoresEsgotamMaisRapidamente() {
        // Mapa para contar vendas por setor
        java.util.Map<String, Integer> vendasPorSetor = new java.util.HashMap<>();
        java.util.Map<String, Integer> capacidadePorSetor = new java.util.HashMap<>();
        
        for (Evento evento : negocio.obterEventos()) {
            for (Ingresso ingresso : evento.obterIngressos()) {
                String setor = ingresso.obterSetor();
                capacidadePorSetor.put(setor, capacidadePorSetor.getOrDefault(setor, 0) + 1);
                if (ingresso.foiVendido()) {
                    vendasPorSetor.put(setor, vendasPorSetor.getOrDefault(setor, 0) + 1);
                }
            }
        }
        
        // Calcular taxa de ocupação por setor
        java.util.Map<String, Double> ocupacaoPorSetor = new java.util.HashMap<>();
        for (String setor : capacidadePorSetor.keySet()) {
            int vendas = vendasPorSetor.getOrDefault(setor, 0);
            int capacidade = capacidadePorSetor.get(setor);
            ocupacaoPorSetor.put(setor, (double) vendas / capacidade);
        }
        
        // Ordenar setores por ocupação
        Vector<String> setoresOrdenados = new Vector<>(ocupacaoPorSetor.keySet());
        setoresOrdenados.sort((a, b) -> Double.compare(ocupacaoPorSetor.get(b), ocupacaoPorSetor.get(a)));
        
        // Retornar os setores mais populares
        Vector<String> resultado = new Vector<>();
        for (int i = 0; i < Math.min(3, setoresOrdenados.size()); i++) {
            resultado.add(setoresOrdenados.get(i));
        }
        
        return resultado;
    }

    @Override
    public Vector<Acao> obterAcoesRecomendadas() {
        Vector<Acao> acoes = new Vector<Acao>();
        
        // Usar os novos métodos para decisões estratégicas
        Vector<Evento> eventosOcupacaoBaixa = quaisEventosTemMenorTaxaOcupacao();
        double receitaMediaIngresso = qualReceitaMediaPorIngressoVendido();
        Vector<String> setoresPopulares = quaisSetoresEsgotamMaisRapidamente();
        
        // Alerta de emergência financeira
        if (this.negocio.getCaixa() < 200.0) {
            acoes.add(new EmergenciaFinanceira("Eventos", this.negocio.getCaixa()));
        }
        
        // Sugerir marketing para eventos com baixa ocupação
        for (Evento evento : eventosOcupacaoBaixa) {
            double taxaOcupacao = evento.obterCapacidadeMaxima() > 0 ? 
                (double)evento.obterIngressosVendidos() / evento.obterCapacidadeMaxima() : 0;
            
            if (taxaOcupacao < 0.3) {
                acoes.add(new InvestirEmMarketing(this.negocio, evento, 200.0));
            }
            
            // Ocupação muito baixa - considerar cancelamento
            if (taxaOcupacao < 0.1 && evento.obterIngressos().size() > 10) {
                acoes.add(new CancelarEvento(this.negocio, evento));
            }
        }
        
        // Ajustar preços de ingressos baseado na receita média
        if (!setoresPopulares.isEmpty() && receitaMediaIngresso > 0) {
            String setorPopular = setoresPopulares.get(0);
            for (Evento evento : this.negocio.obterEventos()) {
                acoes.add(new AjustarPrecoIngresso(evento, setorPopular, 15.0));
            }
        }
        
        // Criar novo evento se tiver dinheiro e poucos eventos
        if (this.negocio.getCaixa() > 2000.0 && this.negocio.obterEventos().size() < 3) {
            acoes.add(new CriarNovoEvento(this.negocio, "Festival de Verão", 
                                        TipoEvento.FESTA, 200));
        }
        
        // Realizar parcerias para reduzir custos
        if (this.negocio.getCaixa() > 1500.0 && !eventosOcupacaoBaixa.isEmpty()) {
            acoes.add(new RealizarParcerias(this.negocio, "Rádio Local", 500.0));
        }
        
        return acoes;
    }
}