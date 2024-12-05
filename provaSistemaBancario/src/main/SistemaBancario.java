package main;

import sistemaClasse.Banco;
// classe principal que inicia o sistema bancário
public class SistemaBancario {

    // método principal que inicia o sistema bancário
    public static void main(String[] args) {
        Banco banco = new Banco();
        banco.carregarDados();
        banco.menuInicial();
    }
}