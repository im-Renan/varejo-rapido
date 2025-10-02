package br.com.trabalho.vendas_api.service;

import br.com.trabalho.vendas_api.model.Cliente;
import br.com.trabalho.vendas_api.model.Produto;
import br.com.trabalho.vendas_api.model.Venda;
import br.com.trabalho.vendas_api.repository.ClienteRepository;
import br.com.trabalho.vendas_api.repository.ProdutoRepository;
import br.com.trabalho.vendas_api.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ArquivoProcessor {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private VendaRepository vendaRepository;

    public void processarArquivo(String caminhoArquivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                processarLinha(linha);
            }

            System.out.println("✅ Arquivo processado com sucesso!");

        } catch (IOException e) {
            System.err.println("❌ Erro ao processar arquivo: " + e.getMessage());
        }
    }

    public void processarLinha(String linha) {
        if (linha == null || linha.trim().isEmpty()) {
            return;
        }

        try {
            Map<String, Object> campos = extrairCampos(linha);

            if (campos == null) {
                System.out.println("❌ Formato inválido na linha: " + linha);
                return;
            }

            processarCampos(campos);

        } catch (Exception e) {
            System.out.println("❌ Erro ao processar linha: " + linha);
            System.out.println("Mensagem: " + e.getMessage());
        }
    }

    private Map<String, Object> extrairCampos(String linha) {
        Map<String, Object> campos = new HashMap<>();

        try {
            System.out.println("📋 Processando linha: '" + linha + "'");
            System.out.println("📏 Tamanho da linha: " + linha.length());

            // CORREÇÃO DAS POSIÇÕES BASEADA NOS LOGS
            // As linhas têm 135 caracteres, vamos ajustar as posições

            // Código Produto (0-4)
            String codigoProdutoStr = linha.substring(0, 4).trim();
            System.out.println("🔹 Código Produto (0-4): '" + codigoProdutoStr + "'");
            campos.put("codigoProduto", Integer.parseInt(codigoProdutoStr));

            // Nome Produto (4-54) - 50 caracteres
            String nomeProduto = linha.substring(4, 54).trim();
            System.out.println("🔹 Nome Produto (4-54): '" + nomeProduto + "'");
            campos.put("nomeProduto", nomeProduto);

            // Código Vendedor (54-58) - CORRIGIDO: parece que está mais à direita
            // Vamos procurar o próximo grupo de 4 dígitos após o nome do produto
            int posCodigoVendedor = encontrarProximos4Digitos(linha, 54);
            String codigoVendedorStr = linha.substring(posCodigoVendedor, posCodigoVendedor + 4).trim();
            System.out.println("🔹 Código Vendedor (" + posCodigoVendedor + "-" + (posCodigoVendedor+4) + "): '" + codigoVendedorStr + "'");
            campos.put("codigoVendedor", Integer.parseInt(codigoVendedorStr));

            // Nome Vendedor (58-108) - após o código do vendedor
            String nomeVendedor = linha.substring(posCodigoVendedor + 4, posCodigoVendedor + 44).trim();
            System.out.println("🔹 Nome Vendedor (" + (posCodigoVendedor+4) + "-" + (posCodigoVendedor+44) + "): '" + nomeVendedor + "'");
            campos.put("nomeVendedor", nomeVendedor);

            // Quantidade - vamos procurar o próximo grupo de 3 dígitos
            int posQuantidade = encontrarProximos3Digitos(linha, posCodigoVendedor + 44);
            String quantidadeStr = linha.substring(posQuantidade, posQuantidade + 3).trim();
            System.out.println("🔹 Quantidade (" + posQuantidade + "-" + (posQuantidade+3) + "): '" + quantidadeStr + "'");
            campos.put("quantidade", Integer.parseInt(quantidadeStr));

            // Valor - 10 dígitos após a quantidade
            String valorStr = linha.substring(posQuantidade + 3, posQuantidade + 13).trim();
            System.out.println("🔹 Valor (" + (posQuantidade+3) + "-" + (posQuantidade+13) + "): '" + valorStr + "'");
            campos.put("valor", converterDouble(valorStr));

            // Data - últimos 10 caracteres
            String dataStr = linha.substring(linha.length() - 10).trim();
            System.out.println("🔹 Data (" + (linha.length()-10) + "-" + linha.length() + "): '" + dataStr + "'");
            campos.put("data", dataStr);

            System.out.println("✅ Campos extraídos com sucesso!\n");
            return campos;

        } catch (Exception e) {
            System.out.println("❌ Erro ao extrair campos: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private int encontrarProximos4Digitos(String linha, int inicio) {
        for (int i = inicio; i <= linha.length() - 4; i++) {
            String substr = linha.substring(i, i + 4);
            if (substr.matches("\\d{4}")) {
                return i;
            }
        }
        throw new RuntimeException("Não encontrou 4 dígitos a partir da posição " + inicio);
    }

    private int encontrarProximos3Digitos(String linha, int inicio) {
        for (int i = inicio; i <= linha.length() - 3; i++) {
            String substr = linha.substring(i, i + 3);
            if (substr.matches("\\d{3}")) {
                return i;
            }
        }
        throw new RuntimeException("Não encontrou 3 dígitos a partir da posição " + inicio);
    }

    private Double converterDouble(String valorStr) {
        if (valorStr == null || valorStr.trim().isEmpty()) {
            throw new NumberFormatException("Valor vazio");
        }

        valorStr = valorStr.trim();
        System.out.println("💰 Convertendo valor: '" + valorStr + "'");

        // Remove zeros à esquerda
        valorStr = valorStr.replaceFirst("^0+", "");

        // Se não tem ponto, adiciona antes dos últimos 2 dígitos
        if (!valorStr.contains(".")) {
            if (valorStr.length() > 2) {
                String parteInteira = valorStr.substring(0, valorStr.length() - 2);
                String parteDecimal = valorStr.substring(valorStr.length() - 2);
                valorStr = parteInteira + "." + parteDecimal;
                System.out.println("💰 Valor convertido: '" + valorStr + "'");
            } else if (valorStr.length() == 2) {
                valorStr = "0." + valorStr;
            } else if (valorStr.length() == 1) {
                valorStr = "0.0" + valorStr;
            }
        }

        return Double.parseDouble(valorStr);
    }

    private void processarCampos(Map<String, Object> campos) {
        try {
            // Extrai os valores do mapa
            Integer codigoProduto = (Integer) campos.get("codigoProduto");
            String nomeProduto = (String) campos.get("nomeProduto");
            Integer codigoVendedor = (Integer) campos.get("codigoVendedor");
            String nomeVendedor = (String) campos.get("nomeVendedor");
            Integer quantidade = (Integer) campos.get("quantidade");
            Double valor = (Double) campos.get("valor");
            String dataStr = (String) campos.get("data");

            // Converte a data
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate data = LocalDate.parse(dataStr, formatter);

            // Processa cliente e produto
            Cliente cliente = processarCliente(codigoVendedor, nomeVendedor);
            Produto produto = processarProduto(codigoProduto, nomeProduto, valor);

            // Cria e salva a venda
            Venda venda = new Venda();
            venda.setCliente(cliente);
            venda.setProduto(produto);
            venda.setQuantidade(quantidade);
            venda.setValorTotal(valor * quantidade);
            venda.setDataVenda(data);

            vendaRepository.save(venda);

            System.out.println("✅ Venda processada: " + produto.getNome() + " para " + cliente.getNome());

        } catch (Exception e) {
            System.out.println("❌ Erro ao processar campos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Cliente processarCliente(Integer codigo, String nome) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(codigo);

        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            if (!cliente.getNome().equals(nome)) {
                cliente.setNome(nome);
                clienteRepository.save(cliente);
            }
            return cliente;
        } else {
            Cliente novoCliente = new Cliente();
            novoCliente.setId(codigo);
            novoCliente.setNome(nome);
            return clienteRepository.save(novoCliente);
        }
    }

    private Produto processarProduto(Integer codigo, String nome, Double preco) {
        Optional<Produto> produtoExistente = produtoRepository.findById(codigo);

        if (produtoExistente.isPresent()) {
            Produto produto = produtoExistente.get();
            if (!produto.getPreco().equals(preco)) {
                produto.setPreco(preco);
                produtoRepository.save(produto);
            }
            return produto;
        } else {
            Produto novoProduto = new Produto();
            novoProduto.setId(codigo);
            novoProduto.setNome(nome);
            novoProduto.setPreco(preco);
            return produtoRepository.save(novoProduto);
        }
    }
}