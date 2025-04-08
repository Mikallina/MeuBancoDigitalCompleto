package br.com.cdb.MeuBancoDigitalCompleto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cdb.MeuBancoDigitalCompleto.entity.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, String> {
	Login findByEmail(String email);
	boolean existsByEmail(String email);

}
