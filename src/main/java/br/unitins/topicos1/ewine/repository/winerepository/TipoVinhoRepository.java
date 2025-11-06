package br.unitins.topicos1.ewine.repository.winerepository;

import java.util.List;

import br.unitins.topicos1.ewine.model.wineentities.TipoVinho;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TipoVinhoRepository implements PanacheRepository <TipoVinho> {
    

    public List<TipoVinho> findByNome(String nome) {
        return find("nome like ?1", "%" + nome + "%").list();
    }
}
