package classesDeConta;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ContaPoupanca extends Conta implements Serializable {
	private LocalDateTime dataCriacao;     // guarda data q criou a conta pra calcular o rendimento
	private static final double TAXA_RENDIMENTO = 0.05; // 5% por minuto

    // construtor
	public ContaPoupanca(String numeroConta) {
		super(numeroConta);
		this.dataCriacao = LocalDateTime.now();
	}

    // deposita um valor a conta e guarda separado pra calcular rendimento dps
	@Override
	public void depositar(double valor) {
		super.depositar(valor);
	}

    // método especializado pra saque
	@Override
	public boolean sacar(double valor) {
		// Atualiza o saldo base com o rendimento, pois o valor total passa a ser o saldo base + rendimento
		super.depositar(calcularRendimento());
		
		// Agora pode sacar normalmente pois o saldo já inclui o rendimento
		return super.sacar(valor);
	}

    // mostra quanto tem guardado, quanto rendeu e retorna total
	@Override
	public double getSaldo() {
		double saldoBase = super.getSaldo();
		double rendimento = calcularRendimento();
		
		System.out.printf("Valor base: R$ %.2f\n", saldoBase);
		System.out.printf("Rendimento: R$ %.2f\n", rendimento);
		
		return saldoBase + rendimento;
	}

    // CAlcula quanto rendeu baseado no tempo q passou desde a data de criaçao
	private double calcularRendimento() {
		long minutosDecorridos = ChronoUnit.MINUTES.between(dataCriacao, LocalDateTime.now());
		return super.getSaldo() * TAXA_RENDIMENTO * minutosDecorridos;
	}

}
