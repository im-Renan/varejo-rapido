package br.com.trabalho.vendas_api.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um cliente no sistema de vendas.
 * <p>
 * Contém informações básicas como id e nome, além da lista de vendas realizadas pelo cliente.
 * A relação com a entidade Venda é bidirecional, onde um cliente pode ter várias vendas.
 */
@Entity
@Table(name = "clientes")
public class Cliente {

    /** Identificador único do cliente */
    @Id
    private Integer id;

    /** Nome do cliente */
    @Column(nullable = false, length = 255)
    private String nome;

    /** Lista de vendas associadas a este cliente */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Venda> vendas = new ArrayList<>();

    /**
     * Construtor vazio.
     */
    public Cliente() {
    }

    /**
     * Construtor com campos principais.
     *
     * @param id Identificador do cliente
     * @param nome Nome do cliente
     */
    public Cliente(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
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

    public List<Venda> getVendas() {
        return vendas;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }

    /**
     * Adiciona uma venda à lista de vendas do cliente e ajusta a relação bidirecional.
     *
     * @param venda Venda a ser adicionada
     */
    public void adicionarVenda(Venda venda) {
        vendas.add(venda);
        venda.setCliente(this);
    }

    /**
     * Remove uma venda da lista de vendas do cliente e ajusta a relação bidirecional.
     *
     * @param venda Venda a ser removida
     */
    public void removerVenda(Venda venda) {
        vendas.remove(venda);
        venda.setCliente(null);
    }

    /**
     * Representação em String do cliente para facilitar debug.
     *
     * @return String com id e nome do cliente
     */
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
