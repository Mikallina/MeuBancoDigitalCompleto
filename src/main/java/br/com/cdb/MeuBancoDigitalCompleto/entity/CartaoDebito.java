package br.com.cdb.MeuBancoDigitalCompleto.entity;

import br.com.cdb.MeuBancoDigitalCompleto.enuns.TipoCartao;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class CartaoDebito extends Cartao {

	private double taxa = 0.05;
	
	@ManyToOne
	private Conta conta;
	private double limiteDiario;
	private double totalPgtoHoje = 0;
	
	
	public CartaoDebito() {}

	public CartaoDebito(Conta conta, String numCartao, TipoCartao tipoCartao, int senha, double limiteDiario) {
		super(numCartao, conta, tipoCartao, senha, true);
		this.tipoCartao = tipoCartao;
		this.limiteDiario = limiteDiario;
	}
	
	public void alterarLimiteDebito(double novoLimite) {
		this.limiteDiario = novoLimite;

	}

	public void realizarPagamento(double pagamento) {
		if (!verificarStatus()) {
			System.out.println("Cartão Desativado.");
			return;
		}
		if (conta.getSaldo() >= pagamento && totalPgtoHoje + pagamento <= limiteDiario) {
			totalPgtoHoje += pagamento;
			double result = conta.getSaldo() - pagamento + (pagamento * taxa);
			System.out.println("Pagamento Efetuado com sucesso" + result);

		} else if (conta.getSaldo() < pagamento) {
			System.out.println("Saldo Insuficente...");
		} else {
			System.out.println("Limite diário excedido....");
		}
	}

	public void alterarLimiteDiario(double novoLimite) {
		this.limiteDiario = novoLimite;
	}


	public double getTaxa() {
		return taxa;
	}

	public void setTaxa(double taxa) {
		this.taxa = taxa;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public double getLimiteDiario() {
		return limiteDiario;
	}

	public void setLimiteDiario(double limiteDiario) {
		this.limiteDiario = limiteDiario;
	}

	public double getTotalPgtoHoje() {
		return totalPgtoHoje;
	}

	public void setTotalPgtoHoje(double totalPgtoHoje) {
		this.totalPgtoHoje = totalPgtoHoje;
	}



}
