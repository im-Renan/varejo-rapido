package br.com.trabalho.vendas_api.controller;

import br.com.trabalho.vendas_api.dto.VendaResponseDTO;
import br.com.trabalho.vendas_api.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendas")
@CrossOrigin(origins = "*")
public class VendaController {

    @Autowired
    private VendaService vendaService;

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