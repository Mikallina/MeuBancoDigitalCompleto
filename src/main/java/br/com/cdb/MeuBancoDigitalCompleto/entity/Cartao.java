package br.com.cdb.MeuBancoDigitalCompleto.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import br.com.cdb.MeuBancoDigitalCompleto.enuns.TipoCartao;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)

public abstract class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCartao;

    private String numCartao;

    @ManyToOne
    @JoinColumn(name = "id_conta", referencedColumnName = "idConta")  
    @JsonBackReference
    private Conta conta;

    private boolean status;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cartao")
    protected TipoCartao tipoCartao;

    private int senha;
    
    private double fatura;
    
    

	public Cartao() {

	}

	
	public Cartao(String numCartao, Conta conta, TipoCartao tipoCartao, int senha, boolean status) {
		super();
		this.numCartao = numCartao;
		this.conta = conta;
		this.tipoCartao = tipoCartao;
		this.senha = senha;
		this.status = status;
	}
	
	
	
	public double getFatura() {
		return fatura;
	}


	public void setFatura(double fatura) {
		this.fatura = fatura;
	}


	public Conta getConta() {
		return conta;
	}
	public void setConta(Conta conta) {
		this.conta = conta;
	}
	public TipoCartao getTipoCartao() {
		return tipoCartao;
	}
	public void setTipoCartao(TipoCartao tipoCartao) {
		this.tipoCartao = tipoCartao;
	}
	public Long getIdCartao() {
		return idCartao;
	}

	public void setIdCartao(Long idCartao) {
		this.idCartao = idCartao;
	}

	public int getSenha() {
		return senha;
	}

	public void setSenha(int senha) {
		this.senha = senha;
	}

	public String getNumCartao() {
		return numCartao;
	}

	public void setNumCartao(String numCartao) {
		this.numCartao = numCartao;
	}


	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void ativarCartao() {
		this.status = true;
	}

	public void desativarCartao() {
		this.status = false;
	}

	public boolean verificarStatus() {
		return this.status;
	}



	
}
