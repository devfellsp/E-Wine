package br.unitins.topicos1.ewine.repository.winerepository;

import java.util.List;

import br.unitins.topicos1.ewine.model.wineentities.Safra;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
@ApplicationScoped
public class SafraRepository  implements PanacheRepository<Safra> {
    
     // Busca por ano (parcial/exata)
    public Safra findByAno(Integer ano) {
        return find("ano = ?1", ano).firstResult();
    }
    
    // Busca por lista de safras (para listagem)
    public List<Safra> findByAnoLike(String ano) {
        return find("CAST(ano AS string) like ?1", "%" + ano + "%").list();
    }
}
