package br.unitins.topicos1.ewine.repository.perfil;

import br.unitins.topicos1.ewine.model.perfil.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.panache.common.Page;
import java.util.List;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {

    public Usuario findByLogin(String login) {
        return find("login = ?1", login).firstResult();
    }

    public Usuario findByLoginSenha(String login, String senhaHashed) {
        return find("login = ?1 and senha = ?2", login, senhaHashed).firstResult();
    }

    public List<Usuario> listAllPaged(int page, int size) {
        return findAll().page(Page.of(page, size)).list();
    }
}