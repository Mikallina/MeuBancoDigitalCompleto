package br.com.cdb.MeuBancoDigitalCompleto.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CompraCartaoDTO { 
	
	private Long id;
	private double valor;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataCompra;
	public CompraCartaoDTO(Long id, double valor, LocalDate dataCompra) {
		super();
		this.id = id;
		this.valor = valor;
		this.dataCompra = dataCompra;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public LocalDate getDataCompra() {
		return dataCompra;
	}
	public void setDataCompra(LocalDate dataCompra) {
		this.dataCompra = dataCompra;
	}
	

}
