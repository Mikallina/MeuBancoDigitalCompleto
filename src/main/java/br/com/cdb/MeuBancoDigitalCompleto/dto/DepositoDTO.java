 
package br.com.cdb.MeuBancoDigitalCompleto.dto;

public class DepositoDTO {
	
	private Long id;
    private double valor;
	public DepositoDTO(Long cpf, double valor) {
		super();
		this.id = id;
		this.valor = valor;
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
    
    

}
