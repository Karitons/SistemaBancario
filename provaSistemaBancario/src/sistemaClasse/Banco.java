package sistemaClasse;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import classesDeConta.ContaCorrente;
import classesDeConta.ContaCorrenteAdicional;
import classesDeUsuarios.Bancario;
import classesDeUsuarios.Cliente;
import classesDeUsuarios.Gerente;
import classesDeUsuarios.Usuario;
import enums.TipoConta;

public class Banco {
	private Map<String, Usuario> usuarios = new HashMap<>();
	private Scanner scanner = new Scanner(System.in);
	private static final String BancoDeDados = "usuarios.dat";

	// carrega os dados do arquivo e se der erro cria outro arquivo novo
	public void carregarDados() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BancoDeDados))) {
			usuarios = (Map<String, Usuario>) ois.readObject();
			System.out.println("Dados carregados com sucesso.");
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo de dados não encontrado. Criando novo arquivo.");
			criarNovoArquivo();
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Falha ao carregar os dados. Criando novo arquivo.");
			File arquivo = new File(BancoDeDados);
			if (arquivo.exists()) {
				arquivo.delete();
				System.out.println("Arquivo corrompido foi deletado.");
			}
			criarNovoArquivo();
		}

		// cria um gerente quando não existir um
		if (!usuarios.containsKey("admin")) {
			Gerente gerente = new Gerente("admin", "admin");
			usuarios.put(gerente.getUsername(), gerente);
		}
		
		// cria um agente bancário quando não existir um no arquivo salvo
		if (!usuarios.containsKey("bancario")) {
			Bancario bancario = new Bancario("bancario", "bancario");
			usuarios.put(bancario.getUsername(), bancario);
		}
	}
    //metodo responsavel por criar um arquivo
	private void criarNovoArquivo() {
		usuarios = new HashMap<>();
		try {
			salvarDados();
			System.out.println("Novo arquivo de dados criado com sucesso.");
		} catch (Exception e) {
			System.out.println("Falha ao criar novo arquivo de dados.");
			e.printStackTrace();
		}
	}

	// salva os dados no arquivo pra nao perder as informacoes
	public void salvarDados() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BancoDeDados))) {
			oos.writeObject(usuarios); // Salva o mapa de usuários no arquivo
		} catch (IOException e) {
			System.out.println("Falha ao salvar os dados.");
			e.printStackTrace();
		}
	}

	// menu principal do sistema q mostra login e sair
	public void menuInicial() {
		while (true) {
			System.out.println("\n Bem-vindo ao Sistema Bancário");
			System.out.println("1. Login\n2. Sair");
			System.out.print("Escolha uma opção: ");

			int opcao = scanner.nextInt();
			scanner.nextLine(); 

			switch (opcao) {
			case 1:
				login();
				break;
			case 2:
				salvarDados();
				System.exit(0);
				break;
			default:
				System.out.println("Opção inválida!");
			}
		}
	}

	// faz o login do usuario e chama o menu especifico dele, usando conceito de herança, o menu é personalizado para cada tipo específico de usuário
	private void login() {
		System.out.print("Usuário: ");
		String username = scanner.nextLine();
		System.out.print("Senha: ");
		String senha = scanner.nextLine();

		Usuario usuario = usuarios.get(username);
		if (usuario != null && usuario.autenticar(senha)) {
			usuario.menu(this);
		} else {
			System.out.println("Credenciais inválidas!");
		}
	}

	// cria cliente novo com conta corrente ou poupanç a
	public void criarNovoCliente(String username, String senha, String tipoConta) {
		TipoConta tipo = TipoConta.fromString(tipoConta);
		Cliente novoCliente = new Cliente(username, senha, tipo);
		String numeroConta = gerarNumeroConta(); 

		switch (tipo) {
		case Corrente:
			novoCliente.criarContaCorrente(numeroConta);
			break;
		case Poupanca:
			novoCliente.criarContaPoupanca(numeroConta);
			break;
		default:
			System.out.println("Tipo de conta inválido para um novo cliente.");
			return;
		}

		adicionarUsuario(novoCliente);
		System.out.println("Cliente criado com sucesso!");
	}

	// gera numero aleatório pra conta nova
	private String gerarNumeroConta() {
		return "AC" + (int) (Math.random() * 100000);
	}

	// adiciona usuario novo no map ade usuarios
	public void adicionarUsuario(Usuario usuario) {
		usuarios.put(usuario.getUsername(), usuario);
	}

	// procura usuario pelo nome dele
	public Usuario buscarUsuario(String username) {
		return usuarios.get(username);
	}

	// ve o saldo da conta do cliente
	public double consultarSaldo(String contaDestino) {
		Usuario usuario = usuarios.get(contaDestino);
		if (usuario instanceof Cliente) {
			Cliente cliente = (Cliente) usuario;
			return cliente.getConta().getSaldo();
		} else {
			throw new IllegalArgumentException("Conta não encontrada ou não é de um cliente válido.");
		}
	}

	// deposita dinhero na conta, se for gerente ou bancário pede conta destino
	public boolean depositar(String username, double valor) {
		Usuario usuario = usuarios.get(username);
		if (usuario instanceof Cliente) {
			Cliente cliente = (Cliente) usuario;
			cliente.getConta().depositar(valor);
			salvarDados();
			return true;
		} else if (usuario instanceof Gerente || usuario instanceof Bancario) {
			System.out.print("Conta destino: ");
			String contaDestino = scanner.nextLine();
			Cliente correntistaDestino = (Cliente) usuarios.get(contaDestino);
			correntistaDestino.depositar(this, contaDestino, valor);
			salvarDados();
			return true;
		} else {
			System.out.println("Usuário inválido para realizar depósito.");
			return false;
		}
	}

	// tira dinhiero da conta se tiver saldo suficiente
	public boolean sacar(String username, double valor) {
		Usuario usuario = usuarios.get(username);
		if (usuario instanceof Cliente) {
			Cliente cliente = (Cliente) usuario;
			boolean sucesso = cliente.getConta().sacar(valor);
			if (sucesso) {
				salvarDados();
			}
			return sucesso;
		} else if (usuario instanceof Gerente || usuario instanceof Bancario) {
			System.out.print("Conta origem: ");
			String contaOrigem = scanner.nextLine();
			Cliente correntistaOrigem = (Cliente) usuarios.get(contaOrigem);
			boolean sucesso = correntistaOrigem.sacar(this, contaOrigem, valor);
			if (sucesso) {
				salvarDados();
			}
			return sucesso;
		} else {
			System.out.println("Usuário inválido para realizar saque.");
			return false;
		}
	}

	// transfere dinheiro de uma conta pra outra
	public boolean transferir(String origemUsername, String destinoUsername, double valor) {
		Usuario origem = usuarios.get(origemUsername);
		Usuario destino = usuarios.get(destinoUsername);

		if (origem instanceof Cliente && destino instanceof Cliente) {
			Cliente origemCorrentista = (Cliente) origem;
			Cliente destinoCorrentista = (Cliente) destino;

			if (origemCorrentista.getConta().sacar(valor)) {
				destinoCorrentista.getConta().depositar(valor);
				System.out.println("Transferência realizada com sucesso.");
				salvarDados();
				return true;
			} else {
				System.out.println("Saldo insuficiente para transferência.");
				return false;
			}
		} else {
			System.out.println("Usuários inválidos para realizar transferência.");
			return false;
		}
	}

	// cria conta adicional vinculada numa conta corrente principal
	public void criarContaAdicional(String usernameContaPrincipal, String usernameContaAdicional, String senha,
			double limite) {
		Usuario usuarioPrincipal = usuarios.get(usernameContaPrincipal);

		if (!(usuarioPrincipal instanceof Cliente)) {
			System.out.println("Conta principal não encontrada ou não é uma conta de cliente.");
			return;
		}

		Cliente clientePrincipal = (Cliente) usuarioPrincipal;
		if (clientePrincipal.getTipo() != TipoConta.Corrente) {
			System.out.println("Apenas contas correntes podem ter contas adicionais.");
			return;
		}

		Cliente novoCliente = new Cliente(usernameContaAdicional, senha, TipoConta.CorrenteAdicional);
		String numeroConta = gerarNumeroConta();
		novoCliente.criarContaAdicional(numeroConta, clientePrincipal.getContaCorrente(), limite);

		adicionarUsuario(novoCliente);
		System.out.println("Conta adicional criada com sucesso!");
	}

	// muda o limite de gasto da conta adicional
	public void alterarLimiteContaAdicional(String username, double novoLimite) {
		Usuario usuario = usuarios.get(username);
		if (usuario instanceof Cliente) {
			Cliente cliente = (Cliente) usuario;
			if (cliente.getTipo() == TipoConta.CorrenteAdicional) {
				ContaCorrenteAdicional contaAdicional = (ContaCorrenteAdicional) cliente.getConta();
				contaAdicional.setNovoLimite(novoLimite);
				salvarDados();
				System.out.println("Limite alterado com sucesso!");
			} else {
				System.out.println("Esta conta não é uma conta adicional.");
			}
		} else {
			System.out.println("Usuário não encontrado ou não é um cliente.");
		}
	}

	// cria funcionario bancario novo no sistema, somente gerente tem acesso a este metodo
	public void criarNovoBancario(String username, String senha) {
		if (usuarios.containsKey(username)) {
			System.out.println("Usuário já existe!");
			return;
		}
		
		Bancario novoBancario = new Bancario(username, senha);
		adicionarUsuario(novoBancario);
		salvarDados();
		System.out.println("Bancário cadastrado com sucesso!");
	}

    // libera cheque especial para a conta corrente do cliente
	public void liberarChequeEspecial(String username, double limite) {
		Usuario usuario = usuarios.get(username);
		if (usuario instanceof Cliente) {
			Cliente cliente = (Cliente) usuario;
			if (cliente.getTipo() == TipoConta.Corrente) {
				ContaCorrente conta = cliente.getContaCorrente();
				conta.setLimiteChequeEspecial(limite);
				salvarDados();
				System.out.println("Cheque especial liberado com sucesso!");
			} else {
				System.out.println("Apenas contas correntes podem ter cheque especial.");
			}
		} else {
			System.out.println("Usuario nao encontrado ou nao e cliente.");
		}
	}
}
