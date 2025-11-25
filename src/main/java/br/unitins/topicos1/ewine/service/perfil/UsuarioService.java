package br.unitins.topicos1.ewine.service.perfil;


import br.unitins.topicos1.ewine.model.perfil.Usuario;

import java.util.List;
import br.unitins.topicos1.ewine.dto.perfil.UsuarioDTO;


public interface UsuarioService {
    List<Usuario> findAll();
    Usuario findById(Long id);
    Usuario findByLogin(String login);
    Usuario findByLoginAndSenha(String login, String senhaPlain);
    Usuario create(UsuarioDTO dto);
    void update(Long id, UsuarioDTO dto);
    void delete(Long id);
}