package br.com.cdb.MeuBancoDigitalCompleto.enuns;

import jakarta.persistence.Entity;

public enum Categoria {

	COMUM("Comum"),
	SUPER("Super"),
	PREMIUM("Premium");

	private final String descricao;



	public String getDescricao() {
		return descricao;
	}

	private Categoria(String descricao) {
		this.descricao = descricao;
	}
	

	public static Categoria fromCodigo(int descricao) {
	        for (Categoria categoria : Categoria.values()) {
	            if (categoria.descricao.equals(categoria)) {
	                return categoria;
	            }
	        }
	        throw new IllegalArgumentException("Código de categoria inválido: " + descricao);
	    }

	
	
}
