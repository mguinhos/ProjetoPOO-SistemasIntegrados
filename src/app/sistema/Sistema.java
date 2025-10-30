package app.sistema;

import java.util.Vector;
import java.util.Random;

import app.acao.Acao;

public abstract class Sistema<T extends Negocio> {
    protected T negocio;
    protected int diasDecorridos = 0;

    public Sistema(T negocio) {
        this.negocio = negocio;
    }

    public int obterDiasDecorridos() {
        return this.diasDecorridos;
    }

    public T obterNegocio() {
        return this.negocio;
    }

    public void simularDia() {
        diasDecorridos++;
        System.out.println("=== INICIANDO SIMULAÇÃO DO DIA " + diasDecorridos + " - " + 
                         negocio.obterNome() + " ===");
        
        System.out.println("\n=== SITUAÇÃO FINANCEIRA INICIAL ===");
        System.out.println("Dinheiro no caixa: R$ " + String.format("%.2f", negocio.obterCaixa()));
        
        realizarAcoesGerenciais();
        simularOperacoesDoDia();
        exibirResumo();
        
        System.out.println("\n=== FIM DA SIMULAÇÃO DO DIA " + diasDecorridos + " ===\n");
    }

    protected void realizarAcoesGerenciais() {
        System.out.println("\n=== AÇÕES GERENCIAIS ===");
        Vector<Acao> acoesRecomendadas = obterAcoesRecomendadas();
        Random random = new Random();
        
        if (acoesRecomendadas.isEmpty()) {
            System.out.println("Nenhuma ação recomendada para hoje.");
            return;
        }
        
        for (Acao acao : acoesRecomendadas) {
            if (acao.verificarSePodeExecutar()) {
                if (random.nextFloat() < 0.8) {
                    System.out.println("Executando: " + acao.obterNome());
                    System.out.println("  Descrição: " + acao.obterDescricao());
                    acao.executar();
                } else {
                    System.out.println("Ação adiada: " + acao.obterNome());
                }
            }
        }
    }

    protected abstract void simularOperacoesDoDia();
    protected abstract void exibirResumo();

    public abstract Vector<Acao> obterAcoesRecomendadas();
}