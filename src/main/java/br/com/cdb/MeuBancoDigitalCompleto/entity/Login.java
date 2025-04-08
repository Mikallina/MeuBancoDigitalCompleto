package br.com.cdb.MeuBancoDigitalCompleto.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Login {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String email;
	private String senhaAdm;
	
	
	public Login() {}

	public Login(String email, String senhaAdm, int id) {
		super();
		this.email = email;
		this.senhaAdm = senhaAdm;
		this.id = id;
	}
	
	public int id() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenhaAdm() {
		return senhaAdm;
	}

	public void setSenhaAdm(String senhaAdm) {
		this.senhaAdm = senhaAdm;
	}

	public Login(String email, String senhaAdm) {
		super();
		this.email = email;
		this.senhaAdm = senhaAdm;
	}

}
