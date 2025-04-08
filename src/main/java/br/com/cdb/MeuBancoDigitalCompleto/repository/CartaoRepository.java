package br.com.cdb.MeuBancoDigitalCompleto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cdb.MeuBancoDigitalCompleto.entity.Cartao;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {
	//Cartao findByClienteCpf(String cpfCliente);

	List<Cartao> findByConta_IdConta(Long idConta);
}
