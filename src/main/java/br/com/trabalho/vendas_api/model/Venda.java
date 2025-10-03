package br.com.trabalho.vendas_api.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Representa uma venda no sistema.
 * <p>
 * Contém informações sobre o produto vendido, o cliente que realizou a compra,
 * a quantidade, o valor total da venda e a data da venda.
 */
@Entity
@Table(name = "vendas")
public class Venda {
    /** Identificador único da venda */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Produto associado à venda */
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    /** Cliente que realizou a compra */
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    /** Quantidade de produtos vendidos */
    @Column(nullable = false)
    private Integer quantidade;

    /** Valor total da venda */
    @Column(name = "valor_total", nullable = false)
    private Double valorTotal;

    /** Data em que a venda foi realizada */
    @Column(name = "data_venda", nullable = false)
    private LocalDate dataVenda;

    /**
     * Construtor vazio.
     */
    public Venda() {
    }

    /**
     * Construtor com campos principais.
     *
     * @param produto Produto vendido
     * @param cliente Cliente que realizou a compra
     * @param quantidade Quantidade de produtos vendidos
     * @param valorTotal Valor total da venda
     * @param dataVenda Data da venda
     */
    public Venda(Produto produto, Cliente cliente, Integer quantidade, Double valorTotal, LocalDate dataVenda) {
        this.produto = produto;
        this.cliente = cliente;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
        this.dataVenda = dataVenda;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }
}
