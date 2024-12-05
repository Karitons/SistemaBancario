package classesDeUsuarios;

import java.io.Serializable;
import java.util.Scanner;

import classesDeConta.Conta;
import classesDeConta.ContaCorrente;
import classesDeConta.ContaCorrenteAdicional;
import classesDeConta.ContaPoupanca;
import enums.TipoConta;
import sistemaClasse.Banco;

public class Cliente extends Usuario implements Serializable {
    // variáveis que guardam as contas do cliente. Um cliente pode ter apenas uma conta por tipo, mas caso seja uma conta corrente ele pode ter várias adicionais vinculadas a conta dele.
	private ContaCorrente contaCorrentePrincipal;
	private ContaCorrenteAdicional adicional;
	private ContaPoupanca contaPoupanca;
	private TipoConta tipo = null;

	public Cliente(String username, String senha, TipoConta tipo) {
		super(username, senha);
		this.tipo = tipo;
	}

	public Conta getConta() {
		return getContaOperacao(tipo);
	}

    // cria conta corrente principal
	public void criarContaCorrente(String numeroConta) {
		if (this.contaCorrentePrincipal == null) {
			this.contaCorrentePrincipal = new ContaCorrente(numeroConta);
		} else {
			System.out.println("Conta Corrente Principal já existe. Use adicionarContaAdicional.");
		}
	}

    // cria conta poupança
	public void criarContaPoupanca(String numeroConta) {
		if (this.contaPoupanca == null) {
			this.contaPoupanca = new ContaPoupanca(numeroConta);
		} else {
			System.out.println("Conta Poupança já existente.");
		}
	}

    // menu do cliente com todas as opçoes q ele pode fazer
	@Override
	public void menu(Banco banco) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("\n Menu do Cliente");
			System.out.println("1. Consultar Saldo\n2. Depositar\n3. Sacar\n4. Transferir" +
							  "\n5. Pagar Cheque Especial\n6. Resetar Limites Adicionais\n7. Voltar");
			System.out.print("Escolha uma opção: ");
			int opcao = scanner.nextInt();
			scanner.nextLine();

			switch (opcao) {
			case 1:
				consultarSaldo();
				break;
			case 2:
				System.out.print("Valor para depósito: ");
				double valorDeposito = scanner.nextDouble();
				getContaOperacao(tipo).depositar(valorDeposito);
				break;
			case 3:
				System.out.print("Valor para saque: ");
				double valorSaque = scanner.nextDouble();
				if (!getContaOperacao(tipo).sacar(valorSaque))
					System.out.println("Valor inserido é maior que o disponível.");
				break;
			case 4:
				System.out.print("Conta destino: ");
				String contaDestino = scanner.nextLine();
				System.out.print("Valor para transferência: ");
				double valorTransferencia = scanner.nextDouble();
				transferir(banco, this.getUsername(), contaDestino, valorTransferencia);
				break;
			case 5:
				if (tipo == TipoConta.Corrente) {
					System.out.print("Valor para pagar do cheque especial: ");
					double valorPagamento = scanner.nextDouble();
					ContaCorrente cc = (ContaCorrente) getContaOperacao(tipo);
					cc.pagarChequeEspecial(valorPagamento);
				} else {
					System.out.println("Apenas contas correntes podem ter cheque especial.");
				}
				break;
			case 6:
				if (tipo == TipoConta.Corrente) {
					resetarLimitesAdicionais();
				} else {
					System.out.println("Apenas contas correntes principais podem resetar limites.");
				}
				break;
			case 7:
				return;
			default:
				System.out.println("Opção inválida!");
			}
		}
	}

	// retorno sempre a conta correta para que possa utilizar algum tipo de operação
	private Conta getContaOperacao(TipoConta tipo) {
		if (tipo == TipoConta.Corrente)
			return contaCorrentePrincipal;
		else if (tipo == TipoConta.Poupanca)
			return contaPoupanca;
		else {
			return adicional;
		}
	}

    // mostra saldo da conta
	private void consultarSaldo() {
		System.out.println("Saldo: " + getContaOperacao(tipo).getSaldo());
	}

    // retorna a conta corrente principal
	public ContaCorrente getContaCorrente() {
		return contaCorrentePrincipal;
	}

    // cria conta adicional
	public void criarContaAdicional(String numeroConta, ContaCorrente contaPrincipal, double limite) {
		this.adicional = new ContaCorrenteAdicional(numeroConta, contaPrincipal, limite);
		contaPrincipal.adicionarContaAdicional(this.adicional);
	}

    // retorna o tipo de conta
	public TipoConta getTipo() {
		return tipo;
	}

    // permite que o cliente resete os limites das contas adicionais vinculadas a conta principal
	private void resetarLimitesAdicionais() {
		if (contaCorrentePrincipal != null && !contaCorrentePrincipal.getContasAdicionais().isEmpty()) {
			for (ContaCorrenteAdicional contaAdicional : contaCorrentePrincipal.getContasAdicionais()) {
				contaAdicional.resetarLimite();
			}
			System.out.println("Limites das contas adicionais resetados com sucesso!");
		} else {
			System.out.println("Não existem contas adicionais vinculadas.");
		}
	}
}
