package br.com.trabalho.vendas_api.controller;

import br.com.trabalho.vendas_api.service.ArquivoProcessor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controlador REST responsável pelo upload e teste de arquivos.
 * Permite upload de arquivos .dat e métodos de diagnóstico para depuração.
 */
@RestController
@RequestMapping("/arquivo")
@CrossOrigin(origins = "*")
public class ArquivoController {

    @Autowired
    private ArquivoProcessor arquivoProcessor;

    /**
     * Endpoint para upload de arquivo .dat.
     *
     * @param file Arquivo enviado pelo cliente via multipart/form-data.
     * @return ResponseEntity com mensagem de sucesso ou erro.
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadArquivo(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("=== 📁 UPLOAD INICIADO ===");
            System.out.println("Nome do arquivo: " + file.getOriginalFilename());
            System.out.println("Tamanho: " + file.getSize() + " bytes");
            System.out.println("Content-Type: " + file.getContentType());

            if (file.isEmpty()) {
                System.out.println("❌ Arquivo vazio!");
                return ResponseEntity.badRequest().body("❌ Arquivo vazio!");
            }

            if (!file.getOriginalFilename().toLowerCase().endsWith(".dat")) {
                System.out.println("❌ Formato inválido: " + file.getOriginalFilename());
                return ResponseEntity.badRequest().body("❌ Formato inválido! Apenas arquivos .dat são aceitos.");
            }

            String resultado = arquivoProcessor.processarArquivoUpload(file);
            System.out.println("✅ " + resultado);
            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            System.err.println("❌ ERRO NO UPLOAD: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("❌ Erro interno: " + e.getMessage());
        }
    }

    /**
     * Endpoint de diagnóstico para testar se o arquivo chega corretamente.
     * Útil para depuração de problemas de envio via Postman ou front-end.
     *
     * @param file Arquivo enviado opcionalmente pelo cliente.
     * @return ResponseEntity com informações do arquivo ou mensagem de erro.
     */
    @PostMapping("/teste-simples")
    public ResponseEntity<String> testeSimples(@RequestParam(value = "file", required = false) MultipartFile file) {
        System.out.println("=== 🧪 TESTE DIAGNÓSTICO INICIADO ===");

        if (file == null) {
            System.out.println("❌ FILE É NULL - O parâmetro 'file' não está chegando!");
            System.out.println("💡 Provável problema: Key no Postman não é 'file'");
            return ResponseEntity.badRequest().body("❌ Erro: Parâmetro 'file' não encontrado. Verifique se no Postman a Key é 'file'");
        }

        System.out.println("✅ FILE NÃO É NULL - Parâmetro chegou!");
        System.out.println("📁 Nome: " + file.getOriginalFilename());
        System.out.println("📏 Tamanho: " + file.getSize());
        System.out.println("🔤 Content-Type: " + file.getContentType());
        System.out.println("📦 Está vazio? " + file.isEmpty());

        if (file.isEmpty()) {
            System.out.println("❌ FILE ESTÁ VAZIO");
            return ResponseEntity.badRequest().body("❌ Arquivo vazio");
        }

        if (!file.getOriginalFilename().toLowerCase().endsWith(".dat")) {
            System.out.println("❌ NÃO É ARQUIVO .dat: " + file.getOriginalFilename());
            return ResponseEntity.badRequest().body("❌ Apenas arquivos .dat são aceitos");
        }

        System.out.println("🎯 TESTE CONCLUÍDO COM SUCESSO!");
        return ResponseEntity.ok("✅ Teste OK! Arquivo: " + file.getOriginalFilename() + " (" + file.getSize() + " bytes)");
    }

    /**
     * Endpoint alternativo de depuração para analisar a request completa.
     * Mostra método, headers e verifica se é multipart/form-data.
     *
     * @param request HttpServletRequest recebido pelo Spring.
     * @return ResponseEntity com informações da request ou mensagem de erro.
     */
    @PostMapping("/debug-request")
    public ResponseEntity<String> debugRequest(HttpServletRequest request) {
        System.out.println("=== 🔍 ANALISANDO REQUEST ===");
        System.out.println("Method: " + request.getMethod());
        System.out.println("Content-Type: " + request.getContentType());
        System.out.println("Content-Length: " + request.getContentLength());
        System.out.println("Headers:");
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            System.out.println("  " + headerName + ": " + request.getHeader(headerName));
        });

        if (request.getContentType() == null || !request.getContentType().startsWith("multipart/form-data")) {
            System.out.println("❌ NÃO É MULTIPART/FORM-DATA!");
            System.out.println("💡 Content-Type atual: " + request.getContentType());
            return ResponseEntity.badRequest().body("❌ Content-Type deve ser multipart/form-data");
        }

        System.out.println("✅ É MULTIPART/FORM-DATA!");
        return ResponseEntity.ok("✅ Request analisado - Tudo OK com o Content-Type");
    }
}
