package br.com.cdb.MeuBancoDigitalCompleto.enuns;

import jakarta.persistence.Entity;

public enum Categoria {

	COMUM(1, "Comum"),
	SUPER(2, "Super"),
	PREMIUM(3, "Premium");
	
	private final Integer codigo;
	private final String descricao;

	Categoria(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}


	public Integer getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	 public static Categoria fromCodigo(int codigo) {
	        for (Categoria categoria : Categoria.values()) {
	            if (categoria.getCodigo() == codigo) {
	                return categoria;
	            }
	        }
	        throw new IllegalArgumentException("Código de categoria inválido: " + codigo);
	    }
	
	
	
}
