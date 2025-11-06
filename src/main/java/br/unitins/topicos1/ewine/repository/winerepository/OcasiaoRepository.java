package br.unitins.topicos1.ewine.repository.winerepository;

import java.util.List;

import br.unitins.topicos1.ewine.model.wineentities.Ocasiao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OcasiaoRepository implements PanacheRepository<Ocasiao> {
    
    // Busca por nome (parcial)
    public List<Ocasiao> findByNome(String nome) {
        return find("nome like ?1", "%" + nome + "%").list();
    }
}