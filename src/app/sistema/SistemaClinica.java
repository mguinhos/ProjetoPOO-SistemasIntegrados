package app.sistema;

import app.clinica.Clinica;
import app.clinica.Consulta;
import app.clinica.Exame;
import app.clinica.Remedio;
import app.clinica.TipoExame;
import app.Cliente;
import app.ItemEstoque;
import app.acao.Acao;
import app.acao.clinica.*;
import app.acao.geral.EmergenciaFinanceira;

import java.util.Vector;
import java.util.Random;

public class SistemaClinica extends Sistema<Clinica> {
    private int consultasRealizadas = 0;
    private int examesRealizados = 0;
    private double receitaDia = 0.0;
    private double precoConsulta = 80.0;

    public SistemaClinica(Clinica clinica) {
        super(clinica);
    }

    @Override
    protected void simularOperacoesDoDia() {
        System.out.println("\n=== OPERAÇÕES DO DIA ===");
        consultasRealizadas = 0;
        examesRealizados = 0;
        receitaDia = 0.0;

        Vector<Cliente> pacientes = gerarPacientesRandomicos();
        System.out.println("Pacientes chegaram: " + pacientes.size());

        for (Cliente paciente : pacientes) {
            // Realizar consulta
            if (paciente.possuiDinheiro(precoConsulta)) {
                Consulta consulta = negocio.agendarConsulta(paciente, precoConsulta);
                
                if (negocio.processarPagamentoConsulta(consulta)) {
                    consultasRealizadas++;
                    receitaDia += precoConsulta;
                    consulta.finalizar();
                    
                    // 30% de chance de solicitar exame
                    Random random = new Random();
                    if (random.nextFloat() < 0.3) {
                        TipoExame tipo = TipoExame.values()[random.nextInt(TipoExame.values().length)];
                        double custoExame = 40.0 + random.nextDouble() * 60.0;
                        
                        if (paciente.possuiDinheiro(custoExame)) {
                            Exame exame = negocio.solicitarExame(paciente, tipo, custoExame);
                            if (negocio.processarPagamentoExame(exame)) {
                                examesRealizados++;
                                receitaDia += custoExame;
                                exame.definirResultado("Resultado do exame " + tipo);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void exibirResumo() {
        System.out.println("\n=== RESUMO DO DIA ===");
        System.out.println("Consultas realizadas: " + consultasRealizadas);
        System.out.println("Exames realizados: " + examesRealizados);
        System.out.println("Receita do dia: R$ " + String.format("%.2f", receitaDia));
        System.out.println("Dinheiro em caixa: R$ " + String.format("%.2f", negocio.obterCaixa()));
    }

    private Vector<Cliente> gerarPacientesRandomicos() {
        Vector<Cliente> pacientes = new Vector<Cliente>();
        Random random = new Random();
        
        int numPacientes = 10 + random.nextInt(15);
        for (int i = 0; i < numPacientes; i++) {
            Cliente paciente = new Cliente("Paciente" + i, 25 + random.nextInt(50), 
                                         100.0 + random.nextDouble() * 200.0);
            pacientes.add(paciente);
        }
        
        return pacientes;
    }

    public double qualTaxaOcupacaoMedicosUltimos30Dias() {
        // Simular a taxa de ocupação com base no número de consultas
        int consultas = negocio.obterConsultas().size();
        // Considerando capacidade de 60 consultas por mês por médico, com 2 médicos
        int capacidadeTotal = 60 * 2; 
        
        return Math.min(100.0, (double) consultas / capacidadeTotal * 100.0);
    }

    public Vector<TipoExame> quaisExamesGeramMaiorReceitaPorPaciente() {
        // Mapear receita por tipo de exame
        java.util.Map<TipoExame, Double> receitaPorExame = new java.util.HashMap<>();
        java.util.Map<TipoExame, Integer> contagemPorExame = new java.util.HashMap<>();
        
        for (Exame exame : negocio.obterExames()) {
            TipoExame tipo = exame.obterTipo();
            receitaPorExame.put(tipo, receitaPorExame.getOrDefault(tipo, 0.0) + exame.obterCusto());
            contagemPorExame.put(tipo, contagemPorExame.getOrDefault(tipo, 0) + 1);
        }
        
        // Calcular receita média por exame
        java.util.Map<TipoExame, Double> receitaMedia = new java.util.HashMap<>();
        for (TipoExame tipo : receitaPorExame.keySet()) {
            receitaMedia.put(tipo, receitaPorExame.get(tipo) / contagemPorExame.get(tipo));
        }
        
        // Ordenar exames por receita média
        Vector<TipoExame> examesOrdenados = new Vector<>(receitaMedia.keySet());
        examesOrdenados.sort((a, b) -> Double.compare(receitaMedia.get(b), receitaMedia.get(a)));
        
        // Retornar os exames com maior receita (até 3)
        Vector<TipoExame> resultado = new Vector<>();
        for (int i = 0; i < Math.min(3, examesOrdenados.size()); i++) {
            resultado.add(examesOrdenados.get(i));
        }
        
        return resultado;
    }

    public double qualEstoqueAtualMedicamentosEssenciais() {
        int medicamentosDisponiveis = 0;
        int totalMedicamentosEssenciais = 0;
        
        // Lista de medicamentos essenciais (exemplos)
        Vector<String> medicamentosEssenciais = new Vector<>();
        medicamentosEssenciais.add("Paracetamol");
        medicamentosEssenciais.add("Ibuprofeno");
        medicamentosEssenciais.add("Amoxicilina");
        
        for (String nomeMedicamento : medicamentosEssenciais) {
            totalMedicamentosEssenciais++;
            for (ItemEstoque item : negocio.obterEstoque().obterItems()) {
                if (item.obterRecurso().obterNome().equals(nomeMedicamento) && item.obterQuantidade() > 0) {
                    medicamentosDisponiveis++;
                    break;
                }
            }
        }
        
        return totalMedicamentosEssenciais > 0 ? 
            100.0 * medicamentosDisponiveis / totalMedicamentosEssenciais : 0.0;
    }

    @Override
    public Vector<Acao> obterAcoesRecomendadas() {
        Vector<Acao> acoes = new Vector<Acao>();
        
        double taxaOcupacao = qualTaxaOcupacaoMedicosUltimos30Dias();
        Vector<TipoExame> examesRentaveis = quaisExamesGeramMaiorReceitaPorPaciente();
        double percentualMedicamentos = qualEstoqueAtualMedicamentosEssenciais();
        
        // Alerta de emergência financeira
        if (this.negocio.getCaixa() < 500.0) {
            acoes.add(new EmergenciaFinanceira("Clínica", this.negocio.getCaixa()));
        }
        
        // Analisar demanda de consultas baseado na taxa de ocupação
        if (taxaOcupacao < 40.0) {
            acoes.add(new AjustarPrecoConsulta(80.0, 60.0, "Atrair mais pacientes"));
        } else if (taxaOcupacao > 85.0) {
            acoes.add(new AjustarPrecoConsulta(80.0, 100.0, "Alta demanda"));
            acoes.add(new ContratarMedico(this.negocio, "Ana Silva", "Cardiologia", 3000.0));
        }
        
        // Ajustar preços de exames rentáveis
        if (!examesRentaveis.isEmpty() && taxaOcupacao > 60.0) {
            TipoExame exame = examesRentaveis.get(0);
            acoes.add(new AjustarPrecoExame(exame, 50.0, 65.0));
        }
        
        // Verificar estoque de remédios
        if (percentualMedicamentos < 70.0) {
            Remedio paracetamol = new Remedio("Paracetamol", "Analgésico", 
                                            15.0, "Paracetamol 500mg", "FarmaLab");
            acoes.add(new ReporRemedio(this.negocio, paracetamol, 50));
        }
        
        // Sugerir compra de equipamento para exames mais rentáveis
        if (this.negocio.getCaixa() > 3000.0 && !examesRentaveis.isEmpty()) {
            String equipamento = "Equipamento para " + examesRentaveis.get(0);
            acoes.add(new ComprarEquipamento(this.negocio, equipamento, 2500.0));
        }
        
        return acoes;
    }
}