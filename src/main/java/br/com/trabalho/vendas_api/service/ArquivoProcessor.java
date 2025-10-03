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

@Service
public class ArquivoProcessor {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private VendaRepository vendaRepository;

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

    private Map<String, Object> extrairCampos(String linha) {
        Map<String, Object> campos = new HashMap<>();

        try {
            System.out.println("📋 Processando linha: '" + linha + "'");
            System.out.println("📏 Tamanho da linha: " + linha.length());

            // CORREÇÃO: Busca dinâmica dos campos baseada em padrões

            // ID_PRODUTO (primeiros 4 dígitos)
            String idProdutoStr = linha.substring(0, 4).trim();
            System.out.println("🔹 ID Produto (0-3): '" + idProdutoStr + "'");
            campos.put("idProduto", Integer.parseInt(idProdutoStr));

            // NOME_PRODUTO (até encontrar 4 dígitos seguidos - ID_CLIENTE)
            int posIdCliente = encontrarProximos4Digitos(linha, 4);
            String nomeProduto = linha.substring(4, posIdCliente).trim();
            System.out.println("🔹 Nome Produto (4-" + (posIdCliente-1) + "): '" + nomeProduto + "'");
            campos.put("nomeProduto", nomeProduto);

            // ID_CLIENTE (4 dígitos após o nome do produto)
            String idClienteStr = linha.substring(posIdCliente, posIdCliente + 4).trim();
            System.out.println("🔹 ID Cliente (" + posIdCliente + "-" + (posIdCliente+3) + "): '" + idClienteStr + "'");
            campos.put("idCliente", Integer.parseInt(idClienteStr));

            // NOME_CLIENTE (até encontrar 3 dígitos seguidos - QTD_VENDIDA)
            int posQuantidade = encontrarProximos3Digitos(linha, posIdCliente + 4);
            String nomeCliente = linha.substring(posIdCliente + 4, posQuantidade).trim();
            System.out.println("🔹 Nome Cliente (" + (posIdCliente+4) + "-" + (posQuantidade-1) + "): '" + nomeCliente + "'");
            campos.put("nomeCliente", nomeCliente);

            // QTD_VENDIDA (3 dígitos)
            String quantidadeStr = linha.substring(posQuantidade, posQuantidade + 3).trim();
            System.out.println("🔹 Quantidade (" + posQuantidade + "-" + (posQuantidade+2) + "): '" + quantidadeStr + "'");
            campos.put("quantidade", Integer.parseInt(quantidadeStr));

            // VALOR_UNIT (10 dígitos após a quantidade)
            int posValor = posQuantidade + 3;
            String valorStr = linha.substring(posValor, posValor + 10).trim();
            System.out.println("🔹 Valor Unitário (" + posValor + "-" + (posValor+9) + "): '" + valorStr + "'");
            campos.put("valorUnitario", converterValorUnitario(valorStr));

            // DATA_VENDA (últimos 10 caracteres)
            String dataStr = linha.substring(linha.length() - 10).trim();
            System.out.println("🔹 Data Venda (" + (linha.length()-10) + "-" + (linha.length()-1) + "): '" + dataStr + "'");
            campos.put("dataVenda", dataStr);

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

    private Double converterValorUnitario(String valorStr) {
        if (valorStr == null || valorStr.trim().isEmpty()) {
            return 0.0;
        }

        valorStr = valorStr.trim();

        try {
            // Remove zeros à esquerda
            String valorSemZeros = valorStr.replaceFirst("^0+", "");

            // Se ficou vazio após remover zeros, é zero
            if (valorSemZeros.isEmpty()) return 0.0;

            // Se já tem ponto decimal, converte diretamente
            if (valorSemZeros.contains(".")) {
                return Double.parseDouble(valorSemZeros);
            }

            // Se não tem ponto, adiciona antes dos últimos 2 dígitos
            if (valorSemZeros.length() > 2) {
                String parteInteira = valorSemZeros.substring(0, valorSemZeros.length() - 2);
                String parteDecimal = valorSemZeros.substring(valorSemZeros.length() - 2);
                return Double.parseDouble(parteInteira + "." + parteDecimal);
            } else {
                // Se tem 1 ou 2 dígitos, é valor decimal pequeno
                return Double.parseDouble("0." + valorSemZeros);
            }

        } catch (Exception e) {
            System.out.println("❌ Erro ao converter valor: '" + valorStr + "'");
            return 0.0;
        }
    }

    private void processarCampos(Map<String, Object> campos) {
        try {
            // Extrai os valores do mapa
            Integer idProduto = (Integer) campos.get("idProduto");
            String nomeProduto = (String) campos.get("nomeProduto");
            Integer idCliente = (Integer) campos.get("idCliente");
            String nomeCliente = (String) campos.get("nomeCliente");
            Integer quantidade = (Integer) campos.get("quantidade");
            Double valorUnitario = (Double) campos.get("valorUnitario");
            String dataStr = (String) campos.get("dataVenda");

            // Converte a data
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate data = LocalDate.parse(dataStr, formatter);

            // Processa cliente e produto
            Cliente cliente = processarCliente(idCliente, nomeCliente);
            Produto produto = processarProduto(idProduto, nomeProduto, valorUnitario);

            // Verifica se a venda já existe (evita duplicação)
            boolean vendaExiste = vendaExiste(data, cliente, produto, quantidade);

            if (!vendaExiste) {
                // Cria e salva a venda
                Venda venda = new Venda();
                venda.setCliente(cliente);
                venda.setProduto(produto);
                venda.setQuantidade(quantidade);
                venda.setValorTotal(valorUnitario * quantidade);
                venda.setDataVenda(data);

                vendaRepository.save(venda);

                System.out.println("✅ Venda processada: " + quantidade + "x " + produto.getNome() +
                        " para " + cliente.getNome() + " - Total: R$ " + (valorUnitario * quantidade));
            } else {
                System.out.println("⚠️ Venda já existe: " + produto.getNome() + " para " + cliente.getNome());
            }

        } catch (Exception e) {
            System.out.println("❌ Erro ao processar campos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método auxiliar para verificar se venda já existe
    private boolean vendaExiste(LocalDate dataVenda, Cliente cliente, Produto produto, Integer quantidade) {
        return vendaRepository.findByDataVendaAndClienteIdAndProdutoId(dataVenda, cliente.getId(), produto.getId())
                .stream()
                .anyMatch(v -> v.getQuantidade().equals(quantidade));
    }

    private Cliente processarCliente(Integer id, String nome) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);

        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            // Atualiza nome se mudou
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

    private Produto processarProduto(Integer id, String nome, Double valorUnitario) {
        Optional<Produto> produtoExistente = produtoRepository.findById(id);

        if (produtoExistente.isPresent()) {
            Produto produto = produtoExistente.get();
            // Atualiza se nome ou preço mudaram
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