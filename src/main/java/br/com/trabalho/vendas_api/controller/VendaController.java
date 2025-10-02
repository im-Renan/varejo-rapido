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

    // ✅ ÚNICO endpoint obrigatório pelo desafio
    @GetMapping
    public ResponseEntity<List<VendaResponseDTO>> getAllVendas() {
        List<VendaResponseDTO> vendas = vendaService.findAllVendas();
        return ResponseEntity.ok(vendas);
    }
}