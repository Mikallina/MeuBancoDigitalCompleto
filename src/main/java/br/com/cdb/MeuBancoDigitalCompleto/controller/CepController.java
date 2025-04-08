package br.com.cdb.MeuBancoDigitalCompleto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.MeuBancoDigitalCompleto.entity.Endereco;
import br.com.cdb.MeuBancoDigitalCompleto.service.CepService;

@RestController
public class CepController {
	
	private final CepService cepService;

    public CepController(CepService cepService) {
        this.cepService = cepService;
    }

    @GetMapping("/buscar-endereco/{cep}")
    public Endereco buscarEndereco(@PathVariable String cep) {
        return cepService.buscarEnderecoPorCep(cep);
    }

}
