package br.com.trabalho.vendas_api.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um produto no sistema de vendas.
 * <p>
 * Contém informações básicas como id, nome e preço, além da lista de vendas associadas a este produto.
 * A relação com a entidade Venda é bidirecional, onde um produto pode estar em várias vendas.
 */
@Entity
@Table(name = "produtos")
public class Produto {
    /** Identificador único do produto */
    @Id
    private Integer id;

    /** Nome do produto */
    @Column(nullable = false, length = 255)
    private String nome;

    /** Preço unitário do produto */
    @Column(name = "preco", nullable = false)
    private Double preco;

    /** Lista de vendas associadas a este produto */
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Venda> vendas = new ArrayList<>();

    /**
     * Construtor vazio.
     */
    public Produto() {}

    /**
     * Construtor com campos principais.
     *
     * @param id Identificador do produto
     * @param nome Nome do produto
     * @param preco Preço unitário do produto
     */
    public Produto(Integer id, String nome, Double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public List<Venda> getVendas() {
        return vendas;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }

    /**
     * Adiciona uma venda à lista de vendas do produto e ajusta a relação bidirecional.
     *
     * @param venda Venda a ser adicionada
     */
    public void adicionarVenda(Venda venda) {
        vendas.add(venda);
        venda.setProduto(this);
    }

    /**
     * Remove uma venda da lista de vendas do produto e ajusta a relação bidirecional.
     *
     * @param venda Venda a ser removida
     */
    public void removerVenda(Venda venda) {
        vendas.remove(venda);
        venda.setProduto(null);
    }

    /**
     * Representação em String do produto para facilitar debug.
     *
     * @return String com id, nome e preço do produto
     */
    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                '}';
    }
}
