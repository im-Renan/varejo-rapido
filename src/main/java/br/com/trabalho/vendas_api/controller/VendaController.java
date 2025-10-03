package br.com.trabalho.vendas_api.controller;

import br.com.trabalho.vendas_api.dto.VendaResponseDTO;
import br.com.trabalho.vendas_api.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável por expor endpoints relacionados às vendas.
 * Permite consultar todas as vendas cadastradas no sistema.
 */
@RestController
@RequestMapping("/vendas")
@CrossOrigin(origins = "*")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    /**
     * Endpoint para obter todas as vendas.
     *
     * @return ResponseEntity contendo a lista de vendas ou uma mensagem de erro.
     *         - Retorna status 200 com lista de vendas em caso de sucesso.
     *         - Retorna status 200 com "[]" caso não existam vendas.
     *         - Retorna status 500 com mensagem de erro em caso de falha.
     */
    @GetMapping
    public ResponseEntity<?> getAllVendas() {
        try {
            List<VendaResponseDTO> vendas = vendaService.findAllVendas();

            if (vendas.isEmpty()) {
                return ResponseEntity.ok().body("[]");
            }

            return ResponseEntity.ok(vendas);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("{\"error\": \"Erro ao buscar vendas: " + e.getMessage() + "\"}");
        }
    }
}
