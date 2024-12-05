package classesDeUsuarios;

import java.util.Scanner;
import sistemaClasse.Banco;

public class Bancario extends Usuario {
	public Bancario(String username, String senha) {
		super(username, senha);
	}

    // menu do bancario com todas as opcoes q ele pode utilizar
	@Override
	public void menu(Banco banco) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("\nMenu do Bancário");
			System.out.println("1. Depositar\n2. Sacar\n3. Transferir\n4. Consultar Saldo\n5. Voltar");
			System.out.print("Escolha uma opção: ");
			int opcao = scanner.nextInt();
			scanner.nextLine();

			switch (opcao) {
			case 1:
				System.out.print("Conta destino: ");
				String contaDestino = scanner.nextLine();
				System.out.print("Valor: ");
				double valorDeposito = scanner.nextDouble();
				depositar(banco, contaDestino, valorDeposito);
				break;
			case 2:
				System.out.print("Conta origem: ");
				String contaOrigem = scanner.nextLine();
				System.out.print("Valor: ");
				double valorSaque = scanner.nextDouble();
				sacar(banco, contaOrigem, valorSaque);
				break;
			case 3:
				System.out.print("Conta origem: ");
				contaOrigem = scanner.nextLine();
				System.out.print("Conta destino: ");
				contaDestino = scanner.nextLine();
				System.out.print("Valor: ");
				double valorTransferencia = scanner.nextDouble();
				transferir(banco, contaOrigem, contaDestino, valorTransferencia);
				break;
			case 4:
				System.out.print("Conta para consulta: ");
				String contaConsulta = scanner.nextLine();
				try {
					double saldo = banco.consultarSaldo(contaConsulta);
					System.out.printf("Saldo da conta %s: R$ %.2f\n", contaConsulta, saldo);
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				}
				break;
			case 5:
				return;
			default:
				System.out.println("Opção inválida!");
			}
		}
	}
}
