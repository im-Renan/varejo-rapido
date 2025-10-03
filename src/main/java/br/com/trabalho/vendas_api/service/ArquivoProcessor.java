package br.com.trabalho.vendas_api.service;

import br.com.trabalho.vendas_api.model.Cliente;
import br.com.trabalho.vendas_api.model.Produto;
import br.com.trabalho.vendas_api.model.Venda;
import br.com.trabalho.vendas_api.repository.ClienteRepository;
import br.com.trabalho.vendas_api.repository.ProdutoRepository;
import br.com.trabalho.vendas_api.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Serviço responsável pelo processamento de arquivos de vendas (.dat).
 * <p>
 * Recebe arquivos via upload, lê linha a linha, extrai os campos, verifica e atualiza
 * clientes e produtos no banco de dados, e persiste vendas evitando duplicações.
 */
@Service
public class ArquivoProcessor {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private VendaRepository vendaRepository;

    /**
     * Processa um arquivo enviado via upload, lendo cada linha e persistindo os dados.
     *
     * @param arquivo arquivo enviado via MultipartFile
     * @return mensagem informando a quantidade de linhas processadas e erros
     */
    public String processarArquivoUpload(MultipartFile arquivo) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(arquivo.getInputStream()))) {
            String linha;
            int linhasProcessadas = 0;
            int linhasComErro = 0;

            while ((linha = br.readLine()) != null) {
                if (processarLinha(linha)) {
                    linhasProcessadas++;
                } else {
                    linhasComErro++;
                }
            }

            return String.format("✅ Arquivo processado com sucesso! %d linhas processadas, %d erros.",
                    linhasProcessadas, linhasComErro);

        } catch (IOException e) {
            return "❌ Erro ao processar arquivo: " + e.getMessage();
        }
    }

    /**
     * Processa uma linha do arquivo, extraindo os campos e salvando no banco.
     *
     * @param linha linha do arquivo
     * @return true se a linha foi processada com sucesso, false caso contrário
     */
    public boolean processarLinha(String linha) {
        if (linha == null || linha.trim().isEmpty()) {
            return false;
        }

        try {
            Map<String, Object> campos = extrairCampos(linha);

            if (campos == null) {
                System.out.println("❌ Formato inválido na linha: " + linha);
                return false;
            }

            processarCampos(campos);
            return true;

        } catch (Exception e) {
            System.out.println("❌ Erro ao processar linha: " + linha);
            System.out.println("Mensagem: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extrai os campos de uma linha do arquivo em um mapa.
     *
     * @param linha linha do arquivo
     * @return mapa com os campos extraídos
     */
    private Map<String, Object> extrairCampos(String linha) {
        Map<String, Object> campos = new HashMap<>();

        try {
            System.out.println("📋 Processando linha: '" + linha + "'");
            System.out.println("📏 Tamanho da linha: " + linha.length());

            // Extrai cada campo com base na posição e formato esperado
            String idProdutoStr = linha.substring(0, 4).trim();
            campos.put("idProduto", Integer.parseInt(idProdutoStr));

            int posIdCliente = encontrarProximos4Digitos(linha, 4);
            String nomeProduto = linha.substring(4, posIdCliente).trim();
            campos.put("nomeProduto", nomeProduto);

            String idClienteStr = linha.substring(posIdCliente, posIdCliente + 4).trim();
            campos.put("idCliente", Integer.parseInt(idClienteStr));

            int posQuantidade = encontrarProximos3Digitos(linha, posIdCliente + 4);
            String nomeCliente = linha.substring(posIdCliente + 4, posQuantidade).trim();
            campos.put("nomeCliente", nomeCliente);

            String quantidadeStr = linha.substring(posQuantidade, posQuantidade + 3).trim();
            campos.put("quantidade", Integer.parseInt(quantidadeStr));

            int posValor = posQuantidade + 3;
            String valorStr = linha.substring(posValor, posValor + 10).trim();
            campos.put("valorUnitario", converterValorUnitario(valorStr));

            String dataStr = linha.substring(linha.length() - 10).trim();
            campos.put("dataVenda", dataStr);

            return campos;

        } catch (Exception e) {
            System.out.println("❌ Erro ao extrair campos: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Procura os próximos 4 dígitos a partir de uma posição.
     */
    private int encontrarProximos4Digitos(String linha, int inicio) {
        for (int i = inicio; i <= linha.length() - 4; i++) {
            String substr = linha.substring(i, i + 4);
            if (substr.matches("\\d{4}")) {
                return i;
            }
        }
        throw new RuntimeException("Não encontrou 4 dígitos a partir da posição " + inicio);
    }

    /**
     * Procura os próximos 3 dígitos a partir de uma posição.
     */
    private int encontrarProximos3Digitos(String linha, int inicio) {
        for (int i = inicio; i <= linha.length() - 3; i++) {
            String substr = linha.substring(i, i + 3);
            if (substr.matches("\\d{3}")) {
                return i;
            }
        }
        throw new RuntimeException("Não encontrou 3 dígitos a partir da posição " + inicio);
    }

    /**
     * Converte string de valor unitário para Double, tratando zeros à esquerda.
     */
    private Double converterValorUnitario(String valorStr) {
        if (valorStr == null || valorStr.trim().isEmpty()) {
            return 0.0;
        }

        valorStr = valorStr.trim();

        try {
            String valorSemZeros = valorStr.replaceFirst("^0+", "");
            if (valorSemZeros.isEmpty()) return 0.0;

            if (valorSemZeros.contains(".")) {
                return Double.parseDouble(valorSemZeros);
            }

            if (valorSemZeros.length() > 2) {
                String parteInteira = valorSemZeros.substring(0, valorSemZeros.length() - 2);
                String parteDecimal = valorSemZeros.substring(valorSemZeros.length() - 2);
                return Double.parseDouble(parteInteira + "." + parteDecimal);
            } else {
                return Double.parseDouble("0." + valorSemZeros);
            }

        } catch (Exception e) {
            System.out.println("❌ Erro ao converter valor: '" + valorStr + "'");
            return 0.0;
        }
    }

    /**
     * Processa os campos extraídos, atualizando clientes, produtos e vendas.
     */
    private void processarCampos(Map<String, Object> campos) {
        try {
            Integer idProduto = (Integer) campos.get("idProduto");
            String nomeProduto = (String) campos.get("nomeProduto");
            Integer idCliente = (Integer) campos.get("idCliente");
            String nomeCliente = (String) campos.get("nomeCliente");
            Integer quantidade = (Integer) campos.get("quantidade");
            Double valorUnitario = (Double) campos.get("valorUnitario");
            String dataStr = (String) campos.get("dataVenda");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate data = LocalDate.parse(dataStr, formatter);

            Cliente cliente = processarCliente(idCliente, nomeCliente);
            Produto produto = processarProduto(idProduto, nomeProduto, valorUnitario);

            boolean vendaExiste = vendaExiste(data, cliente, produto, quantidade);

            if (!vendaExiste) {
                Venda venda = new Venda();
                venda.setCliente(cliente);
                venda.setProduto(produto);
                venda.setQuantidade(quantidade);
                venda.setValorTotal(valorUnitario * quantidade);
                venda.setDataVenda(data);

                vendaRepository.save(venda);

            }

        } catch (Exception e) {
            System.out.println("❌ Erro ao processar campos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Verifica se uma venda já existe para evitar duplicação.
     */
    private boolean vendaExiste(LocalDate dataVenda, Cliente cliente, Produto produto, Integer quantidade) {
        return vendaRepository.findByDataVendaAndClienteIdAndProdutoId(dataVenda, cliente.getId(), produto.getId())
                .stream()
                .anyMatch(v -> v.getQuantidade().equals(quantidade));
    }

    /**
     * Processa ou cria um cliente, atualizando o nome se necessário.
     */
    private Cliente processarCliente(Integer id, String nome) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);

        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            if (!cliente.getNome().equals(nome)) {
                cliente.setNome(nome);
                return clienteRepository.save(cliente);
            }
            return cliente;
        } else {
            Cliente novoCliente = new Cliente();
            novoCliente.setId(id);
            novoCliente.setNome(nome);
            return clienteRepository.save(novoCliente);
        }
    }

    /**
     * Processa ou cria um produto, atualizando nome e preço se necessário.
     */
    private Produto processarProduto(Integer id, String nome, Double valorUnitario) {
        Optional<Produto> produtoExistente = produtoRepository.findById(id);

        if (produtoExistente.isPresent()) {
            Produto produto = produtoExistente.get();
            boolean atualizado = false;
            if (!produto.getNome().equals(nome)) {
                produto.setNome(nome);
                atualizado = true;
            }
            if (!produto.getPreco().equals(valorUnitario)) {
                produto.setPreco(valorUnitario);
                atualizado = true;
            }
            if (atualizado) {
                return produtoRepository.save(produto);
            }
            return produto;
        } else {
            Produto novoProduto = new Produto();
            novoProduto.setId(id);
            novoProduto.setNome(nome);
            novoProduto.setPreco(valorUnitario);
            return produtoRepository.save(novoProduto);
        }
    }
}
