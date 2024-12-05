package classesDeConta;

import java.io.Serializable;

public abstract class Conta implements Serializable {
	private String numeroConta;
	private double saldo;

    // consttrutor
	public Conta(String numeroConta) {
		this.numeroConta = numeroConta;
		this.saldo = 0;
	}

	// Abaixo estão todos os métodos que podem ser utilizados em qualquer tipo de conta, entretanto cada tipo de conta pode ter métodos específicos, aqui utilizamos conceito de poo com polimorfismo por meio de sobrescrita de métodos
	// isso é possivel por meio de outro conceito de poo chamado de abstração, onde criamos uma classe abstrata e classes concretas que herdam da classe abstrata
	// outro conceito que permite que isso seja possível é a herança, onde esta é a classe abstrata e as classes ContaCorrente, ContaPoupanca e ContaCorrenteAdicional são classes específicas que herdam de Conta

    // deposita um valor na conta
	public void depositar(double valor) {
		this.saldo += valor;
	}
    // saca um valor da conta
	public boolean sacar(double valor) {
		if (saldo >= valor) {
			saldo -= valor;
			return true;
		}
		return false;
	}

    // retorna o número da conta
	public String getNumeroConta() {
		return numeroConta;
	}

    // retorna o saldo da conta
	public double getSaldo() {
		return saldo;
	}

}