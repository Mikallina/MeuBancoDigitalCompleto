package br.com.cdb.MeuBancoDigitalCompleto.controller;

import java.util.List;

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

import br.com.cdb.MeuBancoDigitalCompleto.dto.DepositoDTO;
import br.com.cdb.MeuBancoDigitalCompleto.dto.TransferenciaDTO;
import br.com.cdb.MeuBancoDigitalCompleto.entity.Cliente;
import br.com.cdb.MeuBancoDigitalCompleto.entity.Conta;
import br.com.cdb.MeuBancoDigitalCompleto.enuns.TipoConta;
import br.com.cdb.MeuBancoDigitalCompleto.service.ClienteService;
import br.com.cdb.MeuBancoDigitalCompleto.service.ContaService;

@RestController
@RequestMapping("/conta")
public class ContaController {

	@Autowired
	private ContaService contaService;

	@Autowired
	private ClienteService clienteService;

	// Verificado
	@PostMapping("/criarConta")
	public ResponseEntity<Conta> criarConta(@RequestParam String cpf, @RequestParam int agencia,
			@RequestParam TipoConta tipoConta) {
		Cliente cliente = clienteService.buscarClientePorCpf(cpf);
		if (cliente != null) {
			Conta conta = contaService.criarConta(cliente, agencia, tipoConta);
			return ResponseEntity.ok(conta);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	// Verificado
	@GetMapping("/buscarConta/{cpf}")
	public ResponseEntity<List<Conta>> buscarContasPorCpf(@PathVariable String cpf) {
		Cliente cliente = clienteService.buscarClientePorCpf(cpf);
		if (cliente != null) {
			List<Conta> contas = contaService.buscarContasPorCliente(cliente);
			if (!contas.isEmpty()) {
				return ResponseEntity.ok(contas);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	// Verificado
	// Endpoint para efetuar depósito
	@PostMapping("/depositar")
	public ResponseEntity<String> depositar(@RequestBody DepositoDTO depositoDTO) {
		boolean sucesso = contaService.realizarDeposito(depositoDTO.getId(), depositoDTO.getValor());

		if (sucesso) {
			return ResponseEntity.ok("Depósito realizado com sucesso!");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao realizar depósito.");
		}
	}

	// Verificado
	@PostMapping("/efetuarPIX")
	public ResponseEntity<String> efetuarPIX(@RequestBody TransferenciaDTO transferenciaDTO) {
		try {
			boolean sucesso = contaService.realizarTransferenciaPIX(transferenciaDTO.getValor(),
					transferenciaDTO.getNumContaOrigem(), transferenciaDTO.getChave());
			if (sucesso) {
				return ResponseEntity.ok("PIX realizado com sucesso!");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao realizar PIX.");
			}
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PostMapping("/transferirPoupanca")
	public ResponseEntity<String> transferirPoupanca(@RequestBody TransferenciaDTO transferenciaDTO) {
		boolean sucesso = contaService.realizarTransferenciaPoupanca(transferenciaDTO.getValor(),
				transferenciaDTO.getNumContaOrigem(), transferenciaDTO.getNumContaDestino());
		if (sucesso) {
			return ResponseEntity.ok("Transferência para conta poupança realizada com sucesso!");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao transferir para conta poupança.");
		}
	}

	// Endpoint para transferir para outras contas
	@PostMapping("/transferirOutrasContas")
	public ResponseEntity<String> transferirOutrasContas(@RequestBody TransferenciaDTO transferenciaDTO) {
		boolean sucesso = contaService.realizarTransferenciaOutrasContas(transferenciaDTO.getValor(),
				transferenciaDTO.getNumContaDestino(), transferenciaDTO.getNumContaOrigem());
		if (sucesso) {
			return ResponseEntity.ok("Transferência realizada com sucesso!");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao transferir para outra conta.");
		}
	}

	@PutMapping("/{idConta}/manutencao")
	public ResponseEntity<String> aplicarTaxaManutencao(@PathVariable Long idConta, TipoConta tipoConta) {

		try {
			boolean sucesso = contaService.aplicarTaxaOuRendimento(idConta, tipoConta.CORRENTE, true);
			if (sucesso) {
				return ResponseEntity.ok("Taxa de Manutenção aplicada com sucesso");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao aplicar a taxa");
			}
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@PutMapping("/{idConta}/rendimentos")
	public ResponseEntity<String> aplicarRendimentos(@PathVariable Long idConta, TipoConta tipoConta) {
		try {
			boolean sucesso = contaService.aplicarTaxaOuRendimento(idConta, tipoConta.CORRENTE, false);
			if (sucesso) {
				return ResponseEntity.ok("Rendimento aplicado com sucesso");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao aplicar a taxa");
			}
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// Endpoint para exibir o saldo de uma conta
	@GetMapping("/exibirSaldo")
	public ResponseEntity<String> exibirSaldo(@RequestParam String cpf) {
		double saldo = contaService.obterSaldo(cpf);
		return ResponseEntity.ok("Saldo da conta: R$ " + saldo);
	}

}
