package br.com.cdb.MeuBancoDigitalCompleto.entity;

import br.com.cdb.MeuBancoDigitalCompleto.enuns.TipoConta;
import jakarta.persistence.Entity;

@Entity
public class ContaPoupanca extends Conta {

	private double taxaRendimento;


	public ContaPoupanca() {}

	public ContaPoupanca(Cliente cliente, int agencia, String numConta, TipoConta tipoConta) {
		super(cliente, agencia, numConta);
		this.tipoConta = tipoConta;

	}

	public ContaPoupanca(Cliente cliente, int agencia, String numConta, double taxaRendimento, TipoConta tipoConta) {
		super(cliente, agencia, numConta);
		this.taxaRendimento = taxaRendimento;
	}

	public double getTaxaRendimento() {
		return taxaRendimento;
	}

	public void setTaxaRendimento(double taxaRendimento) {
		this.taxaRendimento = taxaRendimento;
	}

	@Override
	public void exibirSaldo() {
		System.out.println("Saldo conta poupança: " + saldo);
	}

	public void aplicarRendimento() {
		saldo += saldo * taxaRendimento;
	}

	@Override
	protected double getSaldoTotal() {
		return saldo;
	}

	@Override
	protected Conta[] values() {
		// TODO Auto-generated method stub
		return null;
	}
}
