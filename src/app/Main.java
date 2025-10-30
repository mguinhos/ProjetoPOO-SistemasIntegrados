// file: /home/marcel/Documentos/Projects/SistemasIntegrados/src/app/Main.java
package app;

import app.clinica.Clinica;
import app.clinica.Consulta;
import app.clinica.Exame;
import app.clinica.Remedio;
import app.eventos.Evento;
import app.eventos.Eventos;
import app.eventos.Ingresso;
import app.eventos.TipoEvento;
import app.restaurante.*;
import app.sistema.SistemaClinica;
import app.sistema.SistemaEventos;
import app.sistema.SistemaRestaurante;
import app.acao.Acao;

import java.util.Vector;
import java.util.Date;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.ArrayList;

enum TipoEventoUI {
    KEY_UP, KEY_DOWN, ENTER, ESC
}

class EventoTeclado {
    TipoEventoUI tipo;
    EventoTeclado(TipoEventoUI tipo) {
        this.tipo = tipo;
    }
}

interface Focusable {
    void onFocus();
    void onBlur();
    boolean isFocused();
}

interface Drawable {
    int getWidth();
    int getHeight();
    String[] renderLines();
}

abstract class Widget implements Drawable {
    Vector<Widget> children;
    Widget parent;
    protected int width;
    protected int height;
    protected int paddingTop = 0;
    protected int paddingBottom = 0;
    protected int paddingLeft = 1;
    protected int paddingRight = 1;
    
    Widget() {
        this.children = new Vector<Widget>();
        this.parent = null;
        this.width = 0;
        this.height = 0;
    }
    
    Widget addWidget(Widget widget) {
        widget.parent = this;
        this.children.add(widget);
        return this;
    }
    
    abstract String exibir();
    
    Widget processarEvento(EventoTeclado evento) {
        return this;
    }
    
    Widget encontrarProximoFocavel() {
        if (parent != null) {
            return parent.encontrarProximoFocavel();
        }
        return null;
    }
    
    Widget encontrarAnteriorFocavel() {
        if (parent != null) {
            return parent.encontrarAnteriorFocavel();
        }
        return null;
    }
    
    protected void calculateDimensions() {
    }
    
    protected String[] drawBox(String[] content) {
        if (content == null || content.length == 0) {
            return new String[] { "┌┐", "└┘" };
        }
        int maxWidth = 0;
        for (String line : content) {
            maxWidth = Math.max(maxWidth, line.length());
        }
        String[] result = new String[content.length + 2];
        result[0] = "┌" + "─".repeat(maxWidth + paddingLeft + paddingRight) + "┐";
        for (int i = 0; i < content.length; i++) {
            String line = content[i];
            String padding = " ".repeat(Math.max(0, maxWidth - line.length()));
            result[i + 1] = "│" + " ".repeat(paddingLeft) + line + padding + " ".repeat(paddingRight) + "│";
        }
        result[result.length - 1] = "└" + "─".repeat(maxWidth + paddingLeft + paddingRight) + "┘";
        return result;
    }
    
    @Override
    public int getWidth() {
        return width;
    }
    
    @Override
    public int getHeight() {
        return height;
    }
    
    @Override
    public String[] renderLines() {
        String[] content = { exibir() };
        return drawBox(content);
    }
}

class Label extends Widget {
    private String texto;
    
    Label(String texto) {
        this.texto = texto;
        this.width = texto.length();
        this.height = 1;
    }
    
    @Override
    String exibir() {
        return this.texto;
    }
    
    @Override
    public String[] renderLines() {
        return new String[] { texto };
    }
    
    public void setText(String texto) {
        this.texto = texto;
        this.width = texto.length();
    }
}

class Opcao extends Widget implements Focusable {
    String texto;
    Consumer<Opcao> funcao;
    private boolean focused = false;
    private UI uiRef;
    
    Opcao(String texto, Consumer<Opcao> funcao) {
        this.texto = texto;
        this.funcao = funcao;
        this.width = texto.length() + 4;
        this.height = 1;
    }
    
    public void setUIReference(UI ui) {
        this.uiRef = ui;
    }
    
    @Override
    Widget processarEvento(EventoTeclado evento) {
        if (focused && evento.tipo == TipoEventoUI.ENTER) {
            this.funcao.accept(this);
            if (uiRef != null) {
                uiRef.forceRedraw();
            }
        }
        return this;
    }
    
    @Override
    String exibir() {
        if (focused) {
            return "> " + this.texto + " <";
        }
        return "  " + this.texto + "  ";
    }
    
    @Override
    public void onFocus() {
        this.focused = true;
    }
    
    @Override
    public void onBlur() {
        this.focused = false;
    }
    
    @Override
    public boolean isFocused() {
        return this.focused;
    }
}

class Menu extends Widget {
    private int selectedIndex = 0;
    private String titulo;
    
    Menu() {
        this("Menu");
    }
    
    Menu(String titulo) {
        this.titulo = titulo;
    }
    
    @Override
    Widget addWidget(Widget widget) {
        super.addWidget(widget);
        calculateDimensions();
        return this;
    }
    
    @Override
    protected void calculateDimensions() {
        int maxWidth = titulo.length();
        int totalHeight = 0;
        for (Widget child : children) {
            maxWidth = Math.max(maxWidth, child.getWidth());
            totalHeight += child.getHeight();
        }
        this.width = maxWidth + paddingLeft + paddingRight + 2;
        this.height = totalHeight + paddingTop + paddingBottom + 3;
    }
    
    @Override
    String exibir() {
        StringBuilder output = new StringBuilder();
        if (!titulo.isEmpty()) {
            output.append(titulo).append("\n");
            output.append("─".repeat(titulo.length())).append("\n");
        }
        for (int i = 0; i < this.children.size(); i++) {
            Widget child = this.children.get(i);
            if (i == selectedIndex && child instanceof Focusable) {
                ((Focusable) child).onFocus();
            } else if (child instanceof Focusable) {
                ((Focusable) child).onBlur();
            }
            output.append(child.exibir());
            if (i < children.size() - 1) {
                output.append("\n");
            }
        }
        return output.toString();
    }
    
    @Override
    public String[] renderLines() {
        Vector<String> lines = new Vector<>();
        if (!titulo.isEmpty()) {
            lines.add(titulo);
            lines.add("─".repeat(titulo.length()));
        }
        for (int i = 0; i < this.children.size(); i++) {
            Widget child = this.children.get(i);
            if (i == selectedIndex && child instanceof Focusable) {
                ((Focusable) child).onFocus();
            } else if (child instanceof Focusable) {
                ((Focusable) child).onBlur();
            }
            if (child instanceof Label) {
                String[] childLines = child.renderLines();
                for (String line : childLines) {
                    lines.add(line);
                }
            } else {
                lines.add(child.exibir());
            }
        }
        String[] content = lines.toArray(new String[0]);
        return drawBox(content);
    }
    
    @Override
    Widget processarEvento(EventoTeclado evento) {
        switch (evento.tipo) {
            case KEY_UP:
                selectedIndex = (selectedIndex - 1 + children.size()) % children.size();
                break;
            case KEY_DOWN:
                selectedIndex = (selectedIndex + 1) % children.size();
                break;
            case ENTER:
                if (selectedIndex < children.size()) {
                    children.get(selectedIndex).processarEvento(evento);
                }
                break;
            case ESC:
                break;
        }
        return this;
    }
    
    public int getSelectedIndex() {
        return selectedIndex;
    }
}

class InfoBox extends Widget {
    private String titulo;
    private Vector<String> linhas;
    
    InfoBox(String titulo) {
        this.titulo = titulo;
        this.linhas = new Vector<>();
    }
    
    public void adicionarLinha(String linha) {
        this.linhas.add(linha);
    }
    
    public void limpar() {
        this.linhas.clear();
    }
    
    @Override
    String exibir() {
        StringBuilder output = new StringBuilder();
        if (!titulo.isEmpty()) {
            output.append(titulo).append("\n");
            output.append("─".repeat(titulo.length())).append("\n");
        }
        for (String linha : linhas) {
            output.append(linha).append("\n");
        }
        return output.toString();
    }
    
    @Override
    public String[] renderLines() {
        Vector<String> allLines = new Vector<>();
        if (!titulo.isEmpty()) {
            allLines.add(titulo);
            allLines.add("─".repeat(titulo.length()));
        }
        for (String linha : linhas) {
            allLines.add(linha);
        }
        return drawBox(allLines.toArray(new String[0]));
    }
}

class UI extends Widget {
    private Widget widgetAtivo;
    private boolean needsRedraw = true;
    
    public UI() {
        super();
        this.widgetAtivo = null;
        this.paddingTop = 1;
        this.paddingBottom = 1;
    }
    
    @Override
    Widget addWidget(Widget widget) {
        super.addWidget(widget);
        // Se não houver widget ativo, define o que acabou de ser adicionado
        if (widgetAtivo == null) {
            widgetAtivo = widget;
        }
        // Se o widget adicionado for um Menu, garantir que ele receba foco (para navegação)
        if (widget instanceof Menu) {
            setWidgetAtivo(widget);
            setUIReferenceRecursive(widget);
        } else {
            // garantir referência para Opcao caso um Menu esteja dentro da hierarquia
            if (widget instanceof Opcao) {
                setUIReferenceRecursive(this); // assegura referências (fallback)
            }
        }
        return this;
    }
    
    private void setUIReferenceRecursive(Widget widget) {
        if (widget instanceof Opcao) {
            ((Opcao) widget).setUIReference(this);
        }
        for (Widget child : widget.children) {
            setUIReferenceRecursive(child);
        }
    }
    
    public void forceRedraw() {
        this.needsRedraw = true;
    }
    
    @Override
    String exibir() {
        StringBuilder output = new StringBuilder();
        output.append("=== SISTEMA INTEGRADO DE GESTÃO ===\n");
        output.append("Controles: W/S=navegar, ENTER=selecionar, Q=sair\n");
        output.append("==========================================\n\n");
        for (Widget child : children) {
            String[] lines = child.renderLines();
            for (String line : lines) {
                output.append(line).append("\n");
            }
            output.append("\n");
        }
        return output.toString();
    }
    
    @Override
    public String[] renderLines() {
        Vector<String> allLines = new Vector<>();
        allLines.add("════════════════════ SISTEMA INTEGRADO DE GESTÃO ═══════════════════════");
        allLines.add("Controles: ↑/↓ navegar, ENTER selecionar, 'q' sair");
        allLines.add("=================================================================");
        allLines.add("");
        for (Widget child : children) {
            String[] childLines = child.renderLines();
            for (String line : childLines) {
                allLines.add(line);
            }
            allLines.add("");
        }
        return allLines.toArray(new String[0]);
    }
    
    @Override
    Widget processarEvento(EventoTeclado evento) {
        if (widgetAtivo != null) {
            widgetAtivo.processarEvento(evento);
        }
        return this;
    }
    
    public void setWidgetAtivo(Widget widget) {
        this.widgetAtivo = widget;
    }
    
    public boolean needsRedraw() {
        return needsRedraw;
    }
    
    public void setRedrawComplete() {
        this.needsRedraw = false;
    }
    
    public void limparTudo() {
        this.children.clear();
    }
}

class TecladoHandler {
    private Scanner scanner;
    
    public TecladoHandler() {
        this.scanner = new Scanner(System.in);
    }
    
    public EventoTeclado lerProximoEvento() {
        System.out.print("Comando [W=↑ S=↓ ENTER=✓ Q=✕]: ");
        String input = scanner.nextLine().toLowerCase().trim();
        switch (input) {
            case "w":
                return new EventoTeclado(TipoEventoUI.KEY_UP);
            case "s":
                return new EventoTeclado(TipoEventoUI.KEY_DOWN);
            case "":
                return new EventoTeclado(TipoEventoUI.ENTER);
            case "q":
                return new EventoTeclado(TipoEventoUI.ESC);
            default:
                return lerProximoEvento(); // Tentar novamente silenciosamente
        }
    }
    
    public void fechar() {
        scanner.close();
    }
}

public class Main {
    static UI ui = new UI();
    static Restaurante restaurante;
    static Clinica clinica;
    static Eventos eventos;
    static SistemaRestaurante sistemaRestaurante;
    static SistemaClinica sistemaClinica;
    static SistemaEventos sistemaEventos;
    static Scanner scanner = new Scanner(System.in);
    
    // Histórico financeiro para calcular rentabilidade
    static ArrayList<Double> historicoReceitas = new ArrayList<>();
    static ArrayList<Double> historicoDespesas = new ArrayList<>();
    
    public static void limparTela() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else if (System.getProperty("os.name").contains("Linux")) {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[2J\033[H");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    public static double calcularRentabilidadeMedia() {
        if (historicoReceitas.size() == 0)
            return 0.0;
        double totalReceitas = 0.0;
        double totalDespesas = 0.0;
        for (Double receita : historicoReceitas) {
            totalReceitas += receita;
        }
        for (Double despesa : historicoDespesas) {
            totalDespesas += despesa;
        }
        if (historicoReceitas.size() == 0)
            return 0.0;
        double lucroMedio = (totalReceitas - totalDespesas) / historicoReceitas.size();
        return lucroMedio;
    }
    
    public static void mostrarDashboard() {
        InfoBox dashboard = new InfoBox("💼 Dashboard do Sistema Integrado");
        dashboard.adicionarLinha("💰 Cofre Restaurante: R$ " + String.format("%.2f", restaurante.obterCaixa()));
        dashboard.adicionarLinha("💰 Cofre Clínica: R$ " + String.format("%.2f", clinica.obterCaixa()));
        dashboard.adicionarLinha("💰 Cofre Eventos: R$ " + String.format("%.2f", eventos.obterCaixa()));
        dashboard.adicionarLinha("💰 Total: R$ " + String.format("%.2f", 
            restaurante.obterCaixa() + clinica.obterCaixa() + eventos.obterCaixa()));
        dashboard.adicionarLinha(
                "📈 Rentabilidade média (30 dias): R$ " + String.format("%.2f", calcularRentabilidadeMedia()) + "/dia");
        ui.limparTudo();
        ui.addWidget(dashboard);
        Menu menu = criarMenuPrincipal();
        ui.addWidget(menu);
        // garante que o menu receba foco para navegação
        ui.setWidgetAtivo(menu);
    }
    
    public static Menu criarMenuPrincipal() {
        Menu menuPrincipal = new Menu("🏪 Menu Principal");
        menuPrincipal
                .addWidget(new Opcao("🍔 Gerenciar Restaurante", (opcao) -> {
                    mostrarMenuRestaurante();
                }))
                .addWidget(new Opcao("🏥 Gerenciar Clínica", (opcao) -> {
                    mostrarMenuClinica();
                }))
                .addWidget(new Opcao("🎭 Gerenciar Eventos", (opcao) -> {
                    mostrarMenuEventos();
                }))
                .addWidget(new Opcao("💡 Ver Ações Recomendadas", (opcao) -> {
                    mostrarAcoesRecomendadas();
                }))
                .addWidget(new Opcao("📊 Resumo da Simulação", (opcao) -> {
                    mostrarResumoSimulacao();
                }))
                .addWidget(new Opcao("🚪 Sair", (opcao) -> {
                    System.exit(0);
                }));
        return menuPrincipal;
    }
    
    public static void mostrarMenuRestaurante() {
        ui.limparTudo();
        InfoBox infoRestaurante = new InfoBox("🍔 Restaurante");
        infoRestaurante.adicionarLinha("💰 Caixa: R$ " + String.format("%.2f", restaurante.obterCaixa()));
        infoRestaurante.adicionarLinha("🍽️ Itens no cardápio: " + restaurante.obterCardapio().obterItems().size());
        infoRestaurante.adicionarLinha("📦 Itens no estoque: " + restaurante.obterEstoque().obterItems().size());
        
        Menu menuRestaurante = new Menu("Ações");
        menuRestaurante
                .addWidget(new Opcao("🍽️ Ver Cardápio", (opcao) -> {
                    mostrarCardapio();
                }))
                .addWidget(new Opcao("📦 Gerenciar Estoque", (opcao) -> {
                    mostrarGerenciarEstoque();
                }))
                .addWidget(new Opcao("💵 Simular Vendas", (opcao) -> {
                    simularVendasRestaurante();
                }))
                .addWidget(new Opcao("🔙 Voltar", (opcao) -> {
                    mostrarDashboard();
                }));
        ui.addWidget(infoRestaurante);
        ui.addWidget(menuRestaurante);
        ui.setWidgetAtivo(menuRestaurante);
    }
    
    public static void mostrarCardapio() {
        ui.limparTudo();
        InfoBox infoCardapio = new InfoBox("🍽️ Cardápio");
        
        for (ItemCardapio item : restaurante.obterCardapio().obterItems()) {
            infoCardapio.adicionarLinha(item.obterNome() + 
                                       " - R$ " + String.format("%.2f", item.obterPreco()) +
                                       " (Aval: " + String.format("%.1f", item.obterAvaliacao()) + ")");
        }
        
        Menu menuCardapio = new Menu("Ações");
        menuCardapio
                .addWidget(new Opcao("💰 Ajustar Preços", (opcao) -> {
                    mostrarAjustarPrecos();
                }))
                .addWidget(new Opcao("🔙 Voltar", (opcao) -> {
                    mostrarMenuRestaurante();
                }));
                
        ui.addWidget(infoCardapio);
        ui.addWidget(menuCardapio);
        ui.setWidgetAtivo(menuCardapio);
    }
    
    public static void mostrarAjustarPrecos() {
        ui.limparTudo();
        InfoBox infoPrecos = new InfoBox("💰 Ajustar Preços");
        infoPrecos.adicionarLinha("Selecione um item para ajustar o preço:");
        
        Menu menuPrecos = new Menu("Itens");
        for (ItemCardapio item : restaurante.obterCardapio().obterItems()) {
            menuPrecos.addWidget(new Opcao(item.obterNome() + " - R$ " + 
                                          String.format("%.2f", item.obterPreco()), (opcao) -> {
                mostrarAjustarPrecoItem(item);
            }));
        }
        
        menuPrecos.addWidget(new Opcao("🔙 Voltar", (opcao) -> {
            mostrarCardapio();
        }));
        
        ui.addWidget(infoPrecos);
        ui.addWidget(menuPrecos);
        ui.setWidgetAtivo(menuPrecos);
    }
    
    public static void mostrarAjustarPrecoItem(ItemCardapio item) {
        ui.limparTudo();
        InfoBox infoItem = new InfoBox("💰 Ajustar: " + item.obterNome());
        infoItem.adicionarLinha("Preço atual: R$ " + String.format("%.2f", item.obterPreco()));
        infoItem.adicionarLinha("Avaliação: " + String.format("%.1f", item.obterAvaliacao()));
        infoItem.adicionarLinha("Custo: R$ " + String.format("%.2f", item.obterCustoDeProducao()));
        infoItem.adicionarLinha("Lucro: R$ " + String.format("%.2f", item.obterLucro()));
        
        Menu menuAjuste = new Menu("Opções");
        double precoAtual = item.obterPreco();
        
        menuAjuste
                .addWidget(new Opcao("📈 Aumentar 10%", (opcao) -> {
                    item.definirPreco(precoAtual * 1.1);
                    mostrarResultadoAcao("✅ Preço aumentado para R$ " + String.format("%.2f", item.obterPreco()));
                }))
                .addWidget(new Opcao("📉 Diminuir 10%", (opcao) -> {
                    item.definirPreco(precoAtual * 0.9);
                    mostrarResultadoAcao("✅ Preço diminuído para R$ " + String.format("%.2f", item.obterPreco()));
                }))
                .addWidget(new Opcao("🔙 Voltar", (opcao) -> {
                    mostrarAjustarPrecos();
                }));
                
        ui.addWidget(infoItem);
        ui.addWidget(menuAjuste);
        ui.setWidgetAtivo(menuAjuste);
    }
    
    public static void mostrarGerenciarEstoque() {
        ui.limparTudo();
        InfoBox infoEstoque = new InfoBox("📦 Gerenciar Estoque");
        
        if (restaurante.obterEstoque().obterItems().isEmpty()) {
            infoEstoque.adicionarLinha("Estoque vazio!");
        } else {
            for (ItemEstoque item : restaurante.obterEstoque().obterItems()) {
                infoEstoque.adicionarLinha(item.obterRecurso().obterNome() + 
                                         " - Quantidade: " + item.obterQuantidade());
            }
        }
        
        Menu menuEstoque = new Menu("Ações");
        menuEstoque
                .addWidget(new Opcao("➕ Adicionar Ingrediente", (opcao) -> {
                    mostrarAdicionarIngrediente();
                }))
                .addWidget(new Opcao("🔙 Voltar", (opcao) -> {
                    mostrarMenuRestaurante();
                }));
                
        ui.addWidget(infoEstoque);
        ui.addWidget(menuEstoque);
        ui.setWidgetAtivo(menuEstoque);
    }
    
    public static void mostrarAdicionarIngrediente() {
        ui.limparTudo();
        InfoBox info = new InfoBox("➕ Adicionar Ingrediente");
        info.adicionarLinha("Selecione um ingrediente para adicionar:");
        
        // Criar alguns ingredientes para exemplo
        Menu menuIngredientes = new Menu("Ingredientes");
        
        Ingrediente pao = new Ingrediente("Pão", 0.80);
        Ingrediente carne = new Ingrediente("Carne", 1.50);
        Ingrediente queijo = new Ingrediente("Queijo", 0.75);
        Ingrediente alface = new Ingrediente("Alface", 0.30);
        Ingrediente tomate = new Ingrediente("Tomate", 0.45);
        
        menuIngredientes
                .addWidget(new Opcao("Pão - R$ 0.80", (opcao) -> {
                    restaurante.obterEstoque().adicionarItem(pao, 10);
                    mostrarResultadoAcao("✅ 10x Pão adicionado ao estoque");
                }))
                .addWidget(new Opcao("Carne - R$ 1.50", (opcao) -> {
                    restaurante.obterEstoque().adicionarItem(carne, 10);
                    mostrarResultadoAcao("✅ 10x Carne adicionada ao estoque");
                }))
                .addWidget(new Opcao("Queijo - R$ 0.75", (opcao) -> {
                    restaurante.obterEstoque().adicionarItem(queijo, 10);
                    mostrarResultadoAcao("✅ 10x Queijo adicionado ao estoque");
                }))
                .addWidget(new Opcao("Alface - R$ 0.30", (opcao) -> {
                    restaurante.obterEstoque().adicionarItem(alface, 10);
                    mostrarResultadoAcao("✅ 10x Alface adicionada ao estoque");
                }))
                .addWidget(new Opcao("Tomate - R$ 0.45", (opcao) -> {
                    restaurante.obterEstoque().adicionarItem(tomate, 10);
                    mostrarResultadoAcao("✅ 10x Tomate adicionado ao estoque");
                }))
                .addWidget(new Opcao("🔙 Voltar", (opcao) -> {
                    mostrarGerenciarEstoque();
                }));
                
        ui.addWidget(info);
        ui.addWidget(menuIngredientes);
        ui.setWidgetAtivo(menuIngredientes);
    }
    
    public static void simularVendasRestaurante() {
        ui.limparTudo();
        InfoBox info = new InfoBox("💵 Simular Vendas");
        info.adicionarLinha("Escolha o número de dias para simular vendas:");
        
        Menu menuSimular = new Menu("Opções");
        menuSimular
                .addWidget(new Opcao("1 Dia", (opcao) -> {
                    sistemaRestaurante.simularDia();
                    mostrarResultadoAcao("✅ Simulação de 1 dia concluída!\nNovo saldo: R$ " + 
                                       String.format("%.2f", restaurante.obterCaixa()));
                }))
                .addWidget(new Opcao("7 Dias", (opcao) -> {
                    double saldoAnterior = restaurante.obterCaixa();
                    for (int i = 0; i < 7; i++) {
                        sistemaRestaurante.simularDia();
                    }
                    double lucro = restaurante.obterCaixa() - saldoAnterior;
                    mostrarResultadoAcao("✅ Simulação de 7 dias concluída!\n" +
                                       "Lucro: R$ " + String.format("%.2f", lucro) + "\n" +
                                       "Novo saldo: R$ " + String.format("%.2f", restaurante.obterCaixa()));
                }))
                .addWidget(new Opcao("30 Dias", (opcao) -> {
                    double saldoAnterior = restaurante.obterCaixa();
                    for (int i = 0; i < 30; i++) {
                        sistemaRestaurante.simularDia();
                    }
                    double lucro = restaurante.obterCaixa() - saldoAnterior;
                    mostrarResultadoAcao("✅ Simulação de 30 dias concluída!\n" +
                                       "Lucro: R$ " + String.format("%.2f", lucro) + "\n" +
                                       "Novo saldo: R$ " + String.format("%.2f", restaurante.obterCaixa()));
                }))
                .addWidget(new Opcao("🔙 Voltar", (opcao) -> {
                    mostrarMenuRestaurante();
                }));
                
        ui.addWidget(info);
        ui.addWidget(menuSimular);
        ui.setWidgetAtivo(menuSimular);
    }
    
    public static void mostrarMenuClinica() {
        ui.limparTudo();
        InfoBox infoClinica = new InfoBox("🏥 Clínica");
        infoClinica.adicionarLinha("💰 Caixa: R$ " + String.format("%.2f", clinica.obterCaixa()));
        infoClinica.adicionarLinha("👨‍⚕️ Consultas realizadas: " + clinica.obterConsultas().size());
        infoClinica.adicionarLinha("🔬 Exames realizados: " + clinica.obterExames().size());
        
        Menu menuClinica = new Menu("Ações");
        menuClinica
                .addWidget(new Opcao("👨‍⚕️ Ver Consultas", (opcao) -> {
                    mostrarConsultas();
                }))
                .addWidget(new Opcao("🔬 Ver Exames", (opcao) -> {
                    mostrarExames();
                }))
                .addWidget(new Opcao("💉 Gerenciar Remédios", (opcao) -> {
                    mostrarGerenciarRemedios();
                }))
                .addWidget(new Opcao("💵 Simular Atendimentos", (opcao) -> {
                    simularAtendimentosClinica();
                }))
                .addWidget(new Opcao("🔙 Voltar", (opcao) -> {
                    mostrarDashboard();
                }));
                
        ui.addWidget(infoClinica);
        ui.addWidget(menuClinica);
        ui.setWidgetAtivo(menuClinica);
    }
    
    public static void mostrarConsultas() {
        ui.limparTudo();
        InfoBox infoConsultas = new InfoBox("👨‍⚕️ Consultas");
        
        if (clinica.obterConsultas().isEmpty()) {
            infoConsultas.adicionarLinha("Nenhuma consulta realizada!");
        } else {
            for (Consulta consulta : clinica.obterConsultas()) {
                infoConsultas.adicionarLinha("Paciente: " + consulta.obterPaciente().obterNome() + 
                                          " - Data: " + consulta.obterDataConsulta() +
                                          " - Valor: R$ " + String.format("%.2f", consulta.obterValorConsulta()));
                if (!consulta.obterDiagnostico().isEmpty()) {
                    infoConsultas.adicionarLinha("   Diagnóstico: " + consulta.obterDiagnostico());
                }
                infoConsultas.adicionarLinha("");
            }
        }
        
        Menu menuConsultas = new Menu("Ações");
        menuConsultas.addWidget(new Opcao("🔙 Voltar", (opcao) -> {
            mostrarMenuClinica();
        }));
        
        ui.addWidget(infoConsultas);
        ui.addWidget(menuConsultas);
        ui.setWidgetAtivo(menuConsultas);
    }
    
    public static void mostrarExames() {
        ui.limparTudo();
        InfoBox infoExames = new InfoBox("🔬 Exames");
        
        if (clinica.obterExames().isEmpty()) {
            infoExames.adicionarLinha("Nenhum exame realizado!");
        } else {
            for (Exame exame : clinica.obterExames()) {
                infoExames.adicionarLinha("Paciente: " + exame.obterPaciente().obterNome() + 
                                       " - Tipo: " + exame.obterTipo() +
                                       " - Valor: R$ " + String.format("%.2f", exame.obterCusto()));
                if (exame.foiRealizado()) {
                    infoExames.adicionarLinha("   Resultado: " + exame.obterResultado());
                } else {
                    infoExames.adicionarLinha("   Status: Aguardando realização");
                }
                infoExames.adicionarLinha("");
            }
        }
        
        Menu menuExames = new Menu("Ações");
        menuExames.addWidget(new Opcao("🔙 Voltar", (opcao) -> {
            mostrarMenuClinica();
        }));
        
        ui.addWidget(infoExames);
        ui.addWidget(menuExames);
        ui.setWidgetAtivo(menuExames);
    }
    
    public static void mostrarGerenciarRemedios() {
        ui.limparTudo();
        InfoBox infoRemedios = new InfoBox("💉 Gerenciar Remédios");
        
        Vector<ItemEstoque> estoque = clinica.obterEstoque().obterItems();
        if (estoque.isEmpty()) {
            infoRemedios.adicionarLinha("Nenhum remédio no estoque!");
        } else {
            for (ItemEstoque item : estoque) {
                if (item.obterRecurso() instanceof Remedio) {
                    Remedio remedio = (Remedio) item.obterRecurso();
                    infoRemedios.adicionarLinha(remedio.obterNome() + " - Quantidade: " + item.obterQuantidade());
                    infoRemedios.adicionarLinha("   Princípio ativo: " + remedio.obterPrincipioAtivo());
                    infoRemedios.adicionarLinha("   Fabricante: " + remedio.obterFabricante());
                    infoRemedios.adicionarLinha("");
                }
            }
        }
        
        Menu menuRemedios = new Menu("Ações");
        menuRemedios
                .addWidget(new Opcao("➕ Adicionar Remédio", (opcao) -> {
                    mostrarAdicionarRemedio();
                }))
                .addWidget(new Opcao("🔙 Voltar", (opcao) -> {
                    mostrarMenuClinica();
                }));
                
        ui.addWidget(infoRemedios);
        ui.addWidget(menuRemedios);
        ui.setWidgetAtivo(menuRemedios);
    }
    
    public static void mostrarAdicionarRemedio() {
        ui.limparTudo();
        InfoBox info = new InfoBox("➕ Adicionar Remédio");
        info.adicionarLinha("Selecione um remédio para adicionar:");
        
        // Criar alguns remédios para exemplo
        Menu menuRemedios = new Menu("Remédios");
        
        Remedio paracetamol = new Remedio("Paracetamol", "Analgésico e antipirético", 15.0, "Paracetamol 500mg", "FarmaLab");
        Remedio ibuprofeno = new Remedio("Ibuprofeno", "Anti-inflamatório", 18.0, "Ibuprofeno 400mg", "MediPharma");
        Remedio amoxicilina = new Remedio("Amoxicilina", "Antibiótico", 25.0, "Amoxicilina 500mg", "BioPharm");
        
        menuRemedios
                .addWidget(new Opcao("Paracetamol - R$ 15.00", (opcao) -> {
                    clinica.obterEstoque().adicionarItem(paracetamol, 10);
                    mostrarResultadoAcao("✅ 10x Paracetamol adicionado ao estoque");
                }))
                .addWidget(new Opcao("Ibuprofeno - R$ 18.00", (opcao) -> {
                    clinica.obterEstoque().adicionarItem(ibuprofeno, 10);
                    mostrarResultadoAcao("✅ 10x Ibuprofeno adicionado ao estoque");
                }))
                .addWidget(new Opcao("Amoxicilina - R$ 25.00", (opcao) -> {
                    clinica.obterEstoque().adicionarItem(amoxicilina, 10);
                    mostrarResultadoAcao("✅ 10x Amoxicilina adicionada ao estoque");
                }))
                .addWidget(new Opcao("🔙 Voltar", (opcao) -> {
                    mostrarGerenciarRemedios();
                }));
                
        ui.addWidget(info);
        ui.addWidget(menuRemedios);
        ui.setWidgetAtivo(menuRemedios);
    }
    
    public static void simularAtendimentosClinica() {
        ui.limparTudo();
        InfoBox info = new InfoBox("💵 Simular Atendimentos");
        info.adicionarLinha("Escolha o número de dias para simular atendimentos:");
        
        Menu menuSimular = new Menu("Opções");
        menuSimular
                .addWidget(new Opcao("1 Dia", (opcao) -> {
                    sistemaClinica.simularDia();
                    mostrarResultadoAcao("✅ Simulação de 1 dia concluída!\nNovo saldo: R$ " + 
                                       String.format("%.2f", clinica.obterCaixa()));
                }))
                .addWidget(new Opcao("7 Dias", (opcao) -> {
                    double saldoAnterior = clinica.obterCaixa();
                    for (int i = 0; i < 7; i++) {
                        sistemaClinica.simularDia();
                    }
                    double lucro = clinica.obterCaixa() - saldoAnterior;
                    mostrarResultadoAcao("✅ Simulação de 7 dias concluída!\n" +
                                       "Lucro: R$ " + String.format("%.2f", lucro) + "\n" +
                                       "Novo saldo: R$ " + String.format("%.2f", clinica.obterCaixa()));
                }))
                .addWidget(new Opcao("30 Dias", (opcao) -> {
                    double saldoAnterior = clinica.obterCaixa();
                    for (int i = 0; i < 30; i++) {
                        sistemaClinica.simularDia();
                    }
                    double lucro = clinica.obterCaixa() - saldoAnterior;
                    mostrarResultadoAcao("✅ Simulação de 30 dias concluída!\n" +
                                       "Lucro: R$ " + String.format("%.2f", lucro) + "\n" +
                                       "Novo saldo: R$ " + String.format("%.2f", clinica.obterCaixa()));
                }))
                .addWidget(new Opcao("🔙 Voltar", (opcao) -> {
                    mostrarMenuClinica();
                }));
                
        ui.addWidget(info);
        ui.addWidget(menuSimular);
        ui.setWidgetAtivo(menuSimular);
    }
    
    public static void mostrarMenuEventos() {
        ui.limparTudo();
        InfoBox infoEventos = new InfoBox("🎭 Eventos");
        infoEventos.adicionarLinha("💰 Caixa: R$ " + String.format("%.2f", eventos.obterCaixa()));
        infoEventos.adicionarLinha("🎫 Eventos cadastrados: " + eventos.obterEventos().size());
        
        Menu menuEventos = new Menu("Ações");
        menuEventos
                .addWidget(new Opcao("📋 Ver Eventos", (opcao) -> {
                    mostrarEventos();
                }))
                .addWidget(new Opcao("➕ Criar Novo Evento", (opcao) -> {
                    mostrarCriarEvento();
                }))
                .addWidget(new Opcao("💵 Simular Vendas de Ingressos", (opcao) -> {
                    simularVendasEventos();
                }))
                .addWidget(new Opcao("🔙 Voltar", (opcao) -> {
                    mostrarDashboard();
                }));
                
        ui.addWidget(infoEventos);
        ui.addWidget(menuEventos);
        ui.setWidgetAtivo(menuEventos);
    }
    
    public static void mostrarEventos() {
        ui.limparTudo();
        InfoBox infoEventos = new InfoBox("📋 Eventos");
        
        if (eventos.obterEventos().isEmpty()) {
            infoEventos.adicionarLinha("Nenhum evento cadastrado!");
        } else {
            for (Evento evento : eventos.obterEventos()) {
                infoEventos.adicionarLinha("🎭 " + evento.obterNome() + " (" + evento.obterTipo() + ")");
                infoEventos.adicionarLinha("   Data: " + evento.obterData());
                infoEventos.adicionarLinha("   Capacidade: " + evento.obterIngressosVendidos() + 
                                        "/" + evento.obterCapacidadeMaxima() + " ingressos");
                infoEventos.adicionarLinha("   Custo: R$ " + String.format("%.2f", evento.obterCustoOrganizacao()));
                infoEventos.adicionarLinha("");
            }
        }
        
        Menu menuEventos = new Menu("Ações");
        if (!eventos.obterEventos().isEmpty()) {
            menuEventos.addWidget(new Opcao("🎫 Gerenciar Ingressos", (opcao) -> {
                mostrarGerenciarIngressos();
            }));
        }
        menuEventos.addWidget(new Opcao("🔙 Voltar", (opcao) -> {
            mostrarMenuEventos();
        }));
        
        ui.addWidget(infoEventos);
        ui.addWidget(menuEventos);
        ui.setWidgetAtivo(menuEventos);
    }
    
    public static void mostrarGerenciarIngressos() {
        ui.limparTudo();
        InfoBox infoIngressos = new InfoBox("🎫 Gerenciar Ingressos");
        infoIngressos.adicionarLinha("Selecione um evento para gerenciar ingressos:");
        
        Menu menuEventos = new Menu("Eventos");
        for (Evento evento : eventos.obterEventos()) {
            menuEventos.addWidget(new Opcao(evento.obterNome() + " (" + evento.obterIngressosVendidos() + 
                                         "/" + evento.obterCapacidadeMaxima() + ")", (opcao) -> {
                mostrarIngressosEvento(evento);
            }));
        }
        menuEventos.addWidget(new Opcao("🔙 Voltar", (opcao) -> {
            mostrarEventos();
        }));
        
        ui.addWidget(infoIngressos);
        ui.addWidget(menuEventos);
        ui.setWidgetAtivo(menuEventos);
    }
    
    public static void mostrarIngressosEvento(Evento evento) {
        ui.limparTudo();
        InfoBox infoEvento = new InfoBox("🎫 Ingressos: " + evento.obterNome());
        
        infoEvento.adicionarLinha("Ingressos vendidos: " + evento.obterIngressosVendidos() + 
                               "/" + evento.obterCapacidadeMaxima());
        
        if (evento.obterIngressos().isEmpty()) {
            infoEvento.adicionarLinha("Nenhum ingresso criado para este evento!");
        } else {
            infoEvento.adicionarLinha("\nDetalhes dos ingressos:");
            for (Ingresso ingresso : evento.obterIngressos()) {
                String status = ingresso.foiVendido() ? "Vendido" : "Disponível";
                infoEvento.adicionarLinha("Setor: " + ingresso.obterSetor() + 
                                       " - Preço: R$ " + String.format("%.2f", ingresso.obterPreco()) + 
                                       " - Status: " + status);
                if (ingresso.foiVendido()) {
                    infoEvento.adicionarLinha("   Cliente: " + ingresso.obterCliente().obterNome());
                }
            }
        }
        
        Menu menuIngressos = new Menu("Ações");
        menuIngressos
                .addWidget(new Opcao("➕ Criar Ingressos", (opcao) -> {
                    mostrarCriarIngressos(evento);
                }))
                .addWidget(new Opcao("🔙 Voltar", (opcao) -> {
                    mostrarGerenciarIngressos();
                }));
                
        ui.addWidget(infoEvento);
        ui.addWidget(menuIngressos);
        ui.setWidgetAtivo(menuIngressos);
    }
    
    public static void mostrarCriarIngressos(Evento evento) {
        ui.limparTudo();
        InfoBox info = new InfoBox("➕ Criar Ingressos");
        info.adicionarLinha("Evento: " + evento.obterNome());
        info.adicionarLinha("Capacidade: " + evento.obterIngressosVendidos() + "/" + evento.obterCapacidadeMaxima());
        info.adicionarLinha("Ingressos disponíveis: " + evento.obterIngressosDisponiveis());
        
        Menu menuIngressos = new Menu("Opções");
        
        if (evento.obterIngressosDisponiveis() <= 0) {
            info.adicionarLinha("\n❌ Capacidade máxima atingida! Não é possível criar mais ingressos.");
            menuIngressos.addWidget(new Opcao("🔙 Voltar", (opcao) -> {
                mostrarIngressosEvento(evento);
            }));
        } else {
            menuIngressos
                    .addWidget(new Opcao("🎭 Ingressos Padrão (R$ 50,00)", (opcao) -> {
                        int quantidade = Math.min(10, evento.obterIngressosDisponiveis());
                        for (int i = 0; i < quantidade; i++) {
                            eventos.criarIngresso(evento, 50.0, "Padrão");
                        }
                        mostrarResultadoAcao("✅ " + quantidade + " ingressos Padrão criados com sucesso!");
                    }))
                    .addWidget(new Opcao("💎 Ingressos VIP (R$ 100,00)", (opcao) -> {
                        int quantidade = Math.min(5, evento.obterIngressosDisponiveis());
                        for (int i = 0; i < quantidade; i++) {
                            eventos.criarIngresso(evento, 100.0, "VIP");
                        }
                        mostrarResultadoAcao("✅ " + quantidade + " ingressos VIP criados com sucesso!");
                    }))
                    .addWidget(new Opcao("🔙 Voltar", (opcao) -> {
                        mostrarIngressosEvento(evento);
                    }));
        }
        
        ui.addWidget(info);
        ui.addWidget(menuIngressos);
        ui.setWidgetAtivo(menuIngressos);
    }
    
    public static void mostrarCriarEvento() {
        ui.limparTudo();
        InfoBox info = new InfoBox("➕ Criar Novo Evento");
        info.adicionarLinha("Selecione um tipo de evento para criar:");
        
        Menu menuTipos = new Menu("Tipos de Evento");
        
        menuTipos
                .addWidget(new Opcao("🎵 Show Musical", (opcao) -> {
                    Date dataEvento = new Date(); // Data atual para simplificar
                    Evento _novoEvento = eventos.criarEvento(
                        "Show Musical", 
                        "Show com bandas locais", 
                        dataEvento, 
                        TipoEvento.SHOW, 
                        100, 
                        1500.0
                    );
                    mostrarResultadoAcao("✅ Evento 'Show Musical' criado com sucesso!");
                }))
                .addWidget(new Opcao("🎭 Peça de Teatro", (opcao) -> {
                    Date dataEvento = new Date(); // Data atual para simplificar
                    Evento _novoEvento = eventos.criarEvento(
                        "Peça de Teatro", 
                        "Apresentação teatral", 
                        dataEvento, 
                        TipoEvento.TEATRO, 
                        80, 
                        1200.0
                    );
                    mostrarResultadoAcao("✅ Evento 'Peça de Teatro' criado com sucesso!");
                }))
                .addWidget(new Opcao("🎬 Cinema", (opcao) -> {
                    Date dataEvento = new Date(); // Data atual para simplificar
                    Evento _novoEvento = eventos.criarEvento(
                        "Noite de Cinema", 
                        "Exibição de filme premiado", 
                        dataEvento, 
                        TipoEvento.CINEMA, 
                        120, 
                        800.0
                    );
                    mostrarResultadoAcao("✅ Evento 'Noite de Cinema' criado com sucesso!");
                }))
                .addWidget(new Opcao("🎓 Palestra", (opcao) -> {
                    Date dataEvento = new Date(); // Data atual para simplificar
                    Evento _novoEvento = eventos.criarEvento(
                        "Palestra Tecnológica", 
                        "Palestra sobre inovações tecnológicas", 
                        dataEvento, 
                        TipoEvento.PALESTRA, 
                        60, 
                        500.0
                    );
                    mostrarResultadoAcao("✅ Evento 'Palestra Tecnológica' criado com sucesso!");
                }))
                .addWidget(new Opcao("🔙 Voltar", (opcao) -> {
                    mostrarMenuEventos();
                }));
                
        ui.addWidget(info);
        ui.addWidget(menuTipos);
        ui.setWidgetAtivo(menuTipos);
    }
    
    public static void simularVendasEventos() {
        ui.limparTudo();
        InfoBox info = new InfoBox("💵 Simular Vendas de Ingressos");
        info.adicionarLinha("Escolha o número de dias para simular vendas:");
        
        Menu menuSimular = new Menu("Opções");
        menuSimular
                .addWidget(new Opcao("1 Dia", (opcao) -> {
                    sistemaEventos.simularDia();
                    mostrarResultadoAcao("✅ Simulação de 1 dia concluída!\nNovo saldo: R$ " + 
                                       String.format("%.2f", eventos.obterCaixa()));
                }))
                .addWidget(new Opcao("7 Dias", (opcao) -> {
                    double saldoAnterior = eventos.obterCaixa();
                    for (int i = 0; i < 7; i++) {
                        sistemaEventos.simularDia();
                    }
                    double lucro = eventos.obterCaixa() - saldoAnterior;
                    mostrarResultadoAcao("✅ Simulação de 7 dias concluída!\n" +
                                       "Lucro: R$ " + String.format("%.2f", lucro) + "\n" +
                                       "Novo saldo: R$ " + String.format("%.2f", eventos.obterCaixa()));
                }))
                .addWidget(new Opcao("30 Dias", (opcao) -> {
                    double saldoAnterior = eventos.obterCaixa();
                    for (int i = 0; i < 30; i++) {
                        sistemaEventos.simularDia();
                    }
                    double lucro = eventos.obterCaixa() - saldoAnterior;
                    mostrarResultadoAcao("✅ Simulação de 30 dias concluída!\n" +
                                       "Lucro: R$ " + String.format("%.2f", lucro) + "\n" +
                                       "Novo saldo: R$ " + String.format("%.2f", eventos.obterCaixa()));
                }))
                .addWidget(new Opcao("🔙 Voltar", (opcao) -> {
                    mostrarMenuEventos();
                }));
                
        ui.addWidget(info);
        ui.addWidget(menuSimular);
        ui.setWidgetAtivo(menuSimular);
    }
    
    public static void mostrarAcoesRecomendadas() {
        ui.limparTudo();
        InfoBox infoAcoes = new InfoBox("💡 Ações Recomendadas");
        
        Vector<Acao> acoesRestaurante = sistemaRestaurante.obterAcoesRecomendadas();
        Vector<Acao> acoesClinica = sistemaClinica.obterAcoesRecomendadas();
        Vector<Acao> acoesEventos = sistemaEventos.obterAcoesRecomendadas();
        
        if (acoesRestaurante.isEmpty() && acoesClinica.isEmpty() && acoesEventos.isEmpty()) {
            infoAcoes.adicionarLinha("✅ Nenhuma ação recomendada no momento!");
            infoAcoes.adicionarLinha("   Todos os negócios estão funcionando bem.");
        } else {
            if (!acoesRestaurante.isEmpty()) {
                infoAcoes.adicionarLinha("🍔 RESTAURANTE:");
                for (Acao acao : acoesRestaurante) {
                    infoAcoes.adicionarLinha("   - " + acao.obterNome() + ": " + acao.obterDescricao());
                }
                infoAcoes.adicionarLinha("");
            }
            
            if (!acoesClinica.isEmpty()) {
                infoAcoes.adicionarLinha("🏥 CLÍNICA:");
                for (Acao acao : acoesClinica) {
                    infoAcoes.adicionarLinha("   - " + acao.obterNome() + ": " + acao.obterDescricao());
                }
                infoAcoes.adicionarLinha("");
            }
            
            if (!acoesEventos.isEmpty()) {
                infoAcoes.adicionarLinha("🎭 EVENTOS:");
                for (Acao acao : acoesEventos) {
                    infoAcoes.adicionarLinha("   - " + acao.obterNome() + ": " + acao.obterDescricao());
                }
            }
        }
        
        Menu menuAcoes = new Menu("Opções");
        if (!acoesRestaurante.isEmpty()) {
            menuAcoes.addWidget(new Opcao("🍔 Executar Ações Restaurante", (opcao) -> {
                executarAcoes(acoesRestaurante, "Restaurante");
            }));
        }
        
        if (!acoesClinica.isEmpty()) {
            menuAcoes.addWidget(new Opcao("🏥 Executar Ações Clínica", (opcao) -> {
                executarAcoes(acoesClinica, "Clínica");
            }));
        }
        
        if (!acoesEventos.isEmpty()) {
            menuAcoes.addWidget(new Opcao("🎭 Executar Ações Eventos", (opcao) -> {
                executarAcoes(acoesEventos, "Eventos");
            }));
        }
        
        menuAcoes.addWidget(new Opcao("🔙 Voltar", (opcao) -> {
            mostrarDashboard();
        }));
        
        ui.addWidget(infoAcoes);
        ui.addWidget(menuAcoes);
        ui.setWidgetAtivo(menuAcoes);
    }
    
    public static void executarAcoes(Vector<Acao> acoes, String tipoNegocio) {
        int executadas = 0;
        for (Acao acao : acoes) {
            if (acao.verificarSePodeExecutar()) {
                acao.executar();
                executadas++;
            }
        }
        mostrarResultadoAcao("✅ " + executadas + " ações de " + tipoNegocio + " executadas com sucesso!");
    }
    
    public static void mostrarResumoSimulacao() {
        ui.limparTudo();
        InfoBox resumo = new InfoBox("📊 Resumo da Simulação");
        resumo.adicionarLinha("💰 SITUAÇÃO FINANCEIRA:");
        resumo.adicionarLinha("   Restaurante: R$ " + String.format("%.2f", restaurante.obterCaixa()));
        resumo.adicionarLinha("   Clínica: R$ " + String.format("%.2f", clinica.obterCaixa()));
        resumo.adicionarLinha("   Eventos: R$ " + String.format("%.2f", eventos.obterCaixa()));
        double total = restaurante.obterCaixa() + clinica.obterCaixa() + eventos.obterCaixa();
        resumo.adicionarLinha("   TOTAL: R$ " + String.format("%.2f", total));
        
        resumo.adicionarLinha("\n🍔 RESTAURANTE:");
        resumo.adicionarLinha("   Items no cardápio: " + restaurante.obterCardapio().obterItems().size());
        resumo.adicionarLinha("   Items no estoque: " + restaurante.obterEstoque().obterItems().size());
        
        resumo.adicionarLinha("\n🏥 CLÍNICA:");
        resumo.adicionarLinha("   Consultas realizadas: " + clinica.obterConsultas().size());
        resumo.adicionarLinha("   Exames realizados: " + clinica.obterExames().size());
        
        resumo.adicionarLinha("\n🎭 EVENTOS:");
        resumo.adicionarLinha("   Eventos cadastrados: " + eventos.obterEventos().size());
        int ingressosVendidos = 0;
        for (Evento evento : eventos.obterEventos()) {
            ingressosVendidos += evento.obterIngressosVendidos();
        }
        resumo.adicionarLinha("   Ingressos vendidos: " + ingressosVendidos);
        
        Menu menuResumo = new Menu("Opções");
        menuResumo.addWidget(new Opcao("🔙 Voltar", (opcao) -> {
            mostrarDashboard();
        }));
        
        ui.addWidget(resumo);
        ui.addWidget(menuResumo);
        ui.setWidgetAtivo(menuResumo);
    }
    
    public static void mostrarResultadoAcao(String mensagem) {
        ui.limparTudo();
        InfoBox resultado = new InfoBox("📝 Resultado");
        resultado.adicionarLinha(mensagem);
        Menu menuContinuar = new Menu("Opções");
        menuContinuar.addWidget(new Opcao("✅ Continuar", (opcao) -> {
            mostrarDashboard();
        }));
        ui.addWidget(resultado);
        ui.addWidget(menuContinuar);
        ui.setWidgetAtivo(menuContinuar);
    }
    
    public static void inicializarSistema() {
        // Inicializar módulos
        restaurante = new Restaurante();
        clinica = new Clinica();
        eventos = new Eventos();
        
        // Inicializar sistemas de simulação
        sistemaRestaurante = new SistemaRestaurante(restaurante);
        sistemaClinica = new SistemaClinica(clinica);
        sistemaEventos = new SistemaEventos(eventos);
        
        // Inicializar o restaurante
        // Criar ingredientes
        Ingrediente pao = new Ingrediente("Pão", 0.80);
        Ingrediente carne = new Ingrediente("Carne", 1.50);
        Ingrediente queijo = new Ingrediente("Queijo", 0.75);
        // Adicionar ao estoque
        restaurante.obterEstoque().adicionarItem(pao, 50);
        restaurante.obterEstoque().adicionarItem(carne, 30);
        restaurante.obterEstoque().adicionarItem(queijo, 40);
        // Criar alimentos e adicionar ao cardápio
        Vector<Ingrediente> ingredientesHamburguer = new Vector<>();
        ingredientesHamburguer.add(pao);
        ingredientesHamburguer.add(carne);
        ingredientesHamburguer.add(queijo);
        Alimento hamburguer = new Alimento("Hamburguer", ingredientesHamburguer);
        ItemCardapio itemHamburguer = new ItemCardapio(15.00, hamburguer);
        restaurante.obterCardapio().adicionarItem(itemHamburguer);
        
        // Inicializar a clínica
        Remedio paracetamol = new Remedio("Paracetamol", "Analgésico e antipirético",
                                        15.0, "Paracetamol 500mg", "FarmaLab");
        clinica.obterEstoque().adicionarItem(paracetamol, 20);
        
        // Inicializar os eventos
        Date dataEvento = new Date();
        Evento show = eventos.criarEvento(
            "Show de Rock",
            "Grande show com bandas locais",
            dataEvento,
            TipoEvento.SHOW,
            100,
            500.0
        );
        eventos.criarIngresso(show, 50.0, "Pista");
        eventos.criarIngresso(show, 80.0, "VIP");
    }
    
    public static void executarSimulacaoInicial() {
        System.out.println("🔄 Executando simulação inicial...");
        System.out.println("Por favor, aguarde...\n");
        
        double saldoInicialRestaurante = restaurante.obterCaixa();
        double saldoInicialClinica = clinica.obterCaixa();
        double saldoInicialEventos = eventos.obterCaixa();
        
        for (int i = 0; i < 10; i++) {
            sistemaRestaurante.simularDia();
            sistemaClinica.simularDia();
            sistemaEventos.simularDia();
            
            // Registrar receitas e despesas
            double receitaRestaurante = restaurante.obterCaixa() - saldoInicialRestaurante;
            double receitaClinica = clinica.obterCaixa() - saldoInicialClinica;
            double receitaEventos = eventos.obterCaixa() - saldoInicialEventos;
            
            historicoReceitas.add(receitaRestaurante + receitaClinica + receitaEventos);
            historicoDespesas.add(0.0); // Simplificado, as despesas já estão descontadas no caixa
            
            saldoInicialRestaurante = restaurante.obterCaixa();
            saldoInicialClinica = clinica.obterCaixa();
            saldoInicialEventos = eventos.obterCaixa();
            
            System.out.println("Dia " + (i + 1) + " concluído.");
        }
        
        System.out.println("\n✅ Simulação inicial concluída!");
        System.out.println("Caixa Restaurante: R$ " + String.format("%.2f", restaurante.obterCaixa()));
        System.out.println("Caixa Clínica: R$ " + String.format("%.2f", clinica.obterCaixa()));
        System.out.println("Caixa Eventos: R$ " + String.format("%.2f", eventos.obterCaixa()));
        System.out.println("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    public static void main(String[] args) {
        // Inicializar o sistema
        inicializarSistema();
        
        // Executar simulação inicial
        executarSimulacaoInicial();
        
        // Mostrar o dashboard
        mostrarDashboard();
        
        // Loop principal da UI
        TecladoHandler teclado = new TecladoHandler();
        try {
            while (true) {
                if (ui.needsRedraw()) {
                    limparTela();
                    String[] lines = ui.renderLines();
                    for (String line : lines) {
                        System.out.println(line);
                    }
                    ui.setRedrawComplete();
                }
                EventoTeclado evento = teclado.lerProximoEvento();
                if (evento.tipo == TipoEventoUI.ESC) {
                    System.out.println("\n=== 👋 ENCERRANDO SISTEMA ===");
                    System.out.println("Obrigado por usar o Sistema Integrado de Gestão!");
                    break;
                }
                ui.processarEvento(evento);
                ui.forceRedraw();
            }
        } finally {
            teclado.fechar();
            scanner.close();
        }
    }
}