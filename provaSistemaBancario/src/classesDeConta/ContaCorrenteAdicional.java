package classesDeConta;

import java.io.Serializable;

public class ContaCorrenteAdicional extends Conta implements Serializable {
	// A conta corrente adicional tem um limite de gastos e um saldo disponível que pode variar conforme seus saques
    private double limite;
	private double limiteDisponivel;
    // é sempre vinculada a uma conta principal
	private ContaCorrente contaPrincipal;

	public ContaCorrenteAdicional(String numeroConta, ContaCorrente contaPrincipal, double limite) {
		super(numeroConta);
		this.contaPrincipal = contaPrincipal;
		this.limite = limite;
		this.limiteDisponivel = limite;
	}

    // método especializado pra saque de valores
    @Override
	public boolean sacar(double valor) {
		if (valor <= limiteDisponivel && contaPrincipal.getSaldo() >= valor) {
			if (contaPrincipal.sacar(valor)) {
				limiteDisponivel -= valor;
				return true;
			}
		}
		System.out.println(
				"A conta não pode sacar um valor acima do disponível em limite, ou a conta principal não possui saldo suficiente.");
		return false;
	}

    // mostra o saldo disponível da conta
	@Override
	public double getSaldo() {
		// Se o saldo da conta principal for menor que o limite disponível,
		// retorna o saldo da conta principal
		if (contaPrincipal.getSaldo() < limiteDisponivel) {
			return contaPrincipal.getSaldo();
		}
		// Caso contrário, retorna o limite disponível
		return limiteDisponivel;
	}

	@Override
	public void depositar(double valor) {
		// Deposita o valor na conta principal
		contaPrincipal.depositar(valor);

		// Atualiza o limite disponível, mas não pode ultrapassar o limite máximo
		double novoLimiteDisponivel = limiteDisponivel + valor;
		if (novoLimiteDisponivel > limite) {
			limiteDisponivel = limite;
			System.out.println("Limite disponível restaurado ao máximo de R$ " + limite);
		} else {
			limiteDisponivel = novoLimiteDisponivel;
			System.out.println("Limite disponível atualizado para R$ " + limiteDisponivel);
		}
	}

    // reseta o limite disponível para o limite máximo
	public void resetarLimite() {
		this.limiteDisponivel = this.limite;
	}

    // altera o limite disponível para o novo limite
	public void setNovoLimite(double novoLimite) {
		this.limite = novoLimite;
		this.limiteDisponivel = novoLimite;
	}
}
