package br.com.cdb.MeuBancoDigitalCompleto.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.cdb.MeuBancoDigitalCompleto.entity.Endereco;

@Service
public class CepService {

    private final RestTemplate restTemplate;

    public CepService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Endereco buscarEnderecoPorCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        return restTemplate.getForObject(url, Endereco.class);
    }
}
