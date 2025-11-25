package br.unitins.topicos1.ewine.model.perfil;

import br.unitins.topicos1.ewine.model.others.DefaultEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Usuario extends DefaultEntity {
                
                    @NotBlank
                    @Column(nullable = false)
                    private String nome;
                
                    @NotBlank
                    @Column(unique = true, nullable = false)
                    private String login;
                
                    @JsonIgnore
                    @NotBlank
                    @Column(nullable = false)
                    private String senha;
                    
                    @Enumerated(EnumType.STRING)
                    @Column(nullable = false)
                    private Perfil perfil;

    public Usuario() { }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    // Exemplo: hash da senha ao setar — implemente hashing real com BCrypt no serviço
    public String getSenha() {
        return senha;
    }

    /**
     * Define a senha já em formato hashed.
     * NUNCA faça hashing aqui — faça no serviço (ex: UsuarioService.hashpw(...))
     * O campo armazena apenas o hash (BCrypt) da senha.
     */
    public void setSenha(String senhaHashed) {
        this.senha = senhaHashed;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

}
