package classesDeUsuarios;

import java.io.Serializable;

import sistemaClasse.Banco;

// Outra classe abstrata. 
// Aqui utilizamos mais uma vez os conceitos de POO como abstração, onde criamos uma classe abstrata e classes concretas que herdam da classe abstrata
// Usamos nas classes específicas conceitos de herança, onde a classe Usuario é a classe abstrata e as classes Cliente, Bancario e Gerente são classes concretas que herdam de Usuario

public abstract class Usuario implements Serializable {
	private String username;
	private String senha;

	public Usuario(String username, String senha) {
		this.username = username;
		this.senha = senha;
	}

    // retorna o username do usuário
	public String getUsername() {
		return username;
	}

    // autentica o usuário, verifica se a senha informada no parâmetro é igual a senha do usuário
	public boolean autenticar(String senha) {
		return this.senha.equals(senha);
	}

    // método abstrato que será implementado nas classes concretas Cliente e Bancario, o menu é personalizado para cada tipo de usuário
	public abstract void menu(Banco banco);

	// Método de depósito
	public void depositar(Banco banco, String contaDestino, double valor) {
		// Realiza o depósito através do método depositar do Banco
		boolean sucesso = banco.depositar(contaDestino, valor);
		if (sucesso) {
			System.out.println("Depósito realizado com sucesso.");
		} else {
			System.out.println("Erro ao realizar o depósito.");
		}
	}

	// Método de saque
	public boolean sacar(Banco banco, String contaOrigem, double valor) {
		// Realiza o saque através do método sacar do Banco
		boolean sucesso = banco.sacar(contaOrigem, valor);
		if (sucesso) {
			System.out.println("Saque realizado com sucesso.");
			return true;
		} else {
			System.out.println("Erro ao realizar o saque.");
			return false;
		}
	}

	// Método de transferência
	public void transferir(Banco banco, String contaOrigem, String contaDestino, double valor) {
		// Realiza a transferência através do método transferir do Banco
		boolean sucesso = banco.transferir(contaOrigem, contaDestino, valor);
		if (sucesso) {
			System.out.println("Transferência realizada com sucesso.");
		} else {
			System.out.println("Erro ao realizar a transferência.");
		}
	}
}
