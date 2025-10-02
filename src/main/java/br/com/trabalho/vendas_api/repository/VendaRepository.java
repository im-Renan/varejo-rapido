package br.com.trabalho.vendas_api.repository;

import br.com.trabalho.vendas_api.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Query("SELECT v FROM Venda v JOIN FETCH v.produto JOIN FETCH v.cliente ORDER BY v.dataVenda DESC")
    List<Venda> findAllWithProdutoAndCliente();
}