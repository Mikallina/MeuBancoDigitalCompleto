package br.com.cdb.MeuBancoDigitalCompleto.entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import br.com.cdb.MeuBancoDigitalCompleto.enuns.TipoCartao;
import jakarta.persistence.Entity;

@Entity
public class CartaoCredito extends Cartao {

	protected double taxa = 0.5;
	protected LocalDate dataVencimento;
	protected LocalDate dataCompra;
	protected double pagamento;
	protected double saldoCredito;
	protected double saldoMes;
	protected String diaVencimento;

	private double limiteCredito;


	
	public CartaoCredito() {
	}

	public CartaoCredito(Conta conta, int senha, String numCartao, TipoCartao tipoCartao, double limiteCredito,
			String diaVencimento, LocalDate dataVencimento) {
		super(numCartao, conta, tipoCartao, senha, true);
		this.limiteCredito = limiteCredito;
		this.tipoCartao = tipoCartao;
		this.diaVencimento = diaVencimento;
		this.dataVencimento = dataVencimento;

	}

	public void alterarLimiteCredito(double novoLimite) {
		this.limiteCredito = novoLimite;

	}

	public void setDiaVencimento(String diaVencimento) {
		this.diaVencimento = diaVencimento;
	}

	public double getLimiteCredito() {
		return limiteCredito;
	}

	public double getSaldoCredito() {
		return saldoCredito;
	}

	public void setSaldoCredito(double saldoCredito) {
		this.saldoCredito = saldoCredito;
	}

	public double getSaldoMes() {
		return saldoMes;
	}

	public double getTaxa() {
		return taxa;
	}

	public void setTaxa(double taxa) {
		this.taxa = taxa;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public LocalDate getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(LocalDate dataCompra) {
		this.dataCompra = dataCompra;
	}

	public double getPagamento() {
		return pagamento;
	}

	public void setPagamento(double pagamento) {
		this.pagamento = pagamento;
	}

	public void setSaldoMes(double saldoMes) {
		this.saldoMes = saldoMes;
	}

	public void setLimiteCredito(double limiteCredito) {
		this.limiteCredito = limiteCredito;
	}
	
	public String getDiaVencimento() {
		return diaVencimento;
	}


	public boolean novoCiclo() {
		long diaUltimaCompra = ChronoUnit.DAYS.between(dataVencimento, dataCompra);
		return diaUltimaCompra >= 30;
	}

	public void realizarPagamentos() {
		if (!verificarStatus()) {
			System.out.println("Cartão de crédito desativado");
			return;
		}

		if (novoCiclo()) {
			saldoMes = 0;
			System.out.println("Novo ciclo iniciado...");
			dataCompra = LocalDate.now();
		}

		if (saldoMes + pagamento <= saldoCredito) {
			saldoMes += pagamento;
			saldoCredito -= pagamento + (pagamento * taxa);
			System.out.println("Pagamento realizado com sucesso");
		} else {
			System.out.println("Limite de Crédito excedido");
		}
	}
	
	public boolean pagarFatura(double valorPagamento) {
		if (valorPagamento <= 0 || valorPagamento > this.saldoMes) {
			return false;
		}

		this.saldoMes -= valorPagamento;
		this.saldoCredito += valorPagamento;

		return true;
	}


}
