package app.acao.restaurante;

import app.acao.Acao;
import app.restaurante.Restaurante;

public class RealizarInventario extends Acao {
    Restaurante restaurante;

    public RealizarInventario(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    @Override
    public void executar() {
        System.out.println("  Realizando inventário do restaurante...");
        System.out.println("  Verificando quantidade de ingredientes");
        System.out.println("  Identificando itens com baixo estoque");
        System.out.println("  Calculando valor total do estoque");
    }

    @Override
    public String obterNome() {
        return "Realizar Inventário";
    }

    @Override
    public String obterDescricao() {
        return "Realizar inventário completo do estoque do restaurante";
    }
}