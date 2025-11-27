// src/main/java/br/unitins/topicos1/ewine/repository/productrepository/ProdutoRepository. java
package br.unitins.topicos1.ewine.repository.comercio;
import java.util.List;
import java.util.Optional;

import br.unitins.topicos1.ewine.model.comercio.Produto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProdutoRepository implements PanacheRepository<Produto> {
    
    public Optional<Produto> findBySku(String sku) {
        return find("sku = ?1", sku).firstResultOptional();
    }
    
    public List<Produto> findAtivos() {
        return find("ativo = true"). list();
    }
    
    public List<Produto> findInativos() {
        return find("ativo = false").list();
    }
    
    public List<Produto> findByVinhoId(Long idVinho) {
        return find("vinho.id = ?1", idVinho).list();
    }
    
    public List<Produto> findByVinhoNome(String nomeVinho) {
        return find("vinho.nome ILIKE ?1", "%" + nomeVinho + "%").list();
    }
    
    public List<Produto> findByMarcaId(Long idMarca) {
        return find("vinho.marca.id = ?1", idMarca).list();
    }
    
    public List<Produto> findByTipoVinhoId(Long idTipoVinho) {
        return find("vinho.tipoVinho.id = ?1", idTipoVinho). list();
    }
    
    public List<Produto> findByPrecoRange(Double precoMin, Double precoMax) {
        return find("preco >= ?1 AND preco <= ?2", precoMin, precoMax).list();
    }
    
    public List<Produto> findDisponiveis() {
        return find("ativo = true AND estoque.quantidade > 0").list();
    }
    
    public List<Produto> findEsgotados() {
        return find("ativo = true AND estoque.quantidade = 0").list();
    }
    
    public boolean existsBySku(String sku) {
        return count("sku = ?1", sku) > 0;
    }
    
    public boolean existsBySkuAndIdNot(String sku, Long id) {
        return count("sku = ?1 AND id != ?2", sku, id) > 0;
    }
}