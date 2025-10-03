package br.com.trabalho.vendas_api.dto;

import java.time.LocalDate;

/**
 * DTO de resposta que representa os dados de uma venda no sistema.
 * <p>
 * Contém informações sobre a venda em si, como id, data, quantidade e valor total,
 * além de referências para os dados do produto e do cliente envolvidos na venda.
 */
public class VendaResponseDTO {
    /** Identificador único da venda */
    private Long idVenda;

    /** Data em que a venda foi realizada */
    private LocalDate dataVenda;

    /** Quantidade de itens vendidos */
    private Integer quantidade;

    /** Dados do produto vendido */
    private ProdutoDTO produto;

    /** Dados do cliente que realizou a compra */
    private ClienteDTO cliente;

    /** Valor total da venda */
    private Double valorTotalVenda;

    /**
     * Construtor vazio.
     */
    public VendaResponseDTO() {}

    /**
     * Construtor completo.
     *
     * @param idVenda Identificador da venda
     * @param dataVenda Data da venda
     * @param quantidade Quantidade de produtos vendidos
     * @param produto Dados do produto vendido
     * @param cliente Dados do cliente
     * @param valorTotalVenda Valor total da venda
     */
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

    /**
     * DTO interno que representa os dados de um produto na venda.
     */
    public static class ProdutoDTO {
        /** Identificador do produto */
        private Integer id;

        /** Nome do produto */
        private String nome;

        /** Valor unitário do produto */
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

    /**
     * DTO interno que representa os dados de um cliente na venda.
     */
    public static class ClienteDTO {
        /** Identificador do cliente */
        private Integer id;

        /** Nome do cliente */
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
