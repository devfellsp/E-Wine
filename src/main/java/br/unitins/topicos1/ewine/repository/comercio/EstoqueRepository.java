// src/main/java/br/unitins/topicos1/ewine/repository/productrepository/EstoqueRepository.java
package br.unitins.topicos1.ewine.repository.comercio;
import java.util.List;

import br.unitins.topicos1.ewine.model.comercio.Estoque;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EstoqueRepository implements PanacheRepository<Estoque> {
    
    public List<Estoque> findEstoqueBaixo() {
        return find("quantidade <= estoqueMinimo").list();
    }
    
    public List<Estoque> findEstoqueAlto() {
        return find("quantidade >= estoqueMaximo").list();
    }
    
    public List<Estoque> findEsgotados() {
        return find("quantidade = 0").list();
    }
    
    public List<Estoque> findByQuantidadeMenorQue(Integer quantidade) {
        return find("quantidade < ? 1", quantidade).list();
    }
    
    public Estoque findByProdutoId(Long idProduto) {
        return find("produto.id = ?1", idProduto).firstResult();
    }
}