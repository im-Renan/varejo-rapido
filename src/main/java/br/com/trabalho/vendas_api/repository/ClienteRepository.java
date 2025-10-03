package br.com.trabalho.vendas_api.repository;

import br.com.trabalho.vendas_api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório para operações de persistência da entidade Cliente.
 * <p>
 * Estende JpaRepository fornecendo métodos CRUD prontos para uso
 * e possibilidade de criação de queries personalizadas.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
