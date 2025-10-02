package br.com.trabalho.vendas_api.dto;

import java.time.LocalDate;

public class VendaResponseDTO {
    private Long idVenda;
    private LocalDate dataVenda;
    private Integer quantidade;
    private ProdutoDTO produto;
    private ClienteDTO cliente;
    private Double valorTotalVenda;

    // Construtores
    public VendaResponseDTO() {}

    public VendaResponseDTO(Long idVenda, LocalDate dataVenda, Integer quantidade,
                            ProdutoDTO produto, ClienteDTO cliente, Double valorTotalVenda) {
        this.idVenda = idVenda;
        this.dataVenda = dataVenda;
        this.quantidade = quantidade;
        this.produto = produto;
        this.cliente = cliente;
        this.valorTotalVenda = valorTotalVenda;
    }

    // Getters e Setters
    public Long getIdVenda() { return idVenda; }
    public void setIdVenda(Long idVenda) { this.idVenda = idVenda; }

    public LocalDate getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDate dataVenda) { this.dataVenda = dataVenda; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public ProdutoDTO getProduto() { return produto; }
    public void setProduto(ProdutoDTO produto) { this.produto = produto; }

    public ClienteDTO getCliente() { return cliente; }
    public void setCliente(ClienteDTO cliente) { this.cliente = cliente; }

    public Double getValorTotalVenda() { return valorTotalVenda; }
    public void setValorTotalVenda(Double valorTotalVenda) { this.valorTotalVenda = valorTotalVenda; }

    // DTOs internos
    public static class ProdutoDTO {
        private Integer id;
        private String nome;
        private Double valorUnitario;

        public ProdutoDTO() {}
        public ProdutoDTO(Integer id, String nome, Double valorUnitario) {
            this.id = id;
            this.nome = nome;
            this.valorUnitario = valorUnitario;
        }

        // Getters e Setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public Double getValorUnitario() { return valorUnitario; }
        public void setValorUnitario(Double valorUnitario) { this.valorUnitario = valorUnitario; }
    }

    public static class ClienteDTO {
        private Integer id;
        private String nome;

        public ClienteDTO() {}
        public ClienteDTO(Integer id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        // Getters e Setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
    }
}