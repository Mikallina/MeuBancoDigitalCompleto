package br.com.cdb.MeuBancoDigitalCompleto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cdb.MeuBancoDigitalCompleto.entity.Cartao;
import br.com.cdb.MeuBancoDigitalCompleto.entity.Conta;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {
    List<Cartao> findByConta(Conta conta);

	Cartao findByNumCartao(String numCartao);
}
