package br.unitins.topicos1.ewine.model.comercio;

import java.time.LocalDateTime;

import br.unitins.topicos1.ewine.model.others.DefaultEntity;
import br.unitins.topicos1.ewine. model.wineentities.Vinho;
import jakarta.persistence.*;

@Entity
public class Produto extends DefaultEntity {
    
    @Column(unique = true, nullable = false)
    private String sku;  // Código único do produto
    
    @Column(nullable = false)
    private Double preco;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    private String descricaoComercial;
    
    @OneToOne
    @JoinColumn(name = "id_vinho", nullable = false)
    private Vinho vinho;
    
    @OneToOne(mappedBy = "produto", cascade = CascadeType.ALL, fetch = FetchType. LAZY)
    private Estoque estoque;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;
    

// Getters e Setters
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    
    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this. ativo = ativo; }
    
    public String getDescricaoComercial() { return descricaoComercial; }
    public void setDescricaoComercial(String descricaoComercial) { this.descricaoComercial = descricaoComercial; }
    
    public Vinho getVinho() { return vinho; }
    public void setVinho(Vinho vinho) { this.vinho = vinho; }
    
    public Estoque getEstoque() { return estoque; }
    public void setEstoque(Estoque estoque) { this.estoque = estoque; }
   

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}