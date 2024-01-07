package modelo;

import utils.FormaPagamento;
import utils.validadores.TipoEntrega;
import utils.validadores.ValidadorCEP;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Carrinho {
    private int idEmpresa;
    private Map<Integer, Produto> produtos = new HashMap<>();
    private Map<Integer, BigDecimal> quantidadeProduto = new HashMap<>();
    private BigDecimal quantidade = new BigDecimal(0);
    private BigDecimal valorTotal = new BigDecimal(0);
    private Usuario usuario;
    private Pedido pedido;

    public Carrinho(Usuario usuario, int idEmpresa) {
        this.idEmpresa = idEmpresa;
        this.usuario = usuario;
    }

    public Carrinho(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa){
        this.idEmpresa = idEmpresa;
    }
    public Map<Integer, Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(Map<Integer, Produto> produtos) {
        this.produtos = produtos;
    }

    public Map<Integer, BigDecimal> getQuantidadeProduto() {
        return quantidadeProduto;
    }

    public void setQuantidadeProduto(Map<Integer, BigDecimal> quantidadeProduto) {
        this.quantidadeProduto = quantidadeProduto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public boolean adicionarProdutoAoCarrinho(Produto produto, BigDecimal quantidade) {
            this.produtos.put(produto.getIdProduto(), produto);
            this.quantidadeProduto.put(produto.getIdProduto(), quantidade);
            valorTotal = valorTotal.add(produto.getPreco().multiply(quantidade));
            return true;
    }

    public boolean editarQuantidadeProdutoDaSacola(int id, BigDecimal quantidade) {
        if (produtos.get(id) != null) {
            quantidadeProduto.put(id, quantidade);
            return true;
        }
        System.out.println("Produto não encontrado!");
        return false;
    }

    public boolean removerProdutoDoCarrinho(int id) {
        if (produtos.remove(id) != null) {
            return true;
        }
        System.out.println("ID não encontrado!!");
        return false;

    }

    public void listarProdutosDoCarrinho() {
        for (int key : produtos.keySet()) {
            System.out.println("Número: " + key + " Nome do produto: " + produtos.get(key).getNome()
                    + " Quantidade: " + quantidadeProduto.get(key));
        }
        System.out.println("Valor final (Sem frete): " + valorTotal);
    }

    public void limparSacola() {
        quantidadeProduto = new HashMap<>();
        produtos = new HashMap<>();
    }

    public void finalizarPedido(FormaPagamento formaPagamento, LocalDate dataDeEntrega,
                                Endereco endereco, Cupom cupom, TipoEntrega tipoEntrega){

        if(ValidadorCEP.isCepValido(endereco.getCep()) != null){
            pedido = new Pedido(usuario.getId(), produtos, quantidadeProduto,
                    formaPagamento, dataDeEntrega, endereco, tipoEntrega, cupom, valorTotal);
        } else {
            System.out.println("CEP invalido - pedido não foi finalizado");
        }

    }
}