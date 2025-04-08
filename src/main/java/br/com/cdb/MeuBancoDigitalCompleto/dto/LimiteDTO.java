package br.com.cdb.MeuBancoDigitalCompleto.dto;

public class LimiteDTO {

	private Long id;
	private double limite;
	private double novoLimite;

	public LimiteDTO(Long id, double limite, double novoLimite) {
		super();
		this.id = id;
		this.limite = limite;
		this.novoLimite = novoLimite;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getLimite() {
		return limite;
	}

	public void setLimite(double limite) {
		this.limite = limite;
	}

	public double getNovoLimite() {
		return novoLimite;
	}

	public void setNovoLimite(double novoLimite) {
		this.novoLimite = novoLimite;
	}

}
