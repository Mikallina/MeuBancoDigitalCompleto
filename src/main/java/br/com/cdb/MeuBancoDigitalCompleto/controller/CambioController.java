package br.com.cdb.MeuBancoDigitalCompleto.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.cdb.MeuBancoDigitalCompleto.service.CambioService;

@RestController
@RequestMapping("/api/cambio")
public class CambioController {

    @Autowired
    private CambioService cambioService;

    @GetMapping("/converter")
    public ResponseEntity<?> converter(
            @RequestParam double valor,
            @RequestParam String moedaBase,
            @RequestParam String moedaDestino) {
        try {
            double valorConvertido = cambioService.converterMoeda(valor, moedaBase, moedaDestino);
            return ResponseEntity.ok("Valor convertido: R$ " + valorConvertido);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
        }
    }
}