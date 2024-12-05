package classesDeUsuarios;

import java.io.Serializable;
import java.util.Scanner;

import enums.TipoConta;
import sistemaClasse.Banco;

public class Gerente extends Usuario implements Serializable {
	public Gerente(String username, String senha) {
		super(username, senha);
	}

    // menu principal do gerente com todas as opcoes que ele pode fazer,como cadastrar cliente, bancario, movimentar dinheiro etc
	@Override
	public void menu(Banco banco) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("\n Menu do Gerente ");
			System.out.println("1. Cadastrar Cliente\n2. Cadastrar Bancário\n3. Depositar\n4. Sacar\n5. Transferir"
					+ "\n6. Consultar Saldo\n7. Alterar Limite Conta Adicional\n8. Liberar Cheque Especial\n9. Voltar");
			System.out.print("Escolha uma opção: ");
			int opcao = scanner.nextInt();
			scanner.nextLine();

			switch (opcao) {
			case 1:
				cadastrarCliente(banco);
				break;
			case 2:
				cadastrarBancario(banco);
				break;
			case 3:
				System.out.print("Conta destino: ");
				String contaDestino = scanner.nextLine();
				System.out.print("Valor: ");
				double valorDeposito = scanner.nextDouble();
				depositar(banco, contaDestino, valorDeposito);
				break;
			case 4:
				System.out.print("Conta origem: ");
				String contaOrigem = scanner.nextLine();
				System.out.print("Valor: ");
				double valorSaque = scanner.nextDouble();
				sacar(banco, contaOrigem, valorSaque);
				break;
			case 5:
				System.out.print("Conta origem: ");
				contaOrigem = scanner.nextLine();
				System.out.print("Conta destino: ");
				String contaDestinoTransfer = scanner.nextLine();
				System.out.print("Valor: ");
				double valorTransferencia = scanner.nextDouble();
				transferir(banco, contaOrigem, contaDestinoTransfer, valorTransferencia);
				break;
			case 6:
				System.out.print("Conta para consulta: ");
				String contaConsulta = scanner.nextLine();
				try {
					double saldo = banco.consultarSaldo(contaConsulta);
					System.out.printf("Saldo da conta %s: R$ %.2f\n", contaConsulta, saldo);
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				}
				break;
			case 7:
				alterarLimiteContaAdicional(banco);
				break;
			case 8:
				liberarChequeEspecial(banco);
				break;
			case 9:
				return;
			default:
				System.out.println("Opção inválida!");
			}
		}
	}

    // Cadastra cliente novo no banco
    // Pede nome, usuario, senha e tipo da conta,  se for adicional pede a conta principal e limite
	private void cadastrarCliente(Banco banco) {
        // pega os dados do cliente novo
		Scanner scanner = new Scanner(System.in);
		System.out.print("Nome do usuario: ");
		String nome = scanner.nextLine();
		System.out.print("Usuário: ");
		String username = scanner.nextLine();
		System.out.print("Senha: ");
		String senha = scanner.nextLine();
		System.out.print("Digite o tipo da conta (corrente, adicional ou poupanca): ");
		String tipo = scanner.nextLine();

        // se for conta adicional cria vinculada na principal
		if (tipo.equalsIgnoreCase("adicional")) {
			System.out.print("Username da conta principal: ");
			String usernamePrincipal = scanner.nextLine();
			System.out.print("Limite de gastos: ");
			double limite = scanner.nextDouble();
			banco.criarContaAdicional(usernamePrincipal, username, senha, limite);
		} else {
			banco.criarNovoCliente(username, senha, tipo);
		}
	}
    
    // cadastra funcionario bancario novo
	private void cadastrarBancario(Banco banco) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Usuário: ");
		String username = scanner.nextLine();
		System.out.print("Senha: ");
		String senha = scanner.nextLine();
		
		banco.criarNovoBancario(username, senha);
	}
    // muda o limite q uma conta adicional pode gastar, pede a conta e o limite novo
	private void alterarLimiteContaAdicional(Banco banco) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Username da conta adicional: ");
		String username = scanner.nextLine();
		System.out.print("Novo limite: ");
		double novoLimite = scanner.nextDouble();

		banco.alterarLimiteContaAdicional(username, novoLimite);
	}

    // libera cheque especial para a conta corrente do cliente
	private void liberarChequeEspecial(Banco banco) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Username do cliente: ");
		String username = scanner.nextLine();
		System.out.print("Valor do cheque especial: ");
		double limite = scanner.nextDouble();
		
		banco.liberarChequeEspecial(username, limite);
	}
}
