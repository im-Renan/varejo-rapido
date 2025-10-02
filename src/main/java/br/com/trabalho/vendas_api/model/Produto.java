package br.com.trabalho.vendas_api.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produtos")
public class Produto {
    @Id
    private Integer id;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(name = "preco", nullable = false) // Mudei o nome da coluna para "preco"
    private Double preco; // Mude o nome do campo para "preco"

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Venda> vendas = new ArrayList<>();

    // Construtores
    public Produto() {}

    public Produto(Integer id, String nome, Double preco) { // Atualize o construtor
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

    public Double getPreco() { // Mude para getPreco
        return preco;
    }

    public void setPreco(Double preco) { // Mude para setPreco
        this.preco = preco;
    }

    public List<Venda> getVendas() {
        return vendas;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }

    // Métodos utilitários
    public void adicionarVenda(Venda venda) {
        vendas.add(venda);
        venda.setProduto(this);
    }

    public void removerVenda(Venda venda) {
        vendas.remove(venda);
        venda.setProduto(null);
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                '}';
    }
}