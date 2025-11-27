package br.unitins.topicos1.ewine.model.comercio;

// src/main/java/br/unitins/topicos1/ewine/model/productentities/Estoque.java

import br.unitins.topicos1.ewine.model.others. DefaultEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Estoque extends DefaultEntity {
    
    @OneToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;
    
    @Column(nullable = false)
    private Integer quantidade = 0;
    
    @Column(nullable = false)
    private Integer estoqueMinimo = 5;
    
    @Column(nullable = false)
    private Integer estoqueMaximo = 1000;
    
    private LocalDateTime ultimaMovimentacao;
    
    private String observacoes;

    // Getters e Setters
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { 
        this.quantidade = quantidade;
        this.ultimaMovimentacao = LocalDateTime.now();
    }
    
    public Integer getEstoqueMinimo() { return estoqueMinimo; }
    public void setEstoqueMinimo(Integer estoqueMinimo) { this.estoqueMinimo = estoqueMinimo; }
    
    public Integer getEstoqueMaximo() { return estoqueMaximo; }
    public void setEstoqueMaximo(Integer estoqueMaximo) { this.estoqueMaximo = estoqueMaximo; }
    
    public LocalDateTime getUltimaMovimentacao() { return ultimaMovimentacao; }
    public void setUltimaMovimentacao(LocalDateTime ultimaMovimentacao) { this.ultimaMovimentacao = ultimaMovimentacao; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    // Métodos auxiliares
    public boolean isEstoqueBaixo() {
        return quantidade <= estoqueMinimo;
    }
    
    public boolean isEstoqueAlto() {
        return quantidade >= estoqueMaximo;
    }
}