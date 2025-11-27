package br.unitins.topicos1.ewine.model.wineentities;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import br.unitins.topicos1.ewine.model.others.DefaultEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Entity
@Schema(hidden = true)
public class Uva extends DefaultEntity {
    private String nome;
     @ManyToMany(mappedBy = "uva")
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}