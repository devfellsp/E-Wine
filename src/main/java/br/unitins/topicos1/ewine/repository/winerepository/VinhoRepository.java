package br.unitins.topicos1.ewine.repository.winerepository;

import java.util.List;

import br.unitins.topicos1.ewine.model.wineentities.Vinho;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
@ApplicationScoped
public class VinhoRepository implements PanacheRepository <Vinho> {
       // 1. Busca por nome (parcial)
    public List<Vinho> findByNome(String nome) {
        return find("nome like ?1", "%" + nome + "%").list();
    }

    // 2. Busca por ID da Marca
    public List<Vinho> findByMarca(Long idMarca) {
        // Usa o relacionamento 'marca.id' definido na entidade Vinho
        return find("marca.id = ?1", idMarca).list();
    }
    
    // 3. Busca por ID do Tipo de Vinho
    public List<Vinho> findByTipoVinho(Long idTipoVinho) {
        // Usa o relacionamento 'tipoVinho.id' definido na entidade Vinho
        return find("tipoVinho.id = ?1", idTipoVinho).list();
    }
    
    // 4. Busca por ID do País de Origem
    public List<Vinho> findByPais(Long idPais) {
        // Usa o relacionamento 'pais.id' definido na entidade Vinho
        return find("pais.id = ?1", idPais).list();
    }
}
