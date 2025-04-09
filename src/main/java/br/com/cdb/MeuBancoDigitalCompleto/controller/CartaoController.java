package br.com.cdb.MeuBancoDigitalCompleto.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.MeuBancoDigitalCompleto.dto.CompraCartaoDTO;
import br.com.cdb.MeuBancoDigitalCompleto.dto.FaturaDTO;
import br.com.cdb.MeuBancoDigitalCompleto.dto.LimiteDTO;
import br.com.cdb.MeuBancoDigitalCompleto.dto.StatusDTO;
import br.com.cdb.MeuBancoDigitalCompleto.entity.Cartao;
import br.com.cdb.MeuBancoDigitalCompleto.entity.CartaoCredito;
import br.com.cdb.MeuBancoDigitalCompleto.entity.CartaoDebito;
import br.com.cdb.MeuBancoDigitalCompleto.entity.Cliente;
import br.com.cdb.MeuBancoDigitalCompleto.entity.Conta;
import br.com.cdb.MeuBancoDigitalCompleto.entity.Fatura;
import br.com.cdb.MeuBancoDigitalCompleto.enuns.TipoCartao;
import br.com.cdb.MeuBancoDigitalCompleto.service.CartaoService;
import br.com.cdb.MeuBancoDigitalCompleto.service.ContaService;

@RestController
@RequestMapping("/cartao")
public class CartaoController {

	@Autowired
	private CartaoService cartaoService;

	@Autowired
	private ContaService contaService;

	@PostMapping("/emitir-cartao")
	public ResponseEntity<Cartao> emitirCartao(@RequestParam String contaC, @RequestParam TipoCartao tipoCartao,
			@RequestParam(required = false) String diaVencimento) {
		Conta conta = contaService.buscarContas(contaC);
		if (conta != null) {
			if (tipoCartao == TipoCartao.CREDITO && diaVencimento == null) {
				return ResponseEntity.badRequest().body(null);
			}
			Cartao cartao = cartaoService.criarCartao(conta, tipoCartao, 1234, diaVencimento);
			return ResponseEntity.ok(cartao);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@PutMapping("/alterar-senha/{idCartao}")
	public ResponseEntity<String> alterarSenha(@PathVariable Long idCartao, @RequestParam int senhaNova,
			@RequestParam int senhaAntiga) throws Exception {
		Optional<Cartao> cartaoOptional = Optional.ofNullable(cartaoService.buscarCartaoPorCliente(idCartao));

		if (cartaoOptional.isPresent()) {
			Cartao cartao = cartaoOptional.get();
			boolean sucesso = cartaoService.alterarSenha(senhaAntiga, senhaNova, cartao);
			if (sucesso) {
				return ResponseEntity.ok("Senha alterada com sucesso!");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Senha antiga incorreta!");
			}
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cartão não encontrado!");
		}
	}

	@GetMapping("/obter-dados/{idCartao}")
	public ResponseEntity<Cartao> buscarCartao(@PathVariable Long idCartao) {
		Cartao cartao = (Cartao) cartaoService.buscarCartaoPorCliente(idCartao);
		if (cartao == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(cartao);
	}

	@PutMapping("/alterar-status/{idCartao}")
	public ResponseEntity<String> alterarStatus(@PathVariable Long idCartao, @RequestParam boolean novoStatus) {
		try {
			Cartao cartao = cartaoService.alterarStatus(idCartao, novoStatus);
			return ResponseEntity.ok("Status do cartão alterado com sucesso!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao alterar status: " + e.getMessage());
		}
	}

	@PutMapping("/alterar-limite/{idCartao}")
	public ResponseEntity<String> alterarLimite(@PathVariable Long idCartao, @RequestBody LimiteDTO limiteDTO) throws Exception {
	    boolean limiteAlterado = cartaoService.alterarLimiteCartao(idCartao, limiteDTO.getNovoLimite());
	    
	    if (limiteAlterado) {
	        return ResponseEntity.ok("Limite atualizado com sucesso.");
	    } else {
	        return ResponseEntity.badRequest().body("Erro ao atualizar o limite do cartão.");
	    }
	}

	@PostMapping("/compra-cartao/{idCartao}")
	public ResponseEntity<String> realizarPagamento(@RequestBody CompraCartaoDTO compraCartaoDTO,
			@PathVariable Long idCartao) throws Exception {
		Cartao cartao = (Cartao) cartaoService.buscarCartaoPorCliente(idCartao);
		System.out.println("Cartao Encontrado" + idCartao);
		if (cartao == null) {
			return ResponseEntity.notFound().build();
		}

		boolean compraRealizado = cartaoService.realizarCompra(idCartao, compraCartaoDTO.getValor(),
				compraCartaoDTO.getDataCompra());
		if (!compraRealizado) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pagamento não autorizado.");
		}

		return ResponseEntity.ok("Pagamento realizado com sucesso.");
	}

	@GetMapping("/fatura/{idCartao}")
	public ResponseEntity<Fatura> consultarFatura(@PathVariable Long idCartao) {
		Cartao cartao = (Cartao) cartaoService.buscarCartaoPorCliente(idCartao);
		if (cartao == null || !(cartao instanceof CartaoCredito)) {
			return ResponseEntity.notFound().build();
		}

		CartaoCredito cartaoCredito = (CartaoCredito) cartao;
		Fatura fatura = cartaoCredito.gerarFatura();
		return ResponseEntity.ok(fatura);
	}

	@PostMapping("/fatura/pagamento/{idCartao}")
	public ResponseEntity<String> pagarFatura(@PathVariable Long idCartao,
			@RequestBody FaturaDTO pagamentoFaturaRequest) throws Exception {
		try {
			boolean pagamentoEfetuado = cartaoService.realizarPagamentoFatura(idCartao,
					pagamentoFaturaRequest.getValor());
			if (!pagamentoEfetuado) {
				return ResponseEntity.badRequest().body("Não foi possível realizar o pagamento da fatura.");
			}
			return ResponseEntity.ok("Pagamento da fatura realizado com sucesso.");
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build(); // Retorna 404 se o cartão não for encontrado ou se for inválido
		}
	}

	

}
