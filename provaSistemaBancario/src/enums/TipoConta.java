package enums;

// Enumeração que define os tipos de contas disponíveis
public enum TipoConta {
    Corrente,
    CorrenteAdicional,
    Poupanca;

    // método que converte uma string para um tipo de conta, vai ser utilizado para converter a entrada do usuário para o tipo de conta correto, caso o usuário digite algo que não corresponda a nenhum tipo de conta, será lançada uma exceção
    public static TipoConta fromString(String tipo) {
        switch (tipo.toLowerCase()) {
            case "corrente":
                return Corrente;
            case "adicional":
                return CorrenteAdicional;
            case "poupanca":
                return Poupanca;
            default:
                throw new IllegalArgumentException("Tipo de conta inválido: " + tipo);
        }
    }
}
