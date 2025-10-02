package br.com.trabalho.vendas_api.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    private Integer id;

    @Column(nullable = false, length = 255)
    private String nome;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Venda> vendas = new ArrayList<>();

    // Construtores
    public Cliente() {
    }

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

    // Métodos utilitários para gerenciar a relação bidirecional
    public void adicionarVenda(Venda venda) {
        vendas.add(venda);
        venda.setCliente(this);
    }

    public void removerVenda(Venda venda) {
        vendas.remove(venda);
        venda.setCliente(null);
    }

    // toString para facilitar o debug
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}