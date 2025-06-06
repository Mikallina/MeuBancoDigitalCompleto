package br.com.cdb.MeuBancoDigitalCompleto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cdb.MeuBancoDigitalCompleto.entity.Cliente;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
	Cliente findByCpf(String cpf);

}
