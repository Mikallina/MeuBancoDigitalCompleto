package br.com.cdb.MeuBancoDigitalCompleto.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class CambioService {
	private final RestTemplate restTemplate;

    @Value("${api.cambio.url}")
    private String apiUrl;

    @Value("${api.cambio.key}")
    private String apiKey;

    public CambioService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public double obterCotacao(String moedaBase, String moedaDestino) throws Exception {
        try {

        	String url = String.format("%s/%s/latest/%s", apiUrl, apiKey, moedaBase);
        
        	System.out.println("URL gerada: " + url);

            // Requisição para obter a resposta da API
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, null, Map.class);

            // Obtenha o corpo da resposta
            Map<String, Object> responseBody = response.getBody();

            // Verifique se a resposta contém a chave "conversion_rates"
            if (responseBody == null || !responseBody.containsKey("conversion_rates")) {
                throw new Exception("A resposta da API não contém a chave 'conversion_rates'.");
            }

            // Acessar o mapa de taxas de câmbio (conversion_rates)
            Map<String, Double> conversionRates = (Map<String, Double>) responseBody.get("conversion_rates");

            // Verifique se a moeda de destino existe no mapa de taxas
            Double cotacao = conversionRates.get(moedaDestino);

            if (cotacao == null) {
                throw new Exception("Moeda de destino não encontrada.");
            }

            return cotacao;

        } catch (Exception e) {
            System.out.println("Erro ao buscar cotação: " + e.getMessage());
            throw new Exception("Erro ao buscar cotação: " + e.getMessage());
        }
    }

    // Método para converter o valor
    public double converterMoeda(double valor, String moedaBase, String moedaDestino) throws Exception {
        double cotacao = obterCotacao(moedaBase, moedaDestino);
        return valor * cotacao;
    }

}
