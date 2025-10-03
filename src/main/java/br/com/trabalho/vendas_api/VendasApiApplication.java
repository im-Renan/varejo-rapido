package br.com.trabalho.vendas_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Spring Boot de Vendas.
 * <p>
 * Responsável por iniciar a aplicação, configurando o contexto Spring
 * e inicializando todos os componentes necessários.
 */
@SpringBootApplication
public class VendasApiApplication {

	/**
	 * Método principal que inicia a aplicação Spring Boot.
	 *
	 * @param args argumentos de linha de comando (opcional)
	 */
	public static void main(String[] args) {
		SpringApplication.run(VendasApiApplication.class, args);
	}
}
