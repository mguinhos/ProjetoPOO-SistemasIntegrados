package app.sistema;

import app.restaurante.Restaurante;
import app.restaurante.ItemCardapio;
import app.restaurante.Ingrediente;
import app.Cliente;
import app.acao.Acao;
import app.acao.geral.EmergenciaFinanceira;
import app.acao.restaurante.*;
import java.util.Vector;
import java.util.Random;

public class SistemaRestaurante extends Sistema<Restaurante> {
    private int clientesAtendidos = 0;
    private int clientesNaoAtendidos = 0;
    private double receitaDia = 0.0;

    public SistemaRestaurante(Restaurante restaurante) {
        super(restaurante);
    }

    @Override
    protected void simularOperacoesDoDia() {
        System.out.println("\n=== OPERAÇÕES DO DIA ===");
        clientesAtendidos = 0;
        clientesNaoAtendidos = 0;
        receitaDia = 0.0;

        Vector<Cliente> clientes = gerarClientesRandomicos();
        System.out.println("Clientes chegaram: " + clientes.size());

        for (Cliente cliente : clientes) {
            ItemCardapio itemEscolhido = escolherItemAleatorio();
            
            if (itemEscolhido == null) {
                clientesNaoAtendidos++;
                continue;
            }

            double preco = itemEscolhido.obterPreco();
            
            if (!cliente.possuiDinheiro(preco)) {
                clientesNaoAtendidos++;
                continue;
            }

            if (!negocio.obterEstoque().possuiItem(
                    itemEscolhido.obterConsumivel().obterIngredientes().get(0), 1)) {
                clientesNaoAtendidos++;
                continue;
            }

            if (cliente.pagar(preco)) {
                negocio.adicionarDinheiroNoCaixa(preco);
                receitaDia += preco;
                clientesAtendidos++;
                
                // Remover ingredientes do estoque
                for (Ingrediente ing : itemEscolhido.obterConsumivel().obterIngredientes()) {
                    negocio.obterEstoque().removerItem(ing, 1);
                }
            }
        }
    }

    @Override
    protected void exibirResumo() {
        System.out.println("\n=== RESUMO DO DIA ===");
        System.out.println("Clientes atendidos: " + clientesAtendidos);
        System.out.println("Clientes não atendidos: " + clientesNaoAtendidos);
        System.out.println("Receita do dia: R$ " + String.format("%.2f", receitaDia));
        System.out.println("Dinheiro em caixa: R$ " + String.format("%.2f", negocio.obterCaixa()));
    }

    private Vector<Cliente> gerarClientesRandomicos() {
        Vector<Cliente> clientes = new Vector<Cliente>();
        Random random = new Random();
        
        int numClientes = 20 + random.nextInt(20);
        for (int i = 0; i < numClientes; i++) {
            Cliente cliente = new Cliente("Cliente" + i, 25 + random.nextInt(40), 
                                        50.0 + random.nextDouble() * 100.0);
            clientes.add(cliente);
        }
        
        return clientes;
    }

    private ItemCardapio escolherItemAleatorio() {
        Vector<ItemCardapio> items = negocio.obterCardapio().obterItems();
        if (items.isEmpty()) return null;
        
        Random random = new Random();
        return items.get(random.nextInt(items.size()));
    }

    public Vector<ItemCardapio> quaisItemsDoCardapioTemMaiorMargemDeLucro() {
        Vector<ItemCardapio> items = negocio.obterCardapio().obterItems();
        Vector<ItemCardapio> itemsOrdenados = new Vector<>(items);
        
        // Ordenar items por margem de lucro (decrescente)
        itemsOrdenados.sort((a, b) -> Double.compare(b.obterLucro(), a.obterLucro()));
        
        // Retornar os 3 items com maior margem ou todos se forem menos de 3
        Vector<ItemCardapio> resultado = new Vector<>();
        for (int i = 0; i < Math.min(3, itemsOrdenados.size()); i++) {
            resultado.add(itemsOrdenados.get(i));
        }
        
        return resultado;
    }

    public double qualPercentualEstoqueIngredientesPrincipais() {
        int totalIngredientes = 0;
        int ingredientesBaixoEstoque = 0;
        
        // Considerar ingredientes principais aqueles usados em mais de um item
        Vector<Ingrediente> ingredientesPrincipais = new Vector<>();
        for (ItemCardapio item : negocio.obterCardapio().obterItems()) {
            for (Ingrediente ing : item.obterConsumivel().obterIngredientes()) {
                if (!ingredientesPrincipais.contains(ing)) {
                    ingredientesPrincipais.add(ing);
                }
            }
        }
        
        for (Ingrediente ing : ingredientesPrincipais) {
            totalIngredientes++;
            if (negocio.obterEstoque().obterQuantidade(ing) < 10) {
                ingredientesBaixoEstoque++;
            }
        }
        
        return totalIngredientes > 0 ? 
            100.0 * (totalIngredientes - ingredientesBaixoEstoque) / totalIngredientes : 0.0;
    }

    public double qualAvaliacaoMediaClientesPorPrato() {
        double somaAvaliacoes = 0.0;
        int totalItems = 0;
        
        for (ItemCardapio item : negocio.obterCardapio().obterItems()) {
            if (item.obterNumeroAvaliacoes() > 0) {
                somaAvaliacoes += item.obterAvaliacao();
                totalItems++;
            }
        }
        
        return totalItems > 0 ? somaAvaliacoes / totalItems : 0.0;
    }

    @Override
    public Vector<Acao> obterAcoesRecomendadas() {
        Vector<Acao> acoes = new Vector<Acao>();
        
        // Usar os novos métodos para decisões estratégicas
        double percentualEstoque = qualPercentualEstoqueIngredientesPrincipais();
        double avaliacaoMedia = qualAvaliacaoMediaClientesPorPrato();
        Vector<ItemCardapio> itemsMaiorLucro = quaisItemsDoCardapioTemMaiorMargemDeLucro();
        
        // Alerta de emergência financeira
        if (this.negocio.getCaixa() < 100.0) {
            acoes.add(new EmergenciaFinanceira("Restaurante", this.negocio.getCaixa()));
        }
        
        // Reposição de estoque baseada em análise de estoque
        if (percentualEstoque < 50.0) {
            for (ItemCardapio item : this.negocio.getCardapio().obterItems()) {
                Vector<Ingrediente> ingredientes = item.obterConsumivel().obterIngredientes();
                for (Ingrediente ing : ingredientes) {
                    if (!this.negocio.getEstoque().possuiItem(ing, 5)) {
                        acoes.add(new ReporIngrediente(this.negocio, ing, 20));
                        break;
                    }
                }
            }
        }
        
        // Aumentar preços de itens com maior margem de lucro se avaliação for boa
        if (avaliacaoMedia > 0.6 && !itemsMaiorLucro.isEmpty()) {
            for (ItemCardapio item : itemsMaiorLucro) {
                if (item.obterAvaliacao() > 0.7) {
                    acoes.add(new AumentarPreco(this.negocio.getCardapio(), item, 10.0));
                }
            }
        }
        
        // Analisar desempenho dos itens do cardápio
        for (ItemCardapio item : this.negocio.getCardapio().obterItems()) {
            double avaliacao = item.obterAvaliacao();
            int numAvaliacoes = item.obterNumeroAvaliacoes();
            
            if (avaliacao < 0.3 && numAvaliacoes > 5) {
                acoes.add(new BaixarPreco(this.negocio.getCardapio(), item, 20.0));
            }
            else if (avaliacao < 0.2 && numAvaliacoes > 10) {
                acoes.add(new RemoverItemCardapio(this.negocio.getCardapio(), item));
            }
        }
        
        // Realizar inventário se tiver dinheiro
        if (this.negocio.getCaixa() > 500.0 && percentualEstoque < 70.0) {
            acoes.add(new RealizarInventario(this.negocio));
        }
        
        return acoes;
    }
}