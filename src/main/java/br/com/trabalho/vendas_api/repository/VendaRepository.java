package br.com.trabalho.vendas_api.repository;

import br.com.trabalho.vendas_api.model.Cliente;
import br.com.trabalho.vendas_api.model.Produto;
import br.com.trabalho.vendas_api.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    // Método para buscar vendas por data, cliente e produto
    List<Venda> findByDataVendaAndClienteIdAndProdutoId(
            LocalDate dataVenda,
            Integer clienteId,
            Integer produtoId
    );

    // Método simplificado para verificar se venda já existe
    @Query("SELECT COUNT(v) > 0 FROM Venda v WHERE v.dataVenda = :dataVenda AND v.cliente = :cliente AND v.produto = :produto AND v.quantidade = :quantidade")
    boolean existsByDataVendaAndClienteAndProdutoAndQuantidade(
            @Param("dataVenda") LocalDate dataVenda,
            @Param("cliente") Cliente cliente,
            @Param("produto") Produto produto,
            @Param("quantidade") Integer quantidade);

    // Busca todas as vendas com produto e cliente carregados
    @Query("SELECT v FROM Venda v JOIN FETCH v.produto JOIN FETCH v.cliente ORDER BY v.dataVenda DESC, v.id DESC")
    List<Venda> findAllWithProdutoAndCliente();
}