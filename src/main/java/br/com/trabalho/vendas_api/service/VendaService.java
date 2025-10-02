package br.com.trabalho.vendas_api.service;

import br.com.trabalho.vendas_api.dto.VendaResponseDTO;
import br.com.trabalho.vendas_api.model.Venda;
import br.com.trabalho.vendas_api.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    public List<VendaResponseDTO> findAllVendas() {
        List<Venda> vendas = vendaRepository.findAllWithProdutoAndCliente();

        return vendas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private VendaResponseDTO convertToDTO(Venda venda) {
        VendaResponseDTO.ProdutoDTO produtoDTO = new VendaResponseDTO.ProdutoDTO(
                venda.getProduto().getId(),
                venda.getProduto().getNome(),
                venda.getProduto().getPreco()
        );

        VendaResponseDTO.ClienteDTO clienteDTO = new VendaResponseDTO.ClienteDTO(
                venda.getCliente().getId(),
                venda.getCliente().getNome()
        );

        Double valorTotal = venda.getQuantidade() * venda.getProduto().getPreco();

        return new VendaResponseDTO(
                venda.getId(),
                venda.getDataVenda(),
                venda.getQuantidade(),
                produtoDTO,
                clienteDTO,
                valorTotal
        );
    }
}