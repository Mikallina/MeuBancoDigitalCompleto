package br.com.cdb.MeuBancoDigitalCompleto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.MeuBancoDigitalCompleto.entity.Cliente;
import br.com.cdb.MeuBancoDigitalCompleto.entity.Conta;
import br.com.cdb.MeuBancoDigitalCompleto.entity.ContaCorrente;
import br.com.cdb.MeuBancoDigitalCompleto.entity.ContaPoupanca;
import br.com.cdb.MeuBancoDigitalCompleto.enuns.TipoConta;
import br.com.cdb.MeuBancoDigitalCompleto.repository.ContaRepository;

@Service
public class ContaService {

	@Autowired
	private ContaRepository contaRepository;
	double taxaRendimento = 0;

	public void salvarConta(Cliente cliente, Conta conta, TipoConta tipoConta) {
		if (conta.getNumConta() == null) {
			throw new IllegalArgumentException("Erro: O número da conta é obrigatório.");
		}
		if (cliente == null) {
			throw new IllegalArgumentException("Erro: Cliente inválido");
		}

		if (conta instanceof ContaCorrente) {
			taxaManutencaoCC(cliente, tipoConta, (ContaCorrente) conta);
		}

		if (conta instanceof ContaPoupanca) {
			taxaManutencaoCP(cliente, tipoConta, (ContaPoupanca) conta);
		}

		contaRepository.save(conta);

		System.out.println("Conta salva com sucesso: " + conta.getNumConta());
	}

	public List<Conta> buscarContasPorCliente(Cliente cliente) {
		return contaRepository.findByCliente(cliente);
	}

	public void listarContas() {
		if (contaRepository.findAll().isEmpty()) {
			System.out.println("Nenhuma conta cadastrada");
		} else {
			for (Conta conta : contaRepository.findAll()) {
				System.out.println("Conta: " + conta.getNumConta());
			}
		}
	}

	public String gerarNumeroConta(int agencia, TipoConta tipoConta) {
		StringBuilder conta = new StringBuilder();
		for (int i = 0; i < 7; i++) {
			conta.append((int) (Math.random() * 10));
		}

		// Calcular o dígito verificador (8º número) usando módulo 11
		int soma = 0;
		for (int i = 0; i < conta.length(); i++) {
			soma += (conta.charAt(i) - '0') * (i + 2);
		}

		int dv = soma % 11;
		if (dv == 10) {
			dv = 0;
		}

		conta.append(dv);

		String tipoContaString = tipoConta.getTipoAbreviado();
		return String.format("%s-%04d-%s", tipoContaString, agencia, conta.toString());
	}

	public Conta criarConta(Cliente cliente, int agencia, TipoConta tipoConta) {
		String numConta = gerarNumeroConta(agencia, tipoConta);
		Conta conta;

		if (tipoConta == TipoConta.CORRENTE) {
			conta = new ContaCorrente(cliente, agencia, numConta, tipoConta);
		} else if (tipoConta == TipoConta.POUPANCA) {
			conta = new ContaPoupanca(cliente, agencia, numConta, tipoConta);
		} else {
			throw new IllegalArgumentException("Tipo de conta inválido.");
		}

		return contaRepository.save(conta);
	}

	public boolean aplicarTaxaOuRendimento(Long idConta, TipoConta tipoConta, boolean aplicarTaxa) {
		Conta conta = contaRepository.findById(idConta)
				.orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

		double valorAplicado;

		if (aplicarTaxa) {
			if (!(conta instanceof ContaCorrente)) {
				throw new IllegalArgumentException("Este endpoint é apenas para contas correntes");
			}

			ContaCorrente contaCorrente = (ContaCorrente) conta;
			Cliente cliente = contaCorrente.getCliente();
			valorAplicado = taxaManutencaoCC(cliente, tipoConta, contaCorrente);
			contaCorrente.setSaldo(contaCorrente.getSaldo() - valorAplicado);

		} else {
			if (!(conta instanceof ContaPoupanca)) {
				throw new IllegalArgumentException("Este endpoint é apenas para contas poupanças");
			}

			ContaPoupanca contaPoupanca = (ContaPoupanca) conta;
			Cliente cliente = contaPoupanca.getCliente();
			valorAplicado = taxaManutencaoCP(cliente, tipoConta, contaPoupanca);
			contaPoupanca.setSaldo(contaPoupanca.getSaldo() + valorAplicado);
		}

		contaRepository.save(conta);
		return true;
	}

	public double taxaManutencaoCC(Cliente cliente, TipoConta tipoConta, ContaCorrente contaC) {
		double taxaManutencao = 0;

		if (cliente.getCategoria().getDescricao().equals("Comum")) {

			taxaManutencao = 12;

		} else if (cliente.getCategoria().getDescricao().equals("Super")) {

			taxaManutencao = 8;

		} else {
			taxaManutencao = 2;
		}

		if (contaC.getSaldo() < taxaManutencao) {
			throw new IllegalArgumentException("Saldo insuficiente para aplicar a taxa de manutenção");
		}

		contaC.setTaxaManutencao(taxaManutencao);
		return taxaManutencao;
	}

	public double taxaManutencaoCP(Cliente cliente, TipoConta tipoConta, ContaPoupanca contaP) {
		double saldoAtual = contaP.getSaldo();

		if (cliente.getCategoria().getDescricao().equals("Comum")) {

			taxaRendimento = 0.5;

		} else if (cliente.getCategoria().getDescricao().equals("Super")) {

			taxaRendimento = 0.7;

		} else {
			taxaRendimento = 0.9;
		}

		double taxaMensal = taxaRendimento / 12;
		double saldoRendimento = saldoAtual * Math.pow(1 + (taxaMensal / 100), 1);
		double rendimentoMensal = saldoRendimento - saldoAtual;

		contaP.setTaxaRendimento(taxaRendimento);

		System.out.println("Saldo atual: " + saldoAtual);
		System.out.println("Taxa mensal: " + taxaMensal);
		System.out.println("Rendimento mensal: " + rendimentoMensal);

		return rendimentoMensal;
	}

	public boolean realizarTransferenciaPoupanca(double valor, String numContaOrigem, String numContaDestino) {

		return realizarTransferencia(valor, numContaOrigem, numContaDestino, true, false);
	}

	public boolean realizarTransferenciaPIX(double valor, String numContaOrigem, String chaveDestino) {

		return realizarTransferencia(valor, numContaOrigem, chaveDestino, false, true);
	}

	public boolean realizarTransferenciaOutrasContas(double valor, String numContaOrigem, String numContaDestino) {

		return realizarTransferenciaOutrasContas(valor, numContaOrigem, numContaDestino);
	}

	public double obterSaldo(String cpf) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean realizarDeposito(Long id, double valor) {
		if (valor <= 0) {
			throw new IllegalArgumentException("Valor do depósito não pode ser zero");
		}

		Conta conta = contaRepository.findById(id).orElse(null);
		if (conta == null) {
			throw new RuntimeException("Conta Não encontrada");
		}

		double novoSaldo = conta.getSaldo() + valor;
		conta.setSaldo(novoSaldo);
		contaRepository.save(conta);

		return true;
	}

	public boolean realizarTransferencia(double valor, String numContaOrigem, String numContaDestino,
			boolean transferenciaPoupança, boolean transferenciaPix) {
		Conta contaOrigem = contaRepository.findByNumConta(numContaOrigem);
		if (contaOrigem == null) {
			throw new IllegalArgumentException("Conta de origem não encontrada.");
		}

		if (valor <= 0) {
			throw new IllegalArgumentException("Valor não pode ser zero ou negativo");
		}

		if (valor > contaOrigem.getSaldo()) {
			throw new IllegalArgumentException("Saldo insuficiente");
		}

		contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);

		if (transferenciaPoupança) {
			Conta contaDestino = contaRepository.findByNumConta(numContaDestino);

			if (contaDestino == null) {
				throw new IllegalArgumentException("Conta de destino não encontrada");
			}
			contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
			contaDestino.setSaldo(contaDestino.getSaldo() + valor);
			contaRepository.save(contaDestino);

		}
		if (transferenciaPix) {
			contaOrigem = contaRepository.findByNumConta(numContaOrigem);
			contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
			contaRepository.save(contaOrigem);
		}

		contaRepository.save(contaOrigem);

		return true;

	}

	public Conta buscarContas(String conta) {
		return contaRepository.findByNumConta(conta);
	}
	
	

}
