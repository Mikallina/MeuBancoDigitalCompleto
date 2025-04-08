package br.com.cdb.MeuBancoDigitalCompleto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cdb.MeuBancoDigitalCompleto.entity.Cliente;
import br.com.cdb.MeuBancoDigitalCompleto.entity.Conta;
import br.com.cdb.MeuBancoDigitalCompleto.enuns.TipoConta;


@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
	  Conta findByNumConta(String numConta);
	  Optional<Conta> findByClienteAndTipoConta(Cliente cliente, TipoConta tipoConta);
	  
	  List<Conta> findByCliente_IdCliente(Long clienteId);
	 // Conta findByCliente(Cliente cliente);
	  List<Conta> findByCliente(Cliente cliente);
	  
}
