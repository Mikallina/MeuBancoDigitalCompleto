package br.com.cdb.MeuBancoDigitalCompleto.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.logging.log4j.message.StructuredDataMessage.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.cdb.MeuBancoDigitalCompleto.entity.Cartao;
import br.com.cdb.MeuBancoDigitalCompleto.entity.CartaoCredito;
import br.com.cdb.MeuBancoDigitalCompleto.entity.CartaoDebito;
import br.com.cdb.MeuBancoDigitalCompleto.entity.Cliente;
import br.com.cdb.MeuBancoDigitalCompleto.entity.Conta;
import br.com.cdb.MeuBancoDigitalCompleto.enuns.Categoria;
import br.com.cdb.MeuBancoDigitalCompleto.enuns.TipoCartao;
import br.com.cdb.MeuBancoDigitalCompleto.repository.CartaoRepository;
import br.com.cdb.MeuBancoDigitalCompleto.repository.ContaRepository;

@Service
public class CartaoService {

	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Autowired
	private ContaRepository contaRepository;

	public void salvarCartao(Cartao cartao, boolean isAtualizar) throws Exception {
		if (cartao == null || cartao.getNumCartao() == null) {
			throw new IllegalArgumentException("Erro: Tentativa de Salvar o Cartão nulo");
		}
		if (isAtualizar) {

			cartaoRepository.save(cartao);
			System.out.println("Cartão atualizado: " + cartao.getNumCartao());
		} else {
			cartaoRepository.save(cartao);
			System.out.println("Novo cartão adicionado: " + cartao.getNumCartao());
		}
	}

	public Cartao criarCartao(Conta conta, TipoCartao tipoCartao, int senha, String diaVencimento) {
		if (conta == null) {
			throw new IllegalArgumentException("Erro: Cliente não pode ser null.");
		}
		LocalDate dataVencimento = null;
		if (tipoCartao == TipoCartao.CREDITO) {
			if (diaVencimento == null) {
				throw new IllegalArgumentException("Erro: O dia de vencimento é obrigatório para cartões de crédito.");
			}
			int dia = Integer.parseInt(diaVencimento);
			LocalDate dataAtual = LocalDate.now();
			int anoAtual = dataAtual.getYear();
			int mesAtual = dataAtual.getMonthValue();

			try {
				dataVencimento = LocalDate.of(anoAtual, mesAtual, dia);
			} catch (Exception e) {
				System.out.println("Data inválida" + e.getMessage());
			}

			if (dataVencimento.isBefore(dataAtual)) {
				if (mesAtual == 12) {
					dataVencimento = LocalDate.of(anoAtual + 1, 1, dia);
				} else {
					dataVencimento = LocalDate.of(anoAtual, mesAtual + 1, dia);
				}
			}
		}

		Cartao cartao;
		String numCartao = gerarNumeroCartao(tipoCartao);

		if (tipoCartao == TipoCartao.CREDITO) {
			cartao = new CartaoCredito(conta, senha, numCartao, TipoCartao.CREDITO, limiteDeCredito(conta),
					diaVencimento, dataVencimento);
		} else if (tipoCartao == TipoCartao.DEBITO) {
			cartao = new CartaoDebito(conta, numCartao, TipoCartao.DEBITO, senha, limiteDiario(conta));
		} else {
			throw new IllegalArgumentException("Tipo de cartão inválido.");
		}

		cartaoRepository.save(cartao);

		return cartao;
	}

	public boolean alterarSenha(int senhaAntiga, int senhaNova, Cartao cartao) {
		if (!cartao.isStatus()) {
			throw new IllegalArgumentException("Status do Cartão Desativado");
		}
		if (cartao.getSenha() == senhaAntiga) {
			cartao.setSenha(senhaNova);
			System.out.println("Senha atualizada com sucesso.");
			cartaoRepository.save(cartao);
			return true;
		} else {
			System.out.println("Senha antiga incorreta.");
			return false;
		}
	}

	public boolean alterarLimiteCartao(String numCartao, double novoLimite) throws Exception {
		Optional<Cartao> cartaoOptional = Optional.ofNullable(cartaoRepository.findByNumCartao(numCartao));

		if (cartaoOptional.isPresent()) {
			Cartao cartao = cartaoOptional.get();
			System.out.println("Cartão Encontrado: " + cartao.getNumCartao());

			if (!cartao.isStatus()) {
				throw new IllegalArgumentException("Status do Cartão Desativado");
			}

			if (cartao instanceof CartaoCredito) {
				CartaoCredito cartaoCredito = (CartaoCredito) cartao;
				cartaoCredito.alterarLimiteCredito(novoLimite);
				cartaoRepository.save(cartaoCredito);
				System.out.println("Limite de crédito atualizado para: R$ " + novoLimite);
				return true;
			} else if (cartao instanceof CartaoDebito) {
				CartaoDebito cartaoDebito = (CartaoDebito) cartao;
				cartaoDebito.alterarLimiteDebito(novoLimite);
				cartaoRepository.save(cartaoDebito);
				System.out.println("Limite de débito atualizado para: R$ " + novoLimite);
				return true;
			} else {
				System.out.println("O cartão não é de crédito nem de débito.");
			}
		} else {
			System.out.println("Cartão não encontrado.");
		}
		return false;
	}

	public boolean alterarStatus(String numCartao, boolean novoStatus) {
		Optional<Cartao> cartaoOptional = Optional.ofNullable(cartaoRepository.findByNumCartao(numCartao));
		if (cartaoOptional.isPresent()) {
			Cartao cartao = cartaoOptional.get();
			cartao.setStatus(novoStatus);
			cartaoRepository.save(cartao);
			System.out.println("Status do cartão de número " + cartao.getNumCartao() + " alterado para " + novoStatus);
			return true;
		} else {
			throw new IllegalArgumentException("Cartão não encontrado com o ID fornecido.");
		}
	}

	private double limiteDeCredito(Conta conta) {
		Categoria categoria = conta.getCliente().getCategoria();
		switch (categoria) {

		case COMUM: {
			return 1000.00;
		}
		case SUPER: {
			return 5000.00;
		}
		case PREMIUM: {
			return 10000.00;
		}
		default:
			throw new IllegalArgumentException("Categoria de Cliente desconhecida" + conta.getCliente().getCategoria());
		}
	}

	private double limiteDiario(Conta conta) {
		Categoria categoria = conta.getCliente().getCategoria();
		switch (categoria) {
		case COMUM: {
			return 500.00;
		}
		case SUPER: {
			return 1000.00;
		}
		case PREMIUM: {
			return 5000.00;
		}
		default:
			throw new IllegalArgumentException("Categoria de Cliente desconhecida" + conta.getCliente().getCategoria());
		}
	}

	private double calcularTaxaUtilizacao(CartaoCredito credito, Conta conta) {

		double limite = limiteDeCredito(conta);
		double saldoDevido = credito.getSaldoMes();

		if (saldoDevido > 0.8 * limite) {
			double taxa = saldoDevido * 0.05;
			return taxa;
		} else {
			return 0;
		}
	}


	public String gerarNumeroCartao(TipoCartao tipoCartao) {

		String numeroParcial = gerarNumeroAleatorio(15);
		int digitoVerificador = calcularDigitoLuhn(numeroParcial);
		String numeroCartao = numeroParcial + digitoVerificador;
		return numeroCartao;
	}

	private String gerarNumeroAleatorio(int tamanho) {
		Random random = new Random();
		StringBuilder numero = new StringBuilder();

		for (int i = 0; i < tamanho; i++) {
			numero.append(random.nextInt(10));
		}

		return numero.toString();
	}

	private int calcularDigitoLuhn(String numeroParcial) {
		int soma = 0;
		boolean alternar = false;
		for (int i = numeroParcial.length() - 1; i >= 0; i--) {
			int digito = Integer.parseInt(String.valueOf(numeroParcial.charAt(i)));

			if (alternar) {
				digito *= 2;
				if (digito > 9) {
					digito -= 9;
				}
			}

			soma += digito;
			alternar = !alternar;
		}

		// Calcular o dígito verificador
		int digitoVerificador = (10 - (soma % 10)) % 10;
		return digitoVerificador;
	}

	public boolean realizarCompra(String numCartao, double valor, LocalDate dataCompra) throws Exception {
		if (valor <= 0) {
			System.out.println("O valor do pagamento deve ser positivo.");
			return false;
		}
		Cartao cartao = cartaoRepository.findByNumCartao(numCartao);
		
		if (cartao == null) {
			throw new RuntimeException("Cartão Não encontrado");
		}
		if (!cartao.isStatus()) {
			throw new IllegalArgumentException("Status do Cartão Desativado");
		}

		if (cartao instanceof CartaoCredito) {
			CartaoCredito cartaoCredito = (CartaoCredito) cartao;

			if (cartaoCredito.getLimiteCredito() > valor) {
				cartaoCredito.setSaldoMes(cartaoCredito.getSaldoMes() + valor);
				cartaoCredito.setSaldoCredito(cartaoCredito.getLimiteCredito() - valor);
				cartaoCredito.setPagamento(cartaoCredito.getPagamento() + valor);

				cartaoCredito.setDataCompra(dataCompra);
				salvarCartao(cartaoCredito, true);

				System.out.println("Pagamento realizado com sucesso.");
				return true;
			} else {
				System.out.println("Limite de crédito excedido.");
				return false;
			}
		} else {
			System.out.println("Este cartão não é de crédito.");
			return false;
		}
	}

	public boolean realizarPagamentoFatura(String numCartao, double valorPagamento) throws Exception {
	    Cartao cartao = cartaoRepository.findByNumCartao(numCartao);

	    if (cartao == null || !(cartao instanceof CartaoCredito)) {
	        throw new RuntimeException("Cartão de crédito não encontrado ou tipo de cartão inválido.");
	    }

	    if (!cartao.isStatus()) {
	        throw new IllegalArgumentException("Status do Cartão Desativado");
	    }

	    CartaoCredito cartaoCredito = (CartaoCredito) cartao;
	    Conta conta = cartaoCredito.getConta();

	    if (conta == null) {
	        throw new RuntimeException("Conta associada ao cartão não encontrada.");
	    }

	    if (valorPagamento <= 0 || valorPagamento > cartaoCredito.getSaldoMes()) {
	        throw new RuntimeException("Valor do pagamento inválido.");
	    }

	    if (conta.getSaldo() < valorPagamento) {
	        throw new RuntimeException("Saldo insuficiente na conta para pagar a fatura.");
	    }

	    // Debita da conta corrente
	    conta.setSaldo(conta.getSaldo() - valorPagamento);

	    // Realiza o pagamento da fatura no cartão
	    boolean pagamentoEfetuado = cartaoCredito.pagarFatura(valorPagamento);

	    if (!pagamentoEfetuado) {
	        throw new RuntimeException("Não foi possível realizar o pagamento da fatura.");
	    }

	    // Salva alterações no cartão e conta
	    salvarCartao(cartaoCredito, true);
	    contaRepository.save(conta);

	    return true;
	}

	public Cartao buscarCartaoPorCliente(String numCartao) {
		Optional<Cartao> cartoes = Optional.ofNullable(cartaoRepository.findByNumCartao(numCartao));
		if (cartoes != null && !cartoes.isEmpty()) {
			return cartoes.get();
		}
		return null;
	}
	
	public List<Cartao> buscarCartaoPorConta(Conta conta) {
	    return cartaoRepository.findByConta(conta);
	}

	public Cartao buscarCartaoPorNumero(String numCartao) {
		
		return cartaoRepository.findByNumCartao(numCartao);
	}
	public double consultarFatura(String numCartao) {
	    Cartao cartao = cartaoRepository.findByNumCartao(numCartao);
	    
	    if (cartao == null) {
	        throw new IllegalArgumentException("Cartão não encontrado");
	    }

	    if (cartao instanceof CartaoCredito) {

	        CartaoCredito cartaoCredito = (CartaoCredito) cartao;

	        return cartaoCredito.getSaldoMes();
	    } else {

	        throw new IllegalArgumentException("Cartão não é de crédito");
	    }
	}
	
}
