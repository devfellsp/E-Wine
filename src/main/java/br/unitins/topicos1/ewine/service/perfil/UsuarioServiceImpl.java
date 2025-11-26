package br.unitins.topicos1.ewine.service.perfil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import br.unitins.topicos1.ewine.dto.perfil.UsuarioDTO;
import br.unitins.topicos1.ewine.model.perfil.Perfil;
import br.unitins.topicos1.ewine.model.perfil.Usuario;
import br.unitins.topicos1.ewine.repository.perfil.UsuarioRepository;


@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {

    @Inject
    UsuarioRepository repository;

    @Override
    public List<Usuario> findAll() {
        return repository.listAll();
    }

    @Override
    public Usuario findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Usuario findByLogin(String login) {
        return repository.findByLogin(login);
    }

    @Override
    public Usuario findByLoginAndSenha(String login, String senhaPlain) {
        String hashed = hashSenha(senhaPlain);
        return repository.findByLoginSenha(login, hashed);
    }

    @Override
    @Transactional
    public Usuario create(UsuarioDTO dto) {
        if (dto == null) return null;

        if (dto.login() == null || dto.login().isBlank())
            throw new IllegalArgumentException("login é obrigatório");
        if (dto.senha() == null || dto.senha().isBlank())
            throw new IllegalArgumentException("senha é obrigatória");
        if (repository.findByLogin(dto.login()) != null)
            throw new IllegalArgumentException("login já existe");

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setLogin(dto.login());
        usuario.setSenha(hashSenha(dto.senha()));

        if (dto.perfil() != null && !dto.perfil().isBlank()) {
            usuario.setPerfil(parsePerfil(dto.perfil()));
        } else {
            throw new IllegalArgumentException("perfil é obrigatório");
        }

        repository.persist(usuario);
        return usuario;
    }

    @Override
    @Transactional
    public void update(Long id, UsuarioDTO dto) {
        Usuario usuario = repository.findById(id);
        if (usuario == null)
            throw new IllegalArgumentException("Usuário não encontrado");

        if (dto.nome() != null && !dto.nome().isBlank())
            usuario.setNome(dto.nome());

        if (dto.login() != null && !dto.login().isBlank()) {
            Usuario existente = repository.findByLogin(dto.login());
            if (existente != null && !existente.getId().equals(id))
                throw new IllegalArgumentException("login já em uso por outro usuário");
            usuario.setLogin(dto.login());
        }

        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenha(hashSenha(dto.senha()));
        }

        if (dto.perfil() != null && !dto.perfil().isBlank()) {
            usuario.setPerfil(parsePerfil(dto.perfil()));
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // --- helper: parsePerfil suporta id (String numérica), nome do enum, ou label ---
    private Perfil parsePerfil(String perfilStr) {
        String p = perfilStr.trim();

        // try numeric id (pode lançar NumberFormatException, que é capturado abaixo como IllegalArgumentException)
        try {
            Long id = Long.valueOf(p);
            return Perfil.valueOf(id); // usa o método valueOf(Long) do seu enum
        } catch (IllegalArgumentException e) {
            // continua para as outras tentativas (captura NumberFormatException e IllegalArgumentException do valueOf)
        }

        // try enum name (ADMIN, CLIENTE) — tornar case-insensitive
        try {
            return Perfil.valueOf(p.toUpperCase());
        } catch (IllegalArgumentException e) {
            // continua
        }

        // try label (ex: "Admin" ou "Cliente") case-insensitive
        for (Perfil perfil : Perfil.values()) {
            if (perfil.label.equalsIgnoreCase(p)) {
                return perfil;
            }
        }

        throw new IllegalArgumentException("perfil inválido: " + perfilStr + ". Valores válidos: "+
            java.util.Arrays.toString(Perfil.values()));
    }

    // --- helper: hash (SHA-256 usado aqui, troque por BCrypt em produção) ---
    private String hashSenha(String senha) {
        if (senha == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao hashear senha", e);
        }
    }
}