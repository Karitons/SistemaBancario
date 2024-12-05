package classesDeConta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContaCorrente extends Conta implements Serializable {
	private List<ContaCorrenteAdicional> contasAdicionais;
	private double limiteChequEspecial; // valor aprovado pelo gerente
	private double limiteDisponivel;    // quanto ainda pode usar do cheque

    // construtor
	public ContaCorrente(String numeroConta) {
		super(numeroConta);
		this.contasAdicionais = new ArrayList<>();
		this.limiteChequEspecial = 0;  
		this.limiteDisponivel = 0;
	}

    // adiciona uma conta adicional a lista de contas adicionais da conta corrente principal
	public void adicionarContaAdicional(ContaCorrenteAdicional contaAdicional) {
		contasAdicionais.add(contaAdicional);
	}

    // retorna a lista de contas adicionais da conta corrente principal
	public List<ContaCorrenteAdicional> getContasAdicionais() {
		return contasAdicionais;
	}

    // sobrescreve o saque pra considerar o cheque especial
	@Override
	public boolean sacar(double valor) {
		double saldoTotal = super.getSaldo() + limiteDisponivel;
		
		if (valor <= saldoTotal) {
			if (valor <= super.getSaldo()) {
				// tem dinhero na conta, usa ele primeiro
				return super.sacar(valor);
			} else {
				// usa o saldo + parte do cheque especial
				double restante = valor - super.getSaldo();
				super.sacar(super.getSaldo());  // zera a conta
				limiteDisponivel -= restante;   // usa o cheque especial
				return true;
			}
		}
		return false;
	}

    // mostra saldo + cheque especial
	@Override
	public double getSaldo() {
		double saldoConta = super.getSaldo();
		double chequeUsado = limiteChequEspecial - limiteDisponivel;
		
		System.out.printf("Saldo em conta: R$ %.2f\n", saldoConta);
		System.out.printf("Cheque especial usado: R$ %.2f\n", chequeUsado);
		System.out.printf("Cheque especial disponível: R$ %.2f\n", limiteDisponivel);
		
		return saldoConta + limiteDisponivel;
	}

    // so gerente pode usar pra liberar cheque especial
	public void setLimiteChequeEspecial(double novoLimite) {
		this.limiteChequEspecial = novoLimite;
		this.limiteDisponivel = novoLimite;  
	}

    // deposita um valor na conta
	@Override
	public void depositar(double valor) {
		super.depositar(valor);
	}

    // metodo pra pagar o cheque especial usando o saldo da conta
	public boolean pagarChequeEspecial(double valor) {
		double chequeUsado = limiteChequEspecial - limiteDisponivel;
		
		if (chequeUsado == 0) {
			System.out.println("Não há cheque especial a ser pago!");
			return false;
		}
		
		if (valor > super.getSaldo()) {
			System.out.println("Saldo insuficiente para pagar o cheque especial!");
			return false;
		}
		
		if (valor > chequeUsado) {
			System.out.println("Valor maior que a divida do cheque especial!");
			return false;
		}
		
		// paga o valor do cheque usando o saldo da conta
		super.sacar(valor);
		limiteDisponivel += valor;  // aumenta o limite disponivel
		
		System.out.printf("Cheque especial pago: R$ %.2f\n", valor);
		System.out.printf("Cheque especial ainda em uso: R$ %.2f\n", limiteChequEspecial - limiteDisponivel);
		return true;
	}
}