package br.com.trabalho.vendas_api.controller;

import br.com.trabalho.vendas_api.service.ArquivoProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/arquivo")
@CrossOrigin(origins = "*")
public class ArquivoController {

    @Autowired
    private ArquivoProcessor arquivoProcessor;

    @PostMapping("/processar")
    public String processarArquivo() {
        try {
            arquivoProcessor.processarArquivo("D:\\RENAN\\RENAN\\vendas_dia_2025-09-29.dat");
            return "✅ Arquivo processado com sucesso!";
        } catch (Exception e) {
            return "❌ Erro: " + e.getMessage();
        }
    }
}